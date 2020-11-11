<template>
  <div>
    <div class="headline">
      <span class="md-headline">{{ post.name }}</span>
    </div>
    <md-divider />
    <vue-markdown class="md-subheading">{{ post.content }}</vue-markdown>
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
import { Post } from "@/types";
import PostChore from "@/components/PostChore.vue";

@Component({
  components: {
    PostChore,
    ChoreListing,
    VueMarkdown
  }
})
export default class PostComponent extends Vue {
  @Prop()
  private post: Post = {
    id: 1,
    name: "Lineær regresjon",
    content: "Litt **deilig** markdown",
    chores: [
      { key: 0, id: 1, name: "Gjøre oppgave 1" },
      { key: 1, id: 2, name: "Lese kapittel 1-5" }
    ]
  };
}
</script>

<style scoped lang="scss">
.headline {
  padding: 15px 10px;
}
</style>
