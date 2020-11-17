<template>
  <div class="postList">
    <div class="horizontalSeparator" v-if="posts.length > 0">
      <span class="md-headline">Mine løp</span>
      <div>
        <md-button class="md-icon-button">
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
    <div v-if="this.posts.length === 0">
      <md-empty-state
        class="centered"
        md-icon="error"
        md-label="Ingen poster å vise"
        md-description="Akkurat nå har løpet ingen poster."
      >
        <md-button class="md-primary md-raised" :to="'/courses/' + courseId + '/posts/newPost'"
          >Opprett post</md-button
        >
      </md-empty-state>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import PostListing from "@/components/PostListing.vue";
import { Post } from "@/types";
@Component({
  components: { PostListing }
})
export default class PostList extends Vue {
  @Prop() private posts!: Post[];
  private courseId = this.$route.params.id;
}
</script>

<style scoped lang="scss">
.horizontalSeparator {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0;
}
.centered {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80vh;
}
</style>
