<template>
  <div>
    <div class="headline">
      <span class="md-headline">{{ headline }}</span>
    </div>
    <md-divider />
    <md-field>
      <label>Tittel</label>
      <md-input v-model="title"></md-input>
    </md-field>
    <md-field>
      <label>Beskrivelse</label>
      <md-textarea v-model="description"></md-textarea>
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
import { Component, Vue, Prop } from "vue-property-decorator";
import ChoreListing from "@/components/ChoreListing.vue";
import { addNewPost, updateExistingPost } from "@/client/post";
import { NewChoreRequest } from "@/client/types";
import { localDateOfDate } from "@/client/date";

@Component({
  components: {
    ChoreListing
  }
})
export default class Post extends Vue {
  @Prop({ type: Boolean }) private new!: boolean;
  @Prop({ type: Number }) private id!: number; //is a course id if new is true

  private title = "";
  private description = "";
  private chores: NewChoreRequest[] = [];
  private startDate: Date = new Date();
  private endDate: Date = new Date();

  private headline: string = this.new ? "Opprett post" : "Rediger post";
  private nextKey = 0;

  private addChore = (): void => {
    this.chores.push({
      key: this.nextKey++,
      id: 0,
      text: "",
      dueDate: localDateOfDate(new Date()) //Må legge til dato
    });
  };

  private removeChore = (chore: NewChoreRequest): void => {
    this.chores.splice(this.chores.indexOf(chore), 1);
  };

  private async sendPost() {
    if (this.new) {
      await addNewPost(this.id, {
        title: this.title,
        description: this.description,
        startDate: localDateOfDate(new Date(this.startDate)),
        endDate: localDateOfDate(new Date(this.endDate)),
        chores: this.chores
      });
      await this.$router.replace("/courses/" + this.id);
    } else {
      await updateExistingPost(this.id, {
        title: this.title,
        description: this.description,
        startDate: localDateOfDate(new Date(this.startDate)),
        endDate: localDateOfDate(new Date(this.endDate)),
        chores: this.chores
      });
      await this.$router.replace(`/posts/${this.id}`);
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
