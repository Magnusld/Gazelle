<template>
  <div class="courseListing">
    <h1>Course</h1>
    <span id="separator"></span>
    name: {{ course.name }}
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import type { Course } from "@/types";

@Component
export default class CourseListing extends Vue {
  @Prop()
  private course!: Course;
}
</script>

<style scoped lang="scss">

</style>