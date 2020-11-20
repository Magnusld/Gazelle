import { createLocalVue, mount } from "@vue/test-utils";
import VueMaterial from "vue-material";
import VueRouter from "vue-router";
import nock from "nock";
import store from "@/store";
import {
  ChoreProgress,
  PostContentResponse,
  UserResponse
} from "@/client/types";
import PostView from "@/views/PostView.vue";
import PostEdit from "@/components/PostEdit.vue";
import ChoreListing from "@/components/ChoreListing.vue";
import PostComponent from "@/components/PostComponent.vue";
import PostChore from "@/components/PostChore.vue";

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

describe("PostView tester", () => {
  beforeEach(() => {
    store.commit("logout");
    nock.cleanAll();
    store.commit("authSuccess", { user, token: 4567 });
  });
  it("Teste at PostView oppfører seg som forventet for nye poster", async () => {
    const wrapper = mount(PostView, {
      localVue,
      router,
      store,
      propsData: {
        mode: "new",
        id: 1
      },
      data: () => ({
        title: "Test",
        description: "Dette er en testpost",
        chores: null,
        startDate: "2020-19-11",
        endDate: "2020-20-11",
        nextKey: 1
      })
    });
    const pEdit = wrapper.findComponent(PostEdit);
    expect(pEdit.exists()).toEqual(true);
    expect(pEdit.vm.$data.chores).toEqual([]);
    pEdit.find(".addChoreButton").trigger("click");
    expect(pEdit.vm.$data.chores.length).toEqual(1);
    await pEdit.vm.$forceUpdate();
    expect(pEdit.findAllComponents(ChoreListing).length).toEqual(1);
    const cListing = pEdit.findAllComponents(ChoreListing).at(0);
    expect(cListing.exists()).toEqual(true);
    await cListing.setData({ description: "Dette er en TestChore" });
    expect(cListing.vm.$props.chore.text).toEqual("Dette er en TestChore");
    await cListing.find(".deleteChoreButton").trigger("click");
    expect(pEdit.vm.$data.chores.length).toEqual(0);
    scope.post("/courses/1/posts").reply(200);
    await pEdit.find(".sendPostButton").trigger("click");
    await delay(300);
  });
  it("Teste PostView for viewing av post", async () => {
    const data: PostContentResponse = {
      id: 1,
      title: "TestPost",
      description: "Dette er en test post",
      startDate: [0, 0, 0],
      endDate: [0, 0, 0],
      courseId: 1,
      courseName: "Test",
      isOwning: true,
      chores: [
        {
          id: 1,
          key: 1,
          text: "Dette er en test Chore",
          dueDate: null,
          progress: ChoreProgress.UNDONE
        }
      ]
    };
    scope.get("/posts/1").reply(200, data);
    const wrapper = mount(PostView, {
      localVue,
      router,
      store,
      propsData: {
        mode: "view",
        id: 1
      }
    });
    await delay(300);
    const pComponent = wrapper.findComponent(PostComponent);
    expect(pComponent.exists()).toEqual(true);
    expect(pComponent.find(".editPostButton").exists()).toEqual(true);
    expect(pComponent.findAllComponents(PostChore).length).toEqual(1);
    const pChore = pComponent.findAllComponents(PostChore).at(0);
    pChore.setData({ checked: false });
    expect(pChore.find(".toggleFocusButton").exists()).toEqual(true);
    scope.put("/users/1/chores/1/progress").reply(200);
    await pChore.find(".toggleFocusButton").trigger("click");
    expect(pChore.vm.$data.isFocused).toEqual(true);
    await delay(300);
    scope.put("/users/1/chores/1/progress").reply(200);
    await pChore.setData({ checked: true });
    await delay(300);
    expect(pChore.vm.$data.checked).toEqual(true);
    scope.put("/users/1/chores/1/progress").reply(200);
    await pChore.setData({ checked: false });
    await delay(300);
    expect(pChore.vm.$data.isFocused).toEqual(false);
  });
  it("Teste at PostView oppfører seg som forventet for redigering av poster", async () => {
    const data: PostContentResponse = {
      id: 1,
      title: "Test",
      description: "Dette er en testpost",
      startDate: [0, 0, 0],
      endDate: [0, 0, 0],
      courseId: 1,
      courseName: "TestCourse",
      isOwning: true,
      chores: []
    };
    scope.get("/posts/1").reply(200, data);
    const wrapper = mount(PostView, {
      localVue,
      router,
      store,
      propsData: {
        mode: "edit",
        id: 1
      }
    });
    await delay(300);
    const pEdit = wrapper.findComponent(PostEdit);
    expect(pEdit.exists()).toEqual(true);
    expect(pEdit.vm.$data.chores).toEqual([]);
    pEdit.setData({ description: "Beskrivelsen har blit endret på" });
    scope.put("/posts/1").reply(200);
    await pEdit.find(".sendPostButton").trigger("click");
    await delay(300);
    expect(pEdit.vm.$data.description).toEqual(
      "Beskrivelsen har blit endret på"
    );
  });
  it("Teste PostView for viewing av post som follower", async () => {
    const data: PostContentResponse = {
      id: 1,
      title: "TestPost",
      description: "Dette er en test post",
      startDate: [0, 0, 0],
      endDate: [0, 0, 0],
      courseId: 1,
      courseName: "Test",
      isOwning: false,
      chores: [
        {
          id: 1,
          key: 1,
          text: "Dette er en test Chore",
          dueDate: null,
          progress: ChoreProgress.UNDONE
        }
      ]
    };
    scope.get("/posts/1").reply(200, data);
    const wrapper = mount(PostView, {
      localVue,
      router,
      store,
      propsData: {
        mode: "view",
        id: 1
      }
    });
    await delay(300);
    const pComponent = wrapper.findComponent(PostComponent);
    expect(pComponent.exists()).toEqual(true);
    expect(pComponent.find(".editPostButton").exists()).toEqual(false);
  });
});
