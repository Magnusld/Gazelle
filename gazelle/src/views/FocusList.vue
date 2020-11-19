<template>
  <div class="content">
    <div class="horizontalSeparator">
      <span class="md-headline">Fokuslista</span>
    </div>
    <md-divider />
    <FocusChore v-for="(chore, index) in chores" :key="index" :chore="chore" />
    <md-empty-state
      v-if="chores && chores.length === 0"
      class="centered"
      md-icon="error"
      md-label="Ingen gjøremål å vise"
      md-description="Du har ikke lagt til noen gjøremål på fokuslista."
    >
      <div>
        Hvis du ser et gjøremål du vil legge til side, trykk på
        <md-icon>event_available</md-icon> for å legge det til fokuslista.
      </div>
    </md-empty-state>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import FocusChore from "@/components/FocusChore.vue";
import { ChoreFullResponse } from "@/client/types";
import { getFocusedChores } from "@/client/chore";

@Component({
  components: {
    FocusChore
  }
})
export default class FocusList extends Vue {
  private chores: ChoreFullResponse[] | null = null;

  async mounted() {
    this.chores = await getFocusedChores(this.$store.getters.loggedInUserId);
  }
}
</script>

<style>
.horizontalSeparator {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0 10px 10px;
}
</style>
