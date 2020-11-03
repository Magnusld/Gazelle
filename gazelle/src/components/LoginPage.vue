<template>
  <form class="wrapper">
    <div class="loginPage">
      <div class="horizontalSeparator">
        <span class="md-headline">Innlogging</span>
      </div>
      <md-divider></md-divider>
      <md-field md-clearable>
        <label>Brukernavn:</label>
        <md-input v-model="username" type="username"></md-input>
      </md-field>
      <md-field :md-toggle-password="true">
        <label>Passord:</label>
        <md-input v-model="password" type="password"></md-input>
      </md-field>
      <div class="buttonBar">
        <md-button
          class="md-raised md-primary"
          v-on:keyup.enter="login"
          v-on:click="login"
          >Logg inn</md-button
        >
        <md-button class="md-primary" v-on:click="register"
          >Ny bruker</md-button
        >
      </div>
    </div>
  </form>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
@Component
export default class LoginPage extends Vue {
  private username = "";
  private password = "";

  private login() {
    const username: string = this.username;
    const password: string = this.password;
    this.$store
      .dispatch("login", { username, password })
      .then(() => this.$router.push("/"))
      .catch(err => console.log(err));
  }

  private register() {
    const username: string = this.username;
    const password: string = this.password;
    this.$store
      .dispatch("register", { username, password })
      .then(() => this.$router.push("/"))
      .catch(err => console.log(err));
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

.buttonBar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: auto;
  width: 100%;
}
</style>
