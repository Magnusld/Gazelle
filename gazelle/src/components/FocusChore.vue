<template>
  <div>
    <md-divider />
    <div class="subheader">LØP</div>
    <md-checkbox v-model="checked" class="checkbox">
      {{ chore.text }}
    </md-checkbox>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { ChoreResponse } from "@/client/types";

@Component
export default class FocusChore extends Vue {
  @Prop()
  private chore!: ChoreResponse;
  private checked = false;
}
</script>

<style scoped>
.subheader {
  color: gray;
  margin-bottom: -1%;
  margin-top: 1%;
}
.checkbox {
  --md-theme-default-accent: #f29253;
}
</style>
