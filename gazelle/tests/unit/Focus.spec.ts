import { createLocalVue, mount } from "@vue/test-utils";
import VueMaterial from "vue-material";
import VueRouter from "vue-router";
import nock from "nock";
import { ChoreFullResponse, ChoreProgress, UserResponse } from "@/client/types";
import store from "@/store";
import FocusList from "@/views/FocusList.vue";
import FocusChore from "@/components/FocusChore.vue";

const localVue = createLocalVue();
localVue.use(VueMaterial);
const router = new VueRouter();
localVue.use(VueRouter);

const baseURL = "http://localhost:8088/";

const scope: nock.Scope = nock(baseURL);

function delay(ms: number) {
  return new Promise(resolve => setTimeout(resolve, ms));
}
const user: UserResponse = {
  id: 1,
  firstname: "Ola",
  lastname: "Nordmann"
};

describe("FocusView og FocusPost tester", () => {
  beforeEach(() => {
    store.commit("logout");
    nock.cleanAll();
    store.commit("authSuccess", { user, token: 4567 });
  });
  it("Test av FocusList", async () => {
    const data2: ChoreFullResponse = {
      id: 1,
      key: 1,
      text: "TestChore",
      dueDate: null,
      progress: ChoreProgress.UNDONE,
      postId: 1,
      postTitle: "TestPost",
      courseId: 1,
      courseName: "TestName"
    };
    const data: ChoreFullResponse[] = [data2];
    scope.get("/users/1/focusedChores/").reply(200, data);
    const wrapper = mount(FocusList, {
      localVue,
      router,
      store
    });
    await delay(500);
    expect(wrapper.vm.$data.chores).toEqual(data);
    const fChore = wrapper.findAllComponents(FocusChore).at(0);
    expect(fChore.exists()).toEqual(true);
    expect(fChore.vm.$props.chore).toEqual(data2);
    fChore.setData({ checked: false });
    expect(fChore.find(".toggleFocusButton").exists()).toEqual(true);
    scope.put("/users/1/chores/1/progress").reply(200);
    await fChore.find(".toggleFocusButton").trigger("click");
    expect(fChore.vm.$data.isFocused).toEqual(true);
    await delay(300);
    scope.put("/users/1/chores/1/progress").reply(200);
    await fChore.setData({ checked: true });
    await delay(300);
    expect(fChore.vm.$data.checked).toEqual(true);
    scope.put("/users/1/chores/1/progress").reply(200);
    await fChore.setData({ checked: false });
    await delay(300);
    scope.put("/users/1/chores/1/progress").reply(200);
    await fChore.find(".toggleFocusButton").trigger("click");
    await delay(300);
    expect(fChore.vm.$data.isFocused).toEqual(false);
  });
  it("Test av tom focusList", async () => {
    const data: ChoreFullResponse[] = [];
    scope.get("/users/1/focusedChores/").reply(200, data);
    const wrapper = mount(FocusList, {
      localVue,
      router,
      store
    });
    await delay(300);
    expect(wrapper.vm.$data.chores).toEqual(data);
  });
});
