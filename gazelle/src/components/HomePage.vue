<template>
  <div class="upperSection">
    <img
      src="../assets/images/orangelogo.svg"
      alt="applogo"
      class="upperSectionItem"
    />
    <RecentlyUpdated
      class="upperSectionItem"
      v-bind:courses="[
        {
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
        }
      ]"
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import CourseList from "@/components/CourseList.vue";
import CourseListing from "@/components/CourseListing.vue";
import RecentlyUpdated from "@/components/RecentlyUpdated.vue";
@Component({
  components: {
    CourseList,
    CourseListing,
    RecentlyUpdated
  }
})
export default class HomePage extends Vue {}
</script>

<style scoped lang="scss">
.upperSection {
  display: flex;
  justify-content: space-between;
}

.upperSectionItem {
  width: 50%;
}
</style>
