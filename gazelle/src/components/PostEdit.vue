<template>
  <div>
    <div class="headline">
      <span class="md-headline">{{ this.headline }}</span>
    </div>
    <md-divider />
    <md-field>
      <label>Tittel</label>
      <md-input v-model="tittel"></md-input>
    </md-field>
    <md-field>
      <label>Beskrivelse</label>
      <md-textarea v-model="beskrivelse"></md-textarea>
    </md-field>
    <div class="horizontalSeparator">
      <md-datepicker v-model="startDate" class="date">
        <label>Startdato</label>
      </md-datepicker>
      <md-datepicker v-model="endDate" class="date">
        <label>Sluttdato</label>
      </md-datepicker>
    </div>
    <ChoreListing
      v-for="chore in chores"
      v-bind:key="chore.key"
      v-bind:chore="chore"
      v-on:remove="removeChore"
    />
    <div class="horizontalSeparator">
      <md-button class="md-primary" v-on:click="this.addChore"
        >Legg til gjøremål</md-button
      >
      <md-button class="md-raised md-primary">Lagre</md-button>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import ChoreListing from "@/components/ChoreListing.vue";
import { Chore } from "@/types";

@Component({
  components: {
    ChoreListing
  }
})
export default class Post extends Vue {
  private newPost = false;
  private tittel = "";
  private beskrivelse = "";
  private chores: Chore[] = [];
  private startDate: Date | number | string | null = null;
  private endDate: Date | number | string | null = null;

  private headline: string = this.newPost ? "Opprett post" : "Rediger post";
  private nextKey = 0;

  private addChore = (): void => {
    this.chores.push({ key: this.nextKey++, id: null });
  };

  private removeChore = (chore: Chore): void => {
    this.chores.splice(this.chores.indexOf(chore), 1);
  };
}
</script>

<style scoped lang="scss">
.horizontalSeparator {
  display: flex;
  justify-content: space-between;
}
.date {
  width: 48%;
}
.headline {
  padding: 15px 10px;
}
</style>
