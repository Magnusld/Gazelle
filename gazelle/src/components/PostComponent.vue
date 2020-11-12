<template>
  <div>
    <div class="headline">
      <span class="md-headline">{{ post.name }}</span>
    </div>
    <md-divider />
    <vue-markdown class="md-subheading">{{ post.content }}</vue-markdown>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from "vue-property-decorator";
import ChoreListing from "@/components/ChoreListing.vue";
import VueMarkdown from "vue-markdown";
import { Post } from "@/types";

@Component({
  components: {
    ChoreListing,
    VueMarkdown
  }
})
export default class PostComponent extends Vue {
  //@Prop()
  //private post!: Post;
  @Prop()
  private post: Post = {
    id: 1,
    name: "Lineær regresjon",
    content: "i am a ~~tast~~ **test**.",
    chores: [{ key: 0, id: 1 }]
  };
}
</script>

<style scoped lang="scss">
.headline {
  padding: 10px;
}
</style>
