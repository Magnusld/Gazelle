<template>
  <CourseList class="courseList" v-bind:courses="this.courses" />
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import CourseList from "@/components/CourseList.vue";
import CourseListing from "@/components/CourseListing.vue";
import { getOwnedCourses } from "@/client/course";
import { CourseResponse } from "@/client/types";

@Component({
  components: {
    CourseList,
    CourseListing
  }
})
export default class MyCourses extends Vue {
  private courses: CourseResponse[] = [];

  async mounted() {
    if (this.$store.getters.isLoggedIn) {
      this.courses = await getOwnedCourses(this.$store.getters.loggedInUser.id);
    }
  }
}
</script>

<style>
.courseList {
  margin: auto;
  max-width: 800px;
  width: 90%;
}
</style>
