<template>
  <div>
    <md-divider />
    <div class="courseListing">
      <div class="header">
        <div class="horizontalSeparator">
          <router-link
            :to="`/posts/${post.id}`"
            style="color:black"
            class="title"
            >{{ post.title }}</router-link
          >
        </div>
      </div>
      <div class="md-body-1">
        {{ post.description }}
      </div>
      <div class="horizontalSeparator">
        <div>Gjort: 3/5</div>
      </div>
      <md-progress-bar
        md-mode="determinate"
        :md-value="(100 * 3) / 5"
      ></md-progress-bar>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { PostResponse } from "@/client/types";

@Component
export default class PostListing extends Vue {
  @Prop()
  private post!: PostResponse;
}
</script>

<style scoped lang="scss">
.horizontalSeparator {
  display: flex;
  align-content: center;
  align-items: flex-end;
  justify-content: space-between;
  padding: 0;
}
.menu {
  display: flex;
  align-content: center;
  align-items: baseline;
}
.title {
  font-size: 20px;
  font-weight: 500;
  letter-spacing: 0.005em;
  line-height: 26px;
  cursor: pointer;
}
</style>
