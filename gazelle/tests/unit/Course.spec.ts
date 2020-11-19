import { createLocalVue, mount, shallowMount } from "@vue/test-utils";
import VueMaterial from "vue-material";
import CourseListing from "@/components/CourseListing.vue";
import VueRouter from "vue-router";
import CourseList from "@/components/CourseList.vue";
import nock from "nock";
import store from "@/store";
import CoursesView from "@/views/CoursesView.vue";
import { CourseResponse, UserResponse } from "@/client/types";
const localVue = createLocalVue();
localVue.use(VueMaterial);
const router = new VueRouter();
localVue.use(VueRouter);

const baseURL = "http://localhost:8088/";

const scope: nock.Scope = nock(baseURL);

function delay(ms: number) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

describe("CourseListing.vue", () => {
  beforeEach(() => {
    store.commit("logout");
    nock.cleanAll();
  });

  it("renders course name", () => {
    const wrapper = shallowMount(CourseListing, {
      localVue,
      router,
      propsData: {
        course: { name: "Testløp" },
        deletable: false
      }
    });
    expect(wrapper.find(".title").text()).toContain("Testløp");
    expect(wrapper.find("md-checkbox").exists()).toEqual(false);
  });
  it("Teste deletable", async () => {
    const wrapper = mount(CourseList, {
      localVue,
      router,
      propsData: {
        courses: [
          {
            id: 1,
            name: "test",
            isOwner: true,
            isFollower: null,
            currentPost: null,
            nextPost: null,
            previousPost: null
          }
        ]
      },
      data: () => ({
        deletable: true
      })
    });
    expect(wrapper.vm.$props.courses).toEqual([
      {
        id: 1,
        name: "test",
        isOwner: true,
        isFollower: null,
        currentPost: null,
        nextPost: null,
        previousPost: null
      }
    ]);
    expect(wrapper.vm.$data.deletable).toBe(true);
    const cListing = wrapper.findAllComponents(CourseListing).at(0);
    expect(cListing.exists()).toBe(true);
    expect(cListing.vm.$props.deletable).toBe(true);
    const box = cListing.find(".deleteBox");
    expect(box.exists()).toBe(true);
    await cListing.setData({ checked: true });
    expect(cListing.vm.$data.checked).toEqual(true);
    expect(wrapper.vm.$data.coursesToDelete.length).toEqual(1);
    scope.delete("/courses/" + 1).reply(200);
    await wrapper.find(".deleteButton").trigger("click");
    await delay(300);
    expect(wrapper.vm.$data.coursesToDelete.length).toEqual(0);
  });
  it("Tester coursesView", async () => {
    const data: CourseResponse[] = [
      {
        id: 2,
        name: "test",
        isOwner: true,
        isFollower: null,
        currentPost: null,
        nextPost: null,
        previousPost: null
      }
    ];
    scope.get("/users/" + 1 + "/ownedCourses").reply(200, data);
    const user: UserResponse = {
      id: 1,
      firstname: "Ola",
      lastname: "Nordmann"
    };
    store.commit("authSuccess", { user, token: 4567 });
    const wrapper = mount(CoursesView, {
      localVue,
      router,
      store,
      propsData: {
        mode: "myCourses"
      },
      components: {
        CourseList: CourseList
      }
    });
    await delay(300);
    expect(store.getters.token).toEqual(4567);
    const cList = wrapper.findComponent(CourseList);
    expect(cList.exists()).toEqual(true);
    expect(cList.vm.$props.owner).toEqual(true);
    expect(cList.vm.$props.courses).toEqual(data);
  });
});
