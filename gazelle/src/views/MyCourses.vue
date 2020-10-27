<template>
  <CourseList v-bind:courses="[{
    id: 5,
    name: 'TNT6969 - Boom'
    },
    {
      id: 6,
      name: 'TNT6970 - Kaboom'
    },
    {
      id: 7,
      name: 'TDT4120 - Algoritmer og datastrukturer'
    },
    {
      id: 8,
      name: 'TDT4120 - Algoritmer og datastrukturer'
    },
    {
      id: 9,
      name: 'TDT4120 - Algoritmer og datastrukturer'
    },
    {
      id: 10,
      name: 'TDT4120 - Algoritmer og datastrukturer'
    }]"/>
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
export default class MyCourses extends Vue {}
</script>
