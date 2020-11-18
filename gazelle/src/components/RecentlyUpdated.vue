<template>
  <div class="md-scrollbar recentlyUpdated">
    <div class="horizontalSeparator">
      <span class="md-headline">Nylig oppdatert</span>
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
import CourseListing from "./CourseListing.vue";
import { CourseResponse } from "@/client/types";
@Component({
  components: { CourseListing }
})
export default class RecentlyUpdated extends Vue {
  @Prop()
  private courses!: CourseResponse[];
}
</script>

<style scoped lang="scss">
.horizontalSeparator {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0 10px 10px;
}
.recentlyUpdated {
  margin: auto;
  width: 60%;
  max-height: 450px;
  overflow: auto;
}
.courses {
  width: 75%;
  align-content: center;
}
</style>
