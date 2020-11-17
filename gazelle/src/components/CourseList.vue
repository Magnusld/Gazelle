<template>
  <div>
    <div class="horizontalSeparator" v-if="courses.length > 0">
      <span class="md-headline">Mine løp</span>
      <div>
        <md-button class="md-icon-button" @click="showNewCourseDialog">
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
    <div v-if="this.courses.length === 0">
      <md-empty-state
        class="centered"
        md-icon="error"
        md-label="Ingen løp å vise"
        md-description="Akkurat nå eier du ingen løp."
      >
        <md-button class="md-primary md-raised" @click="showNewCourseDialog"
          >Lag løp</md-button
        >
      </md-empty-state>
    </div>
    <md-dialog :md-active.sync="showDialog">
      <md-field>
        <label>Navn på løp</label>
        <md-input v-model="name"></md-input>
      </md-field>
      <div class="errorMessage" v-if="error">{{ error }}</div>
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
  private error = "";
  private name = "";

  private showNewCourseDialog() {
    this.error = "";
    this.name = "";
    this.showDialog = true;
  }

  private async addCourse() {
    try {
      await addNewCourse({ name: this.name });
      this.showDialog = false;
      this.$emit("updated");
    } catch (e) {
      this.error = e.message;
    }
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
.errorMessage {
  text-align: center;
  color: red;
  font-size: 1.2rem;
}
</style>
