<template>
  <div>
    <md-divider v-if="!compact"/>
    <div class="subheader" v-if="!compact">{{chore.courseName}}</div>
    <md-checkbox v-model="checked" class="checkbox">
      {{chore.name}}
    </md-checkbox>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import {Chore} from "@/types";

@Component
export default class FocusChore extends Vue {
  @Prop()
  private chore!: Chore;
  private checked = false;
  @Prop()
  private compact!: boolean;
}

</script>

<style scoped>

.subheader {
  color: gray;
  margin-bottom: -1%;
  margin-top: 1%;
}
.checkbox {
  --md-theme-default-accent: #F29253
}

</style>