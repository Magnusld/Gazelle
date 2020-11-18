<template>
  <div class="content">
    <PostComponent v-if="viewing" :id="id" />
    <PostEdit v-else :new="isNewPost" :id="id" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from "vue-property-decorator";
import PostComponent from "@/components/PostComponent.vue";
import PostEdit from "../components/PostEdit.vue";

@Component({
  components: {
    PostEdit,
    PostComponent
  }
})
export default class PostView extends Vue {
  @Prop({ type: String }) mode!: "edit" | "new" | "view";
  @Prop({ type: Number }) id!: number | null; //is a course id if mode=="new"

  get viewing() {
    return this.mode === "view";
  }
  get isNewPost() {
    return this.mode === "new";
  }
}
</script>
<style scoped>
</style>
