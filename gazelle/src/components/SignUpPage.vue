<template>
  <form class="signUpPage">
    <div class="horizontalSeparator">
      <span class="md-headline">Opprett ny bruker:</span>
    </div>
    <md-divider></md-divider>
    <div class="horizontalWrapper">
      <md-field class="horizontalField">
        <label>Fornavn:</label>
        <md-input v-model="firstname" autocomplete="given-name"></md-input>
      </md-field>
      <md-field class="horizontalField">
        <label>Etternavn:</label>
        <md-input v-model="lastname" autocomplete="family-name"></md-input>
      </md-field>
    </div>
    <md-field>
      <label>E-post:</label>
      <md-input v-model="email" type="email" autocomplete="email"></md-input>
    </md-field>
    <div class="horizontalWrapper">
      <md-field :md-toggle-password="true" class="horizontalField">
        <label>Passord:</label>
        <md-input v-model="password" type="password"></md-input>
      </md-field>
      <md-field :md-toggle-password="false" class="horizontalField">
        <label>Gjenta passord:</label>
        <md-input v-model="repeatedPassword" type="password"></md-input>
      </md-field>
    </div>
    <div class="errorMessage" v-if="error">{{ error }}</div>
    <md-button class="md-raised md-primary signUpButton" v-on:click="signUp">
      Opprett Bruker</md-button
    >
  </form>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { signup } from "@/client/login";
import router from "@/router";

@Component
export default class SignUpPage extends Vue {
  private firstname = "";
  private lastname = "";
  private email = "";
  private password = "";
  private repeatedPassword = "";

  private error = "";

  private async signUp() {
    this.error = "";
    const firstname: string = this.firstname;
    const lastname: string = this.lastname;
    const email: string = this.email;
    const password: string = this.password;

    if (this.repeatedPassword !== this.password) {
      this.error = 'Passordene er ikke like.';
      return;
    }
    try {
      await signup({ firstname, lastname, email, password });
      await router.replace("/");
    } catch (e) {
      if (e.status == undefined)
        this.error = `Klarte ikke koble til tjener: ${e.message}`;
      else this.error = `${e.message}`;
    }
  }
}
</script>

<style scoped>
.signUpPage {
  max-width: 500px;
  width: 90%;
}
.horizontalSeparator {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 0 10px 10px;
}
.horizontalWrapper {
  display: flex;
  justify-content: space-between;
}
.wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 90vh;
}
.errorMessage {
  font-size: 1.2rem;
  text-align: center;
  color: red;
}
.signUpButton {
  display: flex;
  width: 100%;
}
.horizontalField {
  width: 48%;
}
</style>
