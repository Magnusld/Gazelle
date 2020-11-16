<template>
  <form class="loginPage">
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
  </form>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import router from "@/router";
import { login } from "@/client/login";

@Component
export default class LoginPage extends Vue {
  private email = "";
  private password = "";
  private error = "";

  mounted() {
    const reason = this.$route.query.reason;
    if (reason == "invalidated") this.error = "Du har blitt logget ut";
  }

  private async login() {
    this.error = "";
    const email: string = this.email;
    const password: string = this.password;
    try {
      await login({ email, password });
      await router.replace("/");
    } catch (e) {
      if (e.status == undefined)
        this.error = `Klarte ikke koble til tjener: ${e.message}`;
      if (e.status == 401) this.error = "Brukernavn eller passord er feil";
      else this.error = `${e.message} (${e.status})`;
    }
  }
}
</script>

<style scoped lang="scss">
.loginPage {
  max-width: 500px;
  width: 90%;
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
  color: #ff0000;
}

.buttonBar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: auto;
  width: 100%;
}
</style>
