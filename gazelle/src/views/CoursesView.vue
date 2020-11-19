<template>
  <CourseList
    class="content"
    :owner="mode === 'myCourses'"
    :courses="courses"
    @updated="listUpdate"
  />
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from "vue-property-decorator";
import CourseList from "@/components/CourseList.vue";
import CourseListing from "@/components/CourseListing.vue";
import { getFollowedCourses, getOwnedCourses } from "@/client/course";
import { CourseResponse } from "@/client/types";

@Component({
  components: {
    CourseList,
    CourseListing
  }
})
export default class CoursesView extends Vue {
  @Prop() private mode!: "myCourses" | "followedCourses";
  private courses: CourseResponse[] | null = null;

  async mounted() {
    await this.listUpdate();
  }

  @Watch("mode")
  async listUpdate() {
    try {
      if (this.mode === "myCourses") {
        this.courses = await getOwnedCourses(
          this.$store.getters.loggedInUserId
        );
      } else {
        this.courses = await getFollowedCourses(
          this.$store.getters.loggedInUserId
        );
      }
    } catch (e) {
      console.log("Failed to get owned courses:", e.message, e.status);
    }
  }
}
</script>

<style scoped></style>
