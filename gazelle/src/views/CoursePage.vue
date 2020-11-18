<template>
  <PostList class="postList" @newPost="newPost" :posts="posts" />
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import PostList from "@/components/PostList.vue";
import { PostResponse } from "@/client/types";
import { getPostsForCourse } from "@/client/post";
@Component({
  components: {
    PostList
  }
})
export default class CoursePage extends Vue {
  @Prop({ type: Number }) private courseId: number;
  private posts: PostResponse[] | null = null;

  async mounted() {
    await this.listUpdate();
  }

  async listUpdate() {
    try {
      this.posts = await getPostsForCourse(this.courseId);
    } catch (e) {
      console.log("Failed to get owned courses:", e.message, e.status);
    }
  }

  private newPost() {
    this.$router.push("/courses/" + this.courseId + "/posts/new");
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
