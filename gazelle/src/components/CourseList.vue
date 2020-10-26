<template>
  <div class="courseList">
    <h1>Mine løp</h1>
    <span id="separator"></span>
    <div class="courses">
      <CourseListing v-for="(course, index) in courses" :key="index" :course="course" />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import type { Course } from "@/types";
import CourseListing from "./CourseListing.vue";

@Component({
  components: { CourseListing }
})
export default class CourseList extends Vue {
  @Prop()
  private courses!: Course[];
}
</script>

<style scoped lang="scss">

</style>