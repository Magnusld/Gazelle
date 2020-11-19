import { createLocalVue, mount } from "@vue/test-utils";
import VueMaterial from "vue-material";
import VueRouter from "vue-router";
import nock from "nock";
import {
  CourseContentResponse,
  PostResponse,
  UserResponse
} from "@/client/types";
import store from "@/store";
import CoursePage from "@/views/CoursePage.vue";
import PostList from "@/components/PostList.vue";

const localVue = createLocalVue();
localVue.use(VueMaterial);
const router = new VueRouter();
localVue.use(VueRouter);

const baseURL = "http://localhost:8088/";

const scope: nock.Scope = nock(baseURL);

function delay(ms: number) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

describe("CoursePageAndPost test", () => {
  it("Teste at CoursePage fungerer som forventet for eier", async () => {
    const user: UserResponse = {
      id: 1,
      firstname: "Ola",
      lastname: "Nordmann"
    };
    store.commit("authSuccess", { user, token: 4567 });
    const data2: PostResponse[] = [
      {
        id: 3,
        title: "Test",
        description: "Test",
        startDate: "2020-11-18",
        endDate: "2020-11-19",
        choresDone: null,
        choresFocused: null,
        choresCount: 0
      }
    ];
    const data1: CourseContentResponse = {
      id: 2,
      name: "test",
      isOwner: true,
      isFollower: false,
      posts: data2
    };
    scope.get("/courses/2").reply(200, data1);
    const wrapper = mount(CoursePage, {
      localVue,
      router,
      store,
      propsData: {
        courseId: 2
      }
    });
    await delay(300);
    expect(wrapper.vm.$data.course).toEqual(data1);
    const pList = wrapper.findComponent(PostList);
    expect(pList.exists()).toEqual(true);
  });
});
