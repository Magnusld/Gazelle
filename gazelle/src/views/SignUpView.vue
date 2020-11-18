<template>
  <div id="wrapper">
    <img src="../assets/images/orangelogo.svg" alt="Orange logo" id="logo" />
    <SignUpPage class="signup"/>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import LoginPage from "../components/LoginPage.vue";
import SignUpPage from "@/components/SignUpPage.vue";

@Component({
  components: {
    SignUpPage,
    LoginPage
  }
})
export default class SignUpView extends Vue {}
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
  position: relative;
  top: 5%;
}
.signup {
  position: relative;
}
</style>
