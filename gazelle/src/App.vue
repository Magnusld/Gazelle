<template>
  <div class="app">
    <TopBar />
    <router-view />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import TopBar from "@/components/TopBar.vue";
import CourseListing from "@/components/CourseListing.vue";
import CourseList from "@/components/CourseList.vue";
import { tryGetRealUser } from "@/client/login";

@Component({
  components: {
    CourseList,
    TopBar,
    CourseListing
  }
})
export default class App extends Vue {
  mounted() {
    console.log("Trying to get real user", this.$store.getters.authStatus);
    //If we are "fake logged in", make an actual request to the server
    tryGetRealUser();
  }
}
</script>

<style lang="scss">
@import url("//fonts.googleapis.com/css?family=Roboto:400,500,700,400italic|Material+Icons");

html,
body {
  margin: 0;
  padding: 0;
}
#app {
  width: 100%;
  min-height: 100vh;
  position: absolute;

  display: flex;
  align-items: stretch;
  flex-direction: column;
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
