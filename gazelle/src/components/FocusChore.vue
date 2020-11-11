<template>
  <div>
    <div class="subheader">{{chore.courseName}}</div>
    <md-checkbox v-model="checked" class="checkbox">{{chore.name}}</md-checkbox>
    <md-divider/>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";

@Component
export default class FocusChore extends Vue {
  @Prop()
  private chore!: { name: string, courseName: string, courseId: number, postName: string, postId: number };
  private checked = false;
}

</script>

<style scoped>

.subheader {
  color: gray;
}
.checkbox {
  --md-theme-default-accent: #F29253
}

</style>