<template>
  <PostList
    class="content"
    v-if="course"
    @newPost="newPost"
    @updated="listUpdate"
    @setFollow="setFollow"
    @unfollow="unfollow"
    :posts="course.posts"
    :title="course.name"
    :owner="course.isOwner"
    :follower="course.isFollower"
  />
</template>

<script lang="ts">
import { Component, Prop, Watch, Vue } from "vue-property-decorator";
import PostList from "@/components/PostList.vue";
import { CourseContentResponse } from "@/client/types";
import {
  addCourseFollower,
  findCourseById,
  removeCourseFollower
} from "@/client/course";
@Component({
  components: {
    PostList
  }
})
export default class CoursePage extends Vue {
  @Prop({ type: Number }) private courseId!: number;
  private course: CourseContentResponse | null = null;

  async mounted() {
    await this.listUpdate();
  }

  @Watch("courseId")
  async listUpdate() {
    try {
      this.course = await findCourseById(this.courseId);
    } catch (e) {
      //TODO: Proper try/catch
      console.log("Failed to get course:", e.message, e.status);
    }
  }

  private newPost() {
    this.$router.push("/courses/" + this.courseId + "/posts/new");
  }

  private async setFollow() {
    await addCourseFollower(+this.courseId, this.$store.getters.loggedInUserId);
    await this.listUpdate();
  }

  private async unfollow() {
    await removeCourseFollower(
      +this.courseId,
      this.$store.getters.loggedInUserId
    );
    await this.listUpdate();
  }
}
</script>

<style scoped></style>
