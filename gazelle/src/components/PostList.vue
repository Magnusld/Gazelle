<template>
  <div class="postList">
    <div class="horizontalSeparator">
      <div class="md-headline">{{ title }}</div>
      <div>
        <div v-if="owner" class="centeredButton addNewPostButton">
          <md-button class="md-icon-button" @click="$emit('newPost')">
            <md-icon>add</md-icon>
          </md-button>
          <span v-if="deletable"
            >Slett {{ postsToDelete.length }} post{{
              postsToDelete.length === 1 ? "" : "er"
            }}:
          </span>
          <md-button
            class="md-icon-button deletePostButton"
            @click="deletePosts"
          >
            <md-icon>delete</md-icon>
          </md-button>
        </div>

        <div v-else>
          <md-button
            v-if="follower"
            class="md-accent md-raised centeredButton unFollowButton"
            @click="$emit('unfollow')"
          >
            Fulgt
          </md-button>
          <md-button
            v-else
            class="md-primary md-raised centeredButton followButton"
            @click="$emit('setFollow')"
          >
            Følg
          </md-button>
        </div>
      </div>
    </div>
    <div class="posts" v-if="posts && posts.length > 0">
      <PostListing
        v-for="post in posts"
        :key="post.id"
        :post="post"
        @postsToDelete="addPostToDelete"
        :deletable="deletable"
      />
    </div>
    <div v-else>
      <md-empty-state
        class="centered"
        md-icon="error"
        md-label="Ingen innlegg å vise"
        md-description="Akkurat nå har løpet ingen innlegg."
      >
        <md-button
          v-if="owner"
          class="md-primary md-raised"
          @click="$emit('newPost')"
          >Opprett innlegg</md-button
        >
        <span v-else>Kom tilbake senere</span>
      </md-empty-state>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import PostListing from "@/components/PostListing.vue";
import { PostResponse } from "@/client/types";
import { deletePost } from "@/client/post";
@Component({
  components: { PostListing }
})
export default class PostList extends Vue {
  @Prop({ default: "Innlegg" })
  private title!: string;
  @Prop({ default: false })
  private owner!: boolean;
  @Prop({ default: false })
  private follower!: boolean;
  @Prop() private posts!: PostResponse[];

  private deletable = false;
  private postsToDelete: PostResponse[] = [];

  private async deletePosts() {
    if (this.deletable && this.postsToDelete.length > 0) {
      for (const post of this.postsToDelete) {
        await deletePost(post.id);
      }
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
  padding: 10px 0 10px 10px;
}
.centered {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80vh;
}
.md-accent {
  --md-theme-default-accent: #f29253;
}
</style>
