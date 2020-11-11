<template>
  <div class="chore">
    <md-field>
      <label>Gjøremål</label>
      <md-textarea
        v-model="description"
        md-autogrow
        md-counter="200"
      ></md-textarea>
    </md-field>
    <md-button v-on:click="$emit('remove', chore)" class="md-icon-button">
      <md-icon>clear</md-icon>
    </md-button>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from "vue-property-decorator";
import { Chore } from "@/types";

@Component
export default class ChoreListing extends Vue {
  private description = "";
  @Prop()
  private chore!: Chore;
}
</script>

<style>
.chore {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
