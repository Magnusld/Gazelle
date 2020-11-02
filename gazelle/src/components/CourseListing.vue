<template>
  <md-card class="courseListing" md-with-hover>
    <md-ripple>
      <md-card-header>
        <md-card-header-text>
          <div class="spacing">
            <div class="md-title">{{ course.name }}</div>
            <span>Neste frist: I dag</span>
          </div>
          <div class="md-subhead">Nå: Datastrukturer (4 dager igjen)</div>
        </md-card-header-text>
      </md-card-header>
      <md-card-content>
        <div class="spacing">
          <span>Gjort: 11/16</span>
          <span>På fokuslista: 3</span>
        </div>
        <md-progress-bar
          md-mode="determinate"
          :md-value="68.75"
        ></md-progress-bar>
        <p>Forrige: Kombinatorikk</p>
      </md-card-content>
    </md-ripple>
  </md-card>
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
.spacing {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
}
.courseListing {
  width: 100%;
  margin: 10px 0;
}
</style>
