<template>
  <div>
    <div class="headline">
      <span class="md-headline">{{ post.title }}</span>
    </div>
    <md-divider />
    <vue-markdown class="md-subheading">{{ post.description }}</vue-markdown>
    <PostChore
      v-for="(chore, index) in post.chores"
      v-bind:key="index"
      v-bind:chore="chore"
      compact="true"
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from "vue-property-decorator";
import ChoreListing from "@/components/ChoreListing.vue";
import VueMarkdown from "vue-markdown";
import PostChore from "@/components/PostChore.vue";
import {PostContentResponse} from "@/client/types";

@Component({
  components: {
    PostChore,
    ChoreListing,
    VueMarkdown
  }
})
export default class PostComponent extends Vue {
  @Prop() private post!: PostContentResponse;
}
</script>

<style scoped lang="scss">
.headline {
  padding: 15px 10px;
}
</style>
