import { createLocalVue, shallowMount, mount } from "@vue/test-utils";
import VueMaterial from "vue-material";
import LoginView from "@/views/LoginView.vue";
import LoginPage from "@/components/LoginPage.vue";
import VueRouter from "vue-router";

const localVue = createLocalVue();
localVue.use(VueMaterial);
const router = new VueRouter();
localVue.use(VueRouter);
jest.mock('axios');

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
    expect(wrapper.props().invalidated).toBe(true)
    expect(wrapper.find(".errorMessage").exists()).toEqual(true);
  });
  it("Tester at login knapp fungerer som forventet", async () => {
    const data = {
      data: {
        user: [

        ]
      }
    }
    const wrapper = mount(LoginPage, {
      localVue,
      router,
    });
    const button = wrapper.findAll(".loginButton").at(0);
    expect(button.exists()).toEqual(true);
    await expect(button.trigger('click'));
  })
});

// Sjekke hvorvidt .errorMessage eksisterer når jeg sender invalidated data
