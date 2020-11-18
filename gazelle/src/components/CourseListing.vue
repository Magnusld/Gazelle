<template>
  <div>
    <md-divider />
    <div class="verticalCenter">
      <md-checkbox v-if="deletable" v-model="checked"> </md-checkbox>
      <div class="courseListing">
        <div class="header">
          <div class="horizontalSeparator">
            <router-link
              style="color:black"
              class="title"
              :to="'/courses/' + course.id"
              >{{ course.name }}</router-link
            >
            <div>Neste frist: I dag</div>
          </div>
          <div v-if="course.currentPost != null" class="subheader">
            Nå: {{ course.currentPost.title }} (Antall dager igjen)
          </div>
        </div>

        <div v-if="course.currentPost != null">
          <div class="horizontalSeparator">
            <div>
              Gjort: {{ course.currentPost.choresDone }}/{{
                course.currentPost.choresCount
              }}
            </div>
            <div>På fokuslista: {{ course.currentPost.choresFocused }}</div>
          </div>
          <md-progress-bar
            md-mode="determinate"
            :md-value="
              (100 * course.currentPost.choresDone) /
                course.currentPost.choresCount
            "
          ></md-progress-bar>
          <div v-if="course.previousPost" class="subheader">
            Forrige: {{ course.previousPost.title }}
          </div>
        </div>

        <div v-if="course.currentPost === null">
          Ingen poster å vise
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from "vue-property-decorator";
import { CourseResponse } from "@/client/types";

@Component
export default class CourseListing extends Vue {
  @Prop() private course!: CourseResponse;
  @Prop() private deletable!: boolean;
  private checked = false;

  @Watch("checked")
  onCheckedChange() {
    this.$emit("coursesToDelete", {
      courseResponse: this.course,
      isChecked: this.checked
    });
  }
}
</script>

<style scoped lang="scss">
.verticalCenter {
  display: flex;
  align-items: center;
}
.courseListing {
  width: 100%;
  padding: 10px;
}
.subheader {
  color: gray;
}
.horizontalSeparator {
  padding: 0;
}
.title {
  color: black;
  font-size: 20px;
  font-weight: 500;
  letter-spacing: 0.005em;
  line-height: 26px;
  cursor: pointer;
}
</style>
