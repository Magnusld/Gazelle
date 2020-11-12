<template>
  <div class="post">
    <PostEdit v-if="editing" />
    <PostComponent v-else />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import PostComponent from "@/components/PostComponent.vue";
import PostEdit from "../components/PostEdit.vue";

@Component({
  components: {
    PostEdit,
    PostComponent
  }
})
export default class PostView extends Vue {
  private editing = false;
}
</script>
<style>
.post {
  margin: auto;
  max-width: 800px;
  width: 90%;
}
</style>
