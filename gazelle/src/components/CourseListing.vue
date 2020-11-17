<template>
  <div>
    <md-divider />
    <div class="verticalCenter">
      <md-checkbox v-if="deletable" v-model="checked" class="checkbox">
      </md-checkbox>
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
          <div v-if="course.currentPost != undefined" class="subheader">
            Nå: {{ course.currentPost.title }} (Antall dager igjen)
          </div>
        </div>

        <div v-if="course.currentPost != undefined">
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
              (course.currentPost.choresCount / course.currentPost.choresDone -
                1) *
                100
            "
          ></md-progress-bar>
          <div class="subheader">Forrige: Kombinatorikk</div>
        </div>

        <div v-if="course.currentPost === undefined">
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
