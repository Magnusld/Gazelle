import {createLocalVue, shallowMount} from "@vue/test-utils";
import VueMaterial from "vue-material";
import CourseListing from "@/components/CourseListing.vue";

const localVue = createLocalVue();
localVue.use(VueMaterial);

describe("CourseListing.vue", () => {
  it("renders course name", () => {
    const wrapper = shallowMount(CourseListing, {
      localVue,
      propsData: { course: {name: "Testløp"} }
    });
    expect(wrapper.find(".title").html()).toContain("Testløp</a>")
  });
});
