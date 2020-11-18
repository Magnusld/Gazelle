import { createLocalVue, shallowMount, mount } from "@vue/test-utils";
import VueMaterial from "vue-material";
import LoginView from "@/views/LoginView.vue";
import LoginPage from "@/components/LoginPage.vue";
import VueRouter from "vue-router";
import { LogInResponse } from "@/client/types";
import store from "@/store";

// eslint-disable-next-line
const nock = require("nock");

const localVue = createLocalVue();
localVue.use(VueMaterial);
const router = new VueRouter();
localVue.use(VueRouter);

const scope = nock("http://localhost:8088/");

jest.mock("axios");

describe("LoginView.vue", () => {
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
    expect(store.getters.token === "3456");
  });
});
