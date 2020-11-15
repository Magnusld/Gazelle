<template>
  <div>
    <div class="horizontalSeparator" v-if="courses.length > 0">
      <span class="md-headline">Mine løp</span>
      <div>
        <md-button class="md-icon-button" v-on:click="showDialog = true">
          <md-icon>add</md-icon>
        </md-button>
        <md-button class="md-icon-button">
          <md-icon>delete</md-icon>
        </md-button>
      </div>
    </div>
    <div class="courses">
      <CourseListing
        v-for="(course, index) in courses"
        :key="index"
        :course="course"
      />
    </div>
    <div v-if="this.courses.length == 0">
      <md-empty-state
        class="centered"
        md-icon="error"
        md-label="Ingen løp å vise"
        md-description="Akkurat nå eier du ingen løp."
      >
        <md-button
          class="md-primary md-raised"
          v-on:click="showDialog = true"
          >Lag løp</md-button
        >
      </md-empty-state>
    </div>
    <md-dialog :md-active.sync="showDialog">
      <md-field>
        <label>Navn på løp</label>
        <md-input v-model="name"></md-input>
      </md-field>
      <md-button class="md-primary md-raised" v-on:click="addCourse"
        >Lag løp</md-button
      >
    </md-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import CourseListing from "./CourseListing.vue";
import { CourseResponse } from "@/client/types";
import { addNewCourse } from "@/client/course";

@Component({
  components: { CourseListing }
})
export default class CourseList extends Vue {
  @Prop() private courses!: CourseResponse[];

  private showDialog = false;
  private name = "";

  private async addCourse() {
    this.showDialog = false;
    await addNewCourse({ name: this.name });
  }
}
</script>

<style scoped lang="scss">
.centered {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80vh;
}
.horizontalSeparator {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0 10px 10px;
}
</style>
