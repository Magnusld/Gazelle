<template>
  <div class="postList">
    <div class="horizontalSeparator">
      <span class="md-headline">Nye Poster:</span>
    </div>
    <div class="posts">
      <PostListing v-for="(post, index) in posts" :key="index" :post="post" />
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
  @Prop()
  private posts!: Post[];
}
</script>

<style scoped lang="scss">
.horizontalSeparator {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0 10px 10px;
}
</style>
