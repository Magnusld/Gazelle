<template>
  <TopBar />
  <router-view />
</template>

<script>
import { Options, Vue } from "vue-class-component";
import TopBar from "@/components/TopBar.vue";
@Options({
  components: { TopBar }
})
export default class App extends Vue {}
</script>

<style lang="scss">
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
