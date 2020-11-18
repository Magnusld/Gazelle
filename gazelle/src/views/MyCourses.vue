<template>
  <CourseList class="content" :courses="courses" @updated="listUpdate" />
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
    await this.listUpdate();
  }

  async listUpdate() {
    try {
      this.courses = await getOwnedCourses(this.$store.getters.loggedInUserId);
    } catch (e) {
      console.log("Failed to get owned courses:", e.message, e.status);
    }
  }
}
</script>

<style scoped>
</style>
