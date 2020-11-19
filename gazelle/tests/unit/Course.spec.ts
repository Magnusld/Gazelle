import { createLocalVue, mount, shallowMount } from "@vue/test-utils";
import VueMaterial from "vue-material";
import CourseListing from "@/components/CourseListing.vue";
import VueRouter from "vue-router";
import CourseList from "@/components/CourseList.vue";

const localVue = createLocalVue();
localVue.use(VueMaterial);
const router = new VueRouter();
localVue.use(VueRouter);

describe("CourseListing.vue", () => {
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
  it("Teste deletable", () => {
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
      }
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
  });
});
