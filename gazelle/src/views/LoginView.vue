<template>
  <div>
    <mq-layout :mq="['mobile', 'tablet']" id="vertical">
      <img
        src="../assets/images/orangelogo.svg"
        alt="Orange logo"
        class="logo logoVertical"
      />
      <LoginPage />
    </mq-layout>
    <mq-layout mq="laptop+" id="horizontal">
      <div class="wrapper info">
        <img
          src="../assets/images/orangelogo.svg"
          alt="Orange logo"
          class="logo logoHorizontal"
        />
        <div class="text">
          <div class="md-title">
            Gjør studiehverdagen effektiv
          </div>
          <div>
            Med Gazelle får du enkel tilgang til gjøremål og egen fremgang i
            alle fag.
          </div>
        </div>
      </div>
      <div id="divider"></div>
      <div class="wrapper">
        <LoginPage />
      </div>
    </mq-layout>
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
#vertical {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
#horizontal {
  height: 100vh;
  display: flex;
  align-items: center;
}
#divider {
  width: 3px;
  height: 70vh;
  background-color: lightgray;
}
.wrapper {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
}
.info {
  justify-content: space-between;
  padding-top: 8%;
  padding-bottom: 50vh;
}
.text {
  width: 60%;
}
.logo {
}
.logoVertical {
  position: absolute;
  top: 10%;
}
.logoHorizontal {
}
</style>
