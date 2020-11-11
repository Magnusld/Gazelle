<template>
  <div>
    <md-divider/>
    <div class="courseListing">
      <div class="header">
        <div class="horizontalSeparator">
          <router-link style="color:black" class="title" :to="'/course/'+course.id">{{ course.name }}</router-link>
          <div>Neste frist: I dag</div>
        </div>
        <div class="subheader">Nå: Datastrukturer (4 dager igjen)</div>
      </div>

      <div class="horizontalSeparator">
        <div>Gjort: 11/16</div>
        <div>På fokuslista: 3</div>
      </div>
      <md-progress-bar
          md-mode="determinate"
          :md-value="68.75"
      ></md-progress-bar>
      <div class="subheader">Forrige: Kombinatorikk</div>
    </div>
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
.horizontalSeparator {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
}
.courseListing {
  width: 100%;
  padding: 10px;
}
.header{
  margin-bottom: 5px;
}
.subheader{
  color: gray;
}
.title{
  font-size: 20px;
  font-weight: 500;
  letter-spacing: .005em;
  line-height: 26px;
  cursor: pointer;
}
</style>
