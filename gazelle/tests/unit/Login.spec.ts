import { createLocalVue, shallowMount } from "@vue/test-utils";
import VueMaterial from "vue-material";
import LoginView from "@/views/LoginView.vue";
import LoginPage from "@/components/LoginPage.vue";
import VueRouter from "vue-router";

const localVue = createLocalVue();
localVue.use(VueMaterial);
const router = new VueRouter();
localVue.use(VueRouter);

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
    expect(wrapper.findAll("div").length).toEqual(2);
  });
});

// Sjekke hvorvidt .errorMessage eksisterer når jeg sender invalidated data
