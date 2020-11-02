<template>
  <div class="wrapper">
    <div class="signUpPage">
      <div class="horizontalSeparator">
        <span class="md-headline">Opprett ny bruker:</span>
      </div>
      <md-divider></md-divider>
      <div class="horizontalWrapper">
        <md-field class="horizontalField">
          <label>Fornavn:</label>
          <md-input v-model="initial" autocomplete="given-name"></md-input>
        </md-field>
        <md-field class="horizontalField">
          <label>Etternavn:</label>
          <md-input v-model="initial" autocomplete="family-name"></md-input>
        </md-field>
      </div>
      <md-field>
        <label>E-post:</label>
        <md-input
          v-model="initial"
          type="email"
          autocomplete="email"
        ></md-input>
      </md-field>
      <div class="horizontalWrapper">
        <md-field :md-toggle-password="true" class="horizontalField">
          <label>Passord:</label>
          <md-input v-model="password" type="password"></md-input>
        </md-field>
        <md-field :md-toggle-password="false" class="horizontalField">
          <label>Gjenta passord:</label>
          <md-input v-model="password" type="password"></md-input>
        </md-field>
      </div>
      <md-button
        class="md-raised md-primary signUpButton"
        v-on:keyup.enter="signUp"
      >
        Opprett Bruker</md-button
      >
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
@Component
export default class SignUpPage extends Vue {}
</script>

<style scoped>
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
.signUpButton {
  display: flex;
  width: 100%;
}
.horizontalField {
  width: 48%;
}
</style>
