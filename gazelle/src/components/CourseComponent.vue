<template>
  <div id="top">
    <h2>{{ course.name }}</h2>
    <span>Neste frist: I dag</span>
  </div>
  <p>Nå: Datastrukturer (4 dager igjen)</p>
  <div id="progress">
    <span>Gjort: 11/16</span>
    <span>På fokuslista: 3</span>
  </div>
  <!-- PROGRESS BAR -->
  <p>Forrige: Kombinatorikk</p>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import type { Course } from "@/types";

@Options({
  props: {
    course: Course
  }
})
export default class CourseComponent extends Vue {
  course!: Course;
}
</script>

<style scoped>

</style>