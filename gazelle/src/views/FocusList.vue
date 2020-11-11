<template>
  <div class="focusList">
    <div class="horizontalSeparator">
      <span class="md-headline">Fokuslista</span>
      <md-button class="md-icon-button">
        <md-icon>delete</md-icon>
      </md-button>
    </div>
    <FocusChore>
      v-for="(course, index) in courses"
      :key="index"
      :course="course"
    </FocusChore>
    <md-divider />
  </div>
</template>

<script lang="ts">
import {Component, Prop, Vue} from "vue-property-decorator";
import FocusChore from "@/components/FocusChore.vue";
import {Chore} from "@/types";

@Component({
  components: {
    FocusChore
  }
})
export default class FocusList extends Vue {
  @Prop()
  private chores!: Chore[];
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
