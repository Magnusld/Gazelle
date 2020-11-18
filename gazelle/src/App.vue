<template>
  <div class="app">
    <TopBar />
    <router-view />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import TopBar from "@/components/TopBar.vue";
import CourseListing from "@/components/CourseListing.vue";
import CourseList from "@/components/CourseList.vue";
import { tryGetRealUser } from "@/client/login";
import restClient from "@/client/restClient";

@Component({
  components: {
    CourseList,
    TopBar,
    CourseListing
  }
})
export default class App extends Vue {
  created() {
    restClient.setToken(this.$store.getters.token);
    //If we are "fake logged in", make an actual request to the server
    tryGetRealUser();
  }

  @Watch("$store.getters.token")
  onTokenChange(newToken: string | null) {
    restClient.setToken(newToken);
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
.content {
  margin-left: auto;
  margin-right: auto;
  margin-top: 4rem;
  max-width: 800px;
  width: 90%;
}
</style>
