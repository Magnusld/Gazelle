import { createLocalVue, shallowMount, mount } from "@vue/test-utils";
import VueMaterial from "vue-material";
import VueMq from "vue-mq";
import LoginView from "@/views/LoginView.vue";
import LoginPage from "@/components/LoginPage.vue";
import VueRouter from "vue-router";
import { LogInResponse } from "@/client/types";
import store from "@/store";
import SignUpPage from "@/components/SignUpPage.vue";
import nock from "nock";
import TopBar from "@/components/TopBar.vue";

const localVue = createLocalVue();
localVue.use(VueMaterial);
const router = new VueRouter();
localVue.use(VueRouter);
// eslint-disable-next-line
window.matchMedia = (query: string) =>
  ({
    matches: true,
    media: "hei",
    onchange: null,
    addListener: () => null,
    removeListener: () => null,
    addEventListener: () => null,
    removeEventListener: () => null
    // eslint-disable-next-line
  } as any);
localVue.use(VueMq, {
  breakpoints: {
    mobile: 450,
    tablet: 800,
    laptop: 1250,
    desktop: Infinity
  },
  defaultBreakpoint: "mobile"
});

const baseURL = "http://localhost:8088/";

const scope: nock.Scope = nock(baseURL);

function delay(ms: number) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

describe("login.ts", () => {
  beforeEach(() => {
    store.commit("logout");
    nock.cleanAll();
  });

  it("Sjekker om loginView inneholder en loginPage", () => {
    const wrapper = shallowMount(LoginView, {
      localVue,
      router
    });
    expect(wrapper.findComponent(LoginPage).exists()).toEqual(true);
  });
  it("Tester at loginPage ikke viser error når det ikke er noe error", () => {
    const wrapper = shallowMount(LoginPage, {
      localVue,
      router
    });
    expect(wrapper.findAll(".errorMessage").exists()).toEqual(false);
  });
  it("Tester at loginPage viser error når route ber om det", async () => {
    const wrapper = mount(LoginPage, {
      localVue,
      router,
      propsData: {
        invalidated: true
      }
    });
    expect(wrapper.props().invalidated).toBe(true);
    expect(wrapper.find(".errorMessage").exists()).toEqual(true);
  });
  it("Tester at login knapp fungerer som forventet", async () => {
    const wrapper = mount(LoginPage, {
      localVue,
      router,
      data: () => ({
        email: "test@test.no",
        id: 3456
      })
    });
    const button = wrapper.findAll(".loginButton").at(0);
    expect(button.exists()).toEqual(true);
    expect(wrapper.vm.$data.email).toEqual("test@test.no");
    const data: LogInResponse = {
      user: { firstname: "Per", lastname: "Jensen", id: 6 },
      token: "3456"
    };
    scope.post("/login").reply(200, data);
    await button.trigger("click");
    await delay(300);
    expect(store.getters.token).toEqual("3456");
    try {
      const data2: LogInResponse = {
        user: { firstname: "Karl", lastname: "Hansen", id: 8 },
        token: "7890"
      };
      scope.post("/login").reply(200, data2);
      await button.trigger("click");
      await delay(300);
    } catch (error) {
      expect(error).toEqual("Already logged in");
    }
  });
  it("Tester oppsett av SignUpView", () => {
    const wrapper = shallowMount(LoginView, {
      localVue,
      router,
      propsData: {
        mode: "signUp"
      }
    });
    expect(wrapper.findComponent(SignUpPage).exists()).toBe(true);
  });
  it("Tester oppretting av nye brukere", async () => {
    const wrapper = mount(SignUpPage, {
      localVue,
      router,
      data: () => ({
        firstname: "Ola",
        lastname: "Nordmann",
        email: "test@test.no",
        password: "OlaNord123",
        repeatedPassword: "OlaNord123"
      })
    });
    const button = wrapper.find(".signUpButton");
    expect(button.exists()).toBe(true);
    expect(wrapper.vm.$data.firstname).toEqual("Ola");
    expect(wrapper.vm.$data.email).toEqual("test@test.no");
    const data: LogInResponse = {
      user: { firstname: "Ola", lastname: "Nordmann", id: 7 },
      token: "1234"
    };
    scope.post("/signup").reply(200, data);
    await button.trigger("click");
    await delay(300);
    expect(store.getters.token).toEqual("1234");
    try {
      const data2: LogInResponse = {
        user: { firstname: "Ola", lastname: "Nordmann", id: 7 },
        token: "2345"
      };
      scope.post("/login").reply(200, data2);
      await button.trigger("click");
    } catch (error) {
      expect(error).toEqual("Already logged in");
    }
  });
  it("Tester om logg ut knapp fungerer som forventet", async () => {
    const wrapper = mount(LoginPage, {
      localVue,
      router,
      data: () => ({
        email: "test@test.no",
        id: 3456
      })
    });
    const loginButton = wrapper.findAll(".loginButton").at(0);
    const data: LogInResponse = {
      user: { firstname: "Per", lastname: "Jensen", id: 6 },
      token: "3456"
    };
    scope.post("/login").reply(200, data);
    await loginButton.trigger("click");
    await delay(300);
    expect(store.getters.token).toEqual("3456");
    const wrapper2 = mount(TopBar, {
      localVue,
      router,
      store,
      stubs: ["md-tabs", "md-tab"]
    });
    const logoutButton = wrapper2.findAll(".logoutButton").at(0);
    expect(logoutButton.exists()).toEqual(true);
    scope.post("/logout").reply(200);
    await logoutButton.trigger("click");
    await delay(300);
    expect(store.getters.token).toEqual(null);
  });
});
