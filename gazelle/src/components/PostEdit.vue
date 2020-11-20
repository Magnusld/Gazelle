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
      <md-button class="md-primary addChoreButton" v-on:click="addChore"
        >Legg til gjøremål</md-button
      >
      <md-button
        class="md-raised md-primary sendPostButton"
        v-on:click="sendPost"
        >Lagre</md-button
      >
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from "vue-property-decorator";
import ChoreListing from "@/components/ChoreListing.vue";
import { addNewPost, getPostContent, updateExistingPost } from "@/client/post";
import {
  NewChoreRequest,
  NewPostRequest,
  PostContentResponse
} from "@/client/types";
import { dateOfLocalDate, localDateOfDate } from "@/client/date";

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

  private headline: string = this.new ? "Opprett innlegg" : "Rediger innlegg";
  private nextKey = 0;

  async mounted() {
    if (!this.new) {
      const post: PostContentResponse = await getPostContent(this.id);
      this.title = post.title;
      this.description = post.description;
      this.chores.push(
        ...post.chores.map(chore => ({
          key: chore.key,
          id: chore.id,
          text: chore.text,
          dueDate: null
        }))
      );
      this.startDate = new Date(dateOfLocalDate(post.startDate));
      this.endDate = new Date(dateOfLocalDate(post.endDate));
      this.nextKey =
        post.chores.length > 0
          ? this.chores.reduce(
              (max, p) => (p.key > max ? p.key : max),
              this.chores[0].key
            ) + 1
          : 0;
    }
  }

  private addChore = (): void => {
    this.chores.push({
      key: this.nextKey++,
      id: null,
      text: "",
      dueDate: localDateOfDate(new Date()) //TODO: Må legge til dato
    });
  };

  private removeChore = (chore: NewChoreRequest): void => {
    this.chores.splice(this.chores.indexOf(chore), 1);
  };

  private async sendPost() {
    const newPostRequest: NewPostRequest = {
      title: this.title,
      description: this.description,
      startDate: localDateOfDate(this.startDate),
      endDate: localDateOfDate(this.endDate),
      chores: this.chores
    };
    if (this.new) {
      await addNewPost(this.id, newPostRequest);
      await this.$router.replace("/courses/" + this.id);
    } else {
      await updateExistingPost(this.id, newPostRequest);
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
