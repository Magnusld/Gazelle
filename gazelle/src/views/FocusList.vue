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
import { Chore } from "@/types";

@Component({
  components: {
    FocusChore
  }
})
export default class FocusList extends Vue {
  private chores: Chore[] = [
    { key: 0, id: 1, name: "Gjøre øving 2", courseName: "TDT4120 - Algdat" },
    { key: 1, id: 1, name: "Gjøre øving 3", courseName: "TDT4120 - Algdat" },
    { key: 2, id: 1, name: "Følge @abakusmemes", courseName: "TDT6969 - Memes" }
  ];
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
  width: 60%;
}
</style>
