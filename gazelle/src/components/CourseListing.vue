<template>
  <div class="courseListing">
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

<style scoped lang="scss"></style>
