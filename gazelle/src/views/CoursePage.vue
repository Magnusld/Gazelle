<template>
  <PostList class="postList" v-bind:posts="this.posts" />
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import PostList from "@/components/PostList.vue";
import CourseList from "@/components/CourseList.vue";
import CourseListing from "@/components/CourseListing.vue";
import Post from "@/components/PostListing.vue";
import { PostResponse } from "@/client/types";
import { getPostsForCourse } from "@/client/post";
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
  private posts: PostResponse[] = [];

  async mounted() {
    await this.listUpdate();
  }

  async listUpdate() {
    try {
      this.posts = await getPostsForCourse(+this.id);
    } catch (e) {
      console.log("Failed to get owned courses:", e.message, e.status);
    }
  }
}
</script>

<style scoped lang="scss">
.postList {
  margin: auto;
  max-width: 800px;
  width: 90%;
}
</style>
