<template>
  <div class="separator">
    <md-checkbox v-model="checked" class="checkbox">
      {{ chore.name }}
    </md-checkbox>
    <md-button
      class="md-icon-button"
      :class="{ 'md-primary': isFocused, 'md-raised': isFocused }"
      v-on:click="toggleFocus"
    >
      <md-icon>event_available</md-icon>
      <md-tooltip md-direction="right">
        {{isFocused ? "Fjern fra fokusliste" : "Legg til i fokusliste"}}
      </md-tooltip>
    </md-button>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { Chore } from "@/types";

@Component
export default class FocusChore extends Vue {
  @Prop()
  private chore!: Chore;
  private checked = false;
  private isFocused = false;

  private toggleFocus() {
    this.isFocused = !this.isFocused;
  }
}
</script>

<style scoped>
.separator {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.checkbox {
  --md-theme-default-accent: #f29253;
}
.md-primary {
  --md-theme-default-primary: #f29253;
}
</style>
