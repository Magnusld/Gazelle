<template>
  <div>
    <mq-layout :mq="['mobile', 'tablet']" id="vertical">
      <img
        src="../assets/images/orangelogo.svg"
        alt="Orange logo"
        class="logo logoVertical"
      />
      <LoginPage v-if="mode === 'login'" class="form" />
      <SignUpPage v-if="mode === 'signUp'" class="form" />
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
        <LoginPage v-if="mode === 'login'" class="form" />
        <SignUpPage v-if="mode === 'signUp'" class="form" />
      </div>
    </mq-layout>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import LoginPage from "../components/LoginPage.vue";
import SignUpPage from "@/components/SignUpPage.vue";

@Component({
  components: {
    LoginPage,
    SignUpPage
  }
})
export default class LoginView extends Vue {
  @Prop({ default: "login" }) private mode!: string | null;
}
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
.form {
  position: relative;
}
.logoVertical {
  top: 5%;
  position: relative;
}
.logoHorizontal {
}
</style>
