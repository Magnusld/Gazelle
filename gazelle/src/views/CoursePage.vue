<template>
  <PostList
    class="postList"
    v-bind:posts="[
      {
        id: 9,
        name: 'Test Post'
      },
      {
        id: 10,
        name: 'Test Post 2.0'
      },
      {
        id: 11,
        name: 'Test Post 3.0'
      }
    ]"
  />
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import PostList from "@/components/PostList.vue";
import CourseList from "@/components/CourseList.vue";
import CourseListing from "@/components/CourseListing.vue";
import Post from "@/components/PostListing.vue";
@Component({
  components: {
    PostList,
    Post,
    CourseListing,
    CourseList
  }
})
export default class CoursePage extends Vue {
  private id = this.$route.params.id;
}
</script>

<style scoped lang="scss">
.postList {
  margin: auto;
  width: 60%;
}
</style>
