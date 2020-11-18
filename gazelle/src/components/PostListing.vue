<template>
  <div>
    <md-divider />
    <div class="verticalCenter">
      <md-checkbox v-if="deletable" v-model="checked"> </md-checkbox>
      <div class="postListing">
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
        <div v-if="post.choresCount > 0">
          <div class="horizontalSeparator">
            <div>Gjort: {{ post.choresDone }}/{{ post.choresCount }}</div>
          </div>
          <md-progress-bar
            md-mode="determinate"
            :md-value="(100 * post.choresDone) / post.choresCount"
          ></md-progress-bar>
        </div>
        <div v-else>Ingen gjøremål i {{ post.title }}</div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from "vue-property-decorator";
import { PostResponse } from "@/client/types";

@Component
export default class PostListing extends Vue {
  @Prop()
  private post!: PostResponse;

  @Prop() private deletable!: boolean;
  private checked = false;

  @Watch("checked")
  onCheckedChange() {
    this.$emit("postsToDelete", {
      postResponse: this.post,
      isChecked: this.checked
    });
  }
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
.verticalCenter {
  display: flex;
  align-items: center;
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
.postListing {
  width: 100%;
  padding: 10px;
}
</style>
