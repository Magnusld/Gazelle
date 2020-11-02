<template>
  <div>
    <img src="../assets/images/orangelogo.svg" alt="applogo"/>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import CourseList from "@/components/CourseList.vue";
import CourseListing from "@/components/CourseListing.vue";

@Component({
  components: {
    CourseList,
    CourseListing
  }
})
export default class Home extends Vue {}
</script>
