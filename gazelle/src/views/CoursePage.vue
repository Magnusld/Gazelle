<template>
  <PostList class="content"
            v-if="course"
            @newPost="newPost"
            @updated="listUpdate"
            :posts="course.posts"
            :title="course.name"
            :owner="course.isOwner"
            :follower="course.isFollower"
  />
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import PostList from "@/components/PostList.vue";
import {CourseContentResponse, PostResponse} from "@/client/types";
import { findCourseById } from "@/client/course";
@Component({
  components: {
    PostList
  }
})
export default class CoursePage extends Vue {
  @Prop({ type: Number }) private courseId!: number;
  private course : CourseContentResponse | null = null;

  async mounted() {
    await this.listUpdate();
  }

  async listUpdate() {
    try {
      this.course = await findCourseById(this.courseId);
      console.log(this.course);
    } catch (e) {
      //TODO: Proper try/catch
      console.log("Failed to get course:", e.message, e.status);
    }
  }

  private newPost() {
    this.$router.push("/courses/" + this.courseId + "/posts/new");
  }
}
</script>

<style scoped>
</style>
