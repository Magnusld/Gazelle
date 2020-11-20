import { createLocalVue, shallowMount } from "@vue/test-utils";
import VueMaterial from "vue-material";
import VueRouter from "vue-router";
import nock from "nock";
import store from "@/store";
import { UserResponse } from "@/client/types";
import TopBar from "@/components/TopBar.vue";
import VueMq from "vue-mq";

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

// const baseURL = "http://localhost:8088/";

// const scope: nock.Scope = nock(baseURL);

function delay(ms: number) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

const user: UserResponse = {
  id: 1,
  firstname: "Ola",
  lastname: "Nordmann"
};

describe("Teste TopBar", () => {
  beforeEach(() => {
    store.commit("logout");
    nock.cleanAll();
  });
  it("Teste at TopBar vises riktig når bruker logget inn", async () => {
    store.commit("authSuccess", { user, token: 4567 });
    const wrapper = shallowMount(TopBar, {
      localVue,
      router,
      store
    });
    await delay(300);
    expect(wrapper.find("#topBar").exists()).toEqual(true);
  });
  it("Teste at TopBar ikke vises når bruker ikke logget inn", async () => {
    const wrapper = shallowMount(TopBar, {
      localVue,
      router,
      store
    });
    expect(wrapper.find("#topBar").exists()).toEqual(false);
  });
});
