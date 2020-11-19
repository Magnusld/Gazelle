<template>
  <div>
    <div class="horizontalSeparator" v-if="courses && courses.length > 0">
      <span class="md-headline">{{ owner ? "Mine løp" : "Fulgte løp" }}</span>
      <div>
        <md-button class="md-icon-button" @click="showNewCourseDialog">
          <md-icon>add</md-icon>
        </md-button>
        <md-button class="md-icon-button" @click="deleteCourses">
          <md-icon>delete</md-icon>
        </md-button>
      </div>
    </div>
    <div class="courses">
      <CourseListing
        v-for="course in courses"
        :key="course.id"
        :course="course"
        :deletable="deletable"
        @coursesToDelete="addCourseToDelete"
      />
    </div>
    <div v-if="courses && courses.length === 0">
      <md-empty-state
        class="centered"
        md-icon="error"
        md-label="Ingen løp å vise"
        :md-description="
          `Akkurat nå ${owner ? 'eier' : 'følger'} du ingen løp.`
        "
      >
        <md-button
          v-if="owner"
          class="md-primary md-raised"
          @click="showNewCourseDialog"
          >Lag løp</md-button
        >
        <span v-else>Følg noen løp for å vise dem her</span>
      </md-empty-state>
    </div>
    <md-dialog :md-active.sync="showDialog">
      <md-field class="input">
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
import { addNewCourse, deleteCourse } from "@/client/course";

@Component({
  components: { CourseListing }
})
export default class CourseList extends Vue {
  @Prop({ default: null }) private courses!: CourseResponse[];
  @Prop({ default: false }) private owner!: boolean;

  private showDialog = false;
  private error = "";
  private name = "";
  private deletable = false;
  private coursesToDelete: CourseResponse[] = [];

  private async deleteCourses() {
    if (this.deletable && this.coursesToDelete.length > 0) {
      for (const course of this.coursesToDelete) {
        await deleteCourse(course.id);
      }
      this.coursesToDelete = [];
      this.$emit("updated");
    }
    this.deletable = !this.deletable;
  }

  private addCourseToDelete(course: {
    courseResponse: CourseResponse;
    isChecked: boolean;
  }) {
    if (
      course.isChecked &&
      !this.coursesToDelete.includes(course.courseResponse)
    ) {
      this.coursesToDelete.push(course.courseResponse);
    } else {
      this.coursesToDelete.splice(
        this.coursesToDelete.indexOf(course.courseResponse),
        1
      );
    }
  }

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
.input {
  width: 95%;
  margin: auto;
}
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
