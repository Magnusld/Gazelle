import { createLocalVue, shallowMount, mount } from "@vue/test-utils";
import VueMaterial from "vue-material";
import VueMq from "vue-mq";
import LoginView from "@/views/LoginView.vue";
import LoginPage from "@/components/LoginPage.vue";
import VueRouter from "vue-router";
import { LogInResponse } from "@/client/types";
import store from "@/store";
import SignUpView from "@/views/SignUpView.vue";
import SignUpPage from "@/components/SignUpPage.vue";
import nock from "nock";

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

const scope = nock("http://localhost:8088/");

function delay(ms: number) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

describe("login.ts", () => {
  beforeEach(() => {
    store.commit("logout");
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
        user: { firstname: "Per", lastname: "Jensen", id: 6 },
        token: "3456"
      };
      scope.post("/login").reply(200, data2);
      await button.trigger("click");
    } catch (error) {
      expect(error).toEqual("Already logged in");
    }
  });
  it("Tester oppsett av SignUpView", () => {
    const wrapper = shallowMount(SignUpView, {
      localVue,
      router
    });
    expect(wrapper.findComponent(SignUpPage).exists()).toBe(true);
    expect(wrapper.find("#logo").exists()).toBe(true);
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
      user: { firstname: "Ola", lastname: "Normann", id: 7 },
      token: "1234"
    };
    scope.post("/signup").reply(200, data);
    await button.trigger("click");
    await delay(300);
    expect(store.getters.token).toEqual("1234");
  });
});
