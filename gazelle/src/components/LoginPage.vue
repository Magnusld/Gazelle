<template>
  <form class="wrapper">
    <div class="loginPage">
      <div class="horizontalSeparator">
        <span class="md-headline">Innlogging</span>
      </div>
      <md-divider></md-divider>
      <md-field md-clearable>
        <label>E-post:</label>
        <md-input v-model="email" type="email" autocomplete="email"></md-input>
      </md-field>
      <md-field :md-toggle-password="true">
        <label>Passord:</label>
        <md-input
          v-model="password"
          type="password"
          autocomplete="password"
        ></md-input>
      </md-field>
      <div class="errorMessage" v-if="error">{{ error }}</div>
      <div class="buttonBar">
        <md-button
          class="md-raised md-primary"
          v-on:keyup.enter="login"
          v-on:click="login"
          >Logg inn</md-button
        >
        <md-button class="md-primary" to="/signup">Ny bruker</md-button>
      </div>
    </div>
  </form>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { login } from "@/client/login";

@Component
export default class LoginPage extends Vue {
  private email = "";
  private password = "";
  private error?: string = "";

  private async login() {
    this.error = undefined;
    const email: string = this.email;
    const password: string = this.password;
    try {
      await login({ email, password });
    } catch (e) {
      if (e.status == undefined)
        this.error = `Klarte ikke koble til tjener: ${e.message}`;
      else this.error = `${e.message} (${e.status})`;
    }
  }
}
</script>

<style scoped lang="scss">
.wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 90vh;
}
.loginPage {
  width: 30%;
}
.horizontalSeparator {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 0 10px 10px;
}

.errorMessage {
  font-size: 1.2rem;
  text-align: center;
  color: red;
}

.buttonBar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: auto;
  width: 100%;
}
</style>
