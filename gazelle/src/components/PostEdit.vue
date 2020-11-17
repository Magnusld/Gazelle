<template>
  <div>
    <div class="headline">
      <span class="md-headline">{{ headline }}</span>
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
      <md-button class="md-primary" v-on:click="addChore"
        >Legg til gjøremål</md-button
      >
      <md-button class="md-raised md-primary" v-on:click="sendPost"
        >Lagre</md-button
      >
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import ChoreListing from "@/components/ChoreListing.vue";
import { addNewPost, updateExistingPost } from "@/client/post";
import { NewChoreRequest } from "@/client/types";

@Component({
  components: {
    ChoreListing
  }
})
export default class Post extends Vue {
  private newPost = true;
  private tittel = "";
  private beskrivelse = "";
  private chores: NewChoreRequest[] = [];
  private startDate: Date = new Date();
  private endDate: Date = new Date();

  private headline: string = this.newPost ? "Opprett post" : "Rediger post";
  private nextKey = 0;

  private addChore = (): void => {
    this.chores.push({
      key: this.nextKey++,
      id: 0,
      text: "",
      dueDate: new Date()
    });
  };

  private removeChore = (chore: NewChoreRequest): void => {
    this.chores.splice(this.chores.indexOf(chore), 1);
  };

  private sendPost() {
    if (this.newPost) {
      addNewPost(1, {
        title: this.tittel,
        description: this.beskrivelse,
        startDate: new Date(this.startDate),
        endDate: new Date(this.endDate),
        chores: this.chores
      });
    } else {
      updateExistingPost(1, {
        title: this.tittel,
        description: this.beskrivelse,
        startDate: new Date(this.startDate),
        endDate: new Date(this.endDate),
        chores: this.chores
      });
    }
  }
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
