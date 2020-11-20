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
import PostListing from "@/components/PostListing.vue";

const localVue = createLocalVue();
localVue.use(VueMaterial);
const router = new VueRouter();
localVue.use(VueRouter);

const baseURL = "http://localhost:8088/";

const scope: nock.Scope = nock(baseURL);

function delay(ms: number) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

describe("CoursePageAndPostList test", () => {
  beforeEach(() => {
    store.commit("logout");
    nock.cleanAll();
  });

  it("Tester at CoursePage fungerer som forventet for eier", async () => {
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
        startDate: [0, 0, 0],
        endDate: [0, 0, 0],
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
    await delay(600);
    expect(wrapper.vm.$data.course).toEqual(data1);
    const pList = wrapper.findComponent(PostList);
    expect(pList.exists()).toEqual(true);
    expect(pList.vm.$props.owner).toEqual(true);
    await pList.find(".addNewPostButton").trigger("click");
    await pList.find(".deletePostButton").trigger("click");
    expect(pList.vm.$data.deletable).toEqual(true);
    const pListing = pList.findComponent(PostListing);
    expect(pListing.exists()).toEqual(true);
    await pListing.setData({ checked: true });
    expect(pList.vm.$data.postsToDelete.length).toEqual(1);
    scope.delete("/posts/3").reply(200);
    scope.get("/courses/2").reply(200, data1);
    await pList.find(".deletePostButton").trigger("click");
    await delay(600);
    expect(pList.vm.$data.postsToDelete.length).toEqual(0);
    expect(pList.vm.$data.deletable).toEqual(false);
  });
  it("Tester at CoursePage oppfører seg som forventet for følger", async () => {
    const user: UserResponse = {
      id: 4,
      firstname: "Kari",
      lastname: "Nordmann"
    };
    store.commit("authSuccess", { user, token: 4567 });
    const data2: PostResponse[] = [
      {
        id: 6,
        title: "Test",
        description: "Test",
        startDate: [0, 0, 0],
        endDate: [0, 0, 0],
        choresDone: null,
        choresFocused: null,
        choresCount: 0
      }
    ];
    const data1: CourseContentResponse = {
      id: 5,
      name: "test",
      isOwner: false,
      isFollower: false,
      posts: data2
    };
    scope.get("/courses/5").reply(200, data1);
    const wrapper = mount(CoursePage, {
      localVue,
      router,
      store,
      propsData: {
        courseId: 5
      }
    });
    await delay(600);
    expect(wrapper.vm.$data.course).toEqual(data1);
    const pList = wrapper.findComponent(PostList);
    expect(pList.exists()).toEqual(true);
    expect(pList.vm.$props.owner).toEqual(false);
    expect(pList.vm.$props.follower).toEqual(false);
    const data3: CourseContentResponse = {
      id: 5,
      name: "test",
      isOwner: false,
      isFollower: true,
      posts: data2
    };
    scope.post("/users/4/followedCourses").reply(200);
    scope.get("/courses/5").reply(200, data3);
    await pList.find(".followButton").trigger("click");
    await delay(600);
    expect(pList.vm.$props.follower).toEqual(true);
    scope.delete("/users/4/followedCourses/5").reply(200);
    scope.get("/courses/5").reply(200, data1);
    await pList.find(".unFollowButton").trigger("click");
    await delay(600);
    expect(pList.vm.$props.follower).toEqual(false);
  });
});
