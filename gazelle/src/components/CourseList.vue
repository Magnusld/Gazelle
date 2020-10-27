<template>
  <div class="courseList">
    <div class="horizontalSeparator">
      <span class="md-headline">Mine løp</span>
      <md-button class="md-icon-button">
        <md-icon>delete</md-icon>
      </md-button>
    </div>
    <md-divider></md-divider>
    <md-content class="courses">
      <CourseListing
        v-for="(course, index) in courses"
        :key="index"
        :course="course"
      />
    </md-content>
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
.horizontalSeparator{
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0 10px 10px;
}
.courseList{
  margin: auto;
  width: 60%;
}
</style>
