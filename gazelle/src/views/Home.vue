<template>
  <HomePage/>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import CourseList from "@/components/CourseList.vue";
import CourseListing from "@/components/CourseListing.vue";
import RecentlyUpdated from "@/components/RecentlyUpdated.vue";
import HomePage from "@/components/HomePage.vue";

@Component({
  components: {
    CourseList,
    CourseListing,
    RecentlyUpdated,
    HomePage
  }
})
export default class Home extends Vue {}
</script>
