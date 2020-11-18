<template>
  <div v-if="post">
    <div class="horizontalSeparator">
      <span class="md-headline">{{ post.title }}</span>
      <md-button class="md-primary md-raised" :to="`/posts/${id}?edit=true`"
        >Rediger post</md-button
      >
    </div>
    <md-divider />
    <vue-markdown class="md-subheading">{{ post.description }}</vue-markdown>
    <PostChore
      v-for="(chore, index) in post.chores"
      v-bind:key="index"
      v-bind:chore="chore"
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from "vue-property-decorator";
import ChoreListing from "@/components/ChoreListing.vue";
import VueMarkdown from "vue-markdown";
import PostChore from "@/components/PostChore.vue";
import { PostContentResponse } from "@/client/types";
import { getPostContent } from "@/client/post";

@Component({
  components: {
    PostChore,
    ChoreListing,
    VueMarkdown
  }
})
export default class PostComponent extends Vue {
  @Prop() id!: number;
  public post: PostContentResponse | null = null;

  async mounted() {
    this.post = await getPostContent(this.id);
  }
}
</script>

<style scoped lang="scss">
.horizontalSeparator {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
