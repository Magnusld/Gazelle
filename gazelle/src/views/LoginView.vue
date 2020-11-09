<template>
  <div id="wrapper">
    <img src="../assets/images/orangelogo.svg" alt="Orange logo" id="logo" />
    <LoginPage />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import LoginPage from "../components/LoginPage.vue";

@Component({
  components: {
    LoginPage
  }
})
export default class LoginView extends Vue {}
</script>

<style scoped>
#wrapper {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
#logo {
  position: absolute;
  top: 10%;
}
</style>
