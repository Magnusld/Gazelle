<template>
  <div class="focusList">
    <div class="horizontalSeparator">
      <span class="md-headline">Fokuslista</span>
      <md-button class="md-icon-button">
        <md-icon>delete</md-icon>
      </md-button>
    </div>
    <FocusChore v-for="(chore, index) in chores" :key="index" :chore="chore" />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import FocusChore from "@/components/FocusChore.vue";
import { ChoreResponse } from "@/client/types";

@Component({
  components: {
    FocusChore
  }
})
export default class FocusList extends Vue {
  private chores: ChoreResponse[] = [];
}
</script>

<style>
.horizontalSeparator {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0 10px 10px;
}
.focusList {
  margin: auto;
  max-width: 800px;
  width: 90%;
}
</style>
