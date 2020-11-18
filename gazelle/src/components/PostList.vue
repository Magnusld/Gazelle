<template>
  <div class="postList">
    <div class="horizontalSeparator" v-if="posts && posts.length > 0">
      <span class="md-headline">Innlegg</span>
      <div>
        <md-button class="md-icon-button" @click="$emit('newPost')">
          <md-icon>add</md-icon>
        </md-button>
        <md-button class="md-icon-button">
          <md-icon>delete</md-icon>
        </md-button>
      </div>
    </div>
    <div class="posts">
      <PostListing v-for="post in posts" :key="post.id" :post="post" />
    </div>
    <div v-if="posts && posts.length === 0">
      <md-empty-state
        class="centered"
        md-icon="error"
        md-label="Ingen poster å vise"
        md-description="Akkurat nå har løpet ingen poster."
      >
        <md-button class="md-primary md-raised" @click="$emit('newPost')"
          >Opprett post</md-button
        >
      </md-empty-state>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import PostListing from "@/components/PostListing.vue";
import { CourseResponse, PostResponse } from "@/client/types";
@Component({
  components: { PostListing }
})
export default class PostList extends Vue {
  @Prop() private posts!: PostResponse[];

  private deletable = false;
  private postsToDelete: CourseResponse[] = [];

  private async deletePosts() {
    if (this.deletable && this.postsToDelete.length > 0) {
      //for (const post of this.postsToDelete) {
      //await deletePost(post.id);
      //}
      this.postsToDelete = [];
      this.$emit("updated");
    }
    this.deletable = !this.deletable;
  }

  private addPostToDelete(post: {
    postResponse: PostResponse;
    isChecked: boolean;
  }) {
    if (post.isChecked && !this.postsToDelete.includes(post.postResponse)) {
      this.postsToDelete.push(post.postResponse);
    } else {
      this.postsToDelete.splice(
        this.postsToDelete.indexOf(post.postResponse),
        1
      );
    }
  }
}
</script>

<style scoped lang="scss">
.horizontalSeparator {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 13px 10px;
}
.centered {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80vh;
}
</style>
