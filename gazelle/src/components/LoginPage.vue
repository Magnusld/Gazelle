<template>
  <div class="loginPage">
    <div class="horizontalSeparator">
      <span class="md-headline">Innlogging</span>
    </div>
    <md-divider></md-divider>
    <md-field md-clearable>
      <label>Brukernavn:</label>
      <md-input v-model="initial"></md-input>
    </md-field>
    <md-field :md-toggle-password="true">
      <label>Passord:</label>
      <md-input v-model="password" type="password"></md-input>
    </md-field>
    <div class="buttonBar">
      <md-button class="md-raised md-primary">Logg inn</md-button>
      <md-button class="md-primary">Ny bruker</md-button>
    </div>
  </div>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";

@Component
export default class LoginPage extends Vue{
}

</script>

<style scoped lang="scss">

.loginPage{
  margin: auto;
  width: 30%;
}
.horizontalSeparator{
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 75px 0 10px 10px;
}

.buttonBar{
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: auto;
  width: 100%;
}


</style>