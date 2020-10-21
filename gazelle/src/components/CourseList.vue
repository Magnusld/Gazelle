<template>
  <div class="courseList">
    <h1>Mine løp</h1>
    <span id="separator"></span>
    <div class="courses">
      <Course v-for="(course, index) in courses" :key="index" :course="course" />
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import type { Course } from "@/types"

@Options({
  props: {
    courses: [Course]
  }
})
export default class CourseList extends Vue {
  courses!: Course[];
}
</script>

<style scoped>

</style>