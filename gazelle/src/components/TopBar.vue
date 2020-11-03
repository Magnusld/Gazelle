<template>
  <md-content v-if="this.$store.getters.isLoggedIn" class="md-layout md-primary md-elevation-2" id="topBar">
    <div class="md-layout-item md-layout">
      <div class="buttonContainer">
        <md-button class="md-icon-button" to="/">
          <img src="../assets/applogo.svg" alt="applogo" />
        </md-button>
      </div>
      <md-tabs class="md-primary" id="navTabs" md-sync-route>
        <md-tab md-label="Mine løp" to="/my-courses"></md-tab>
        <md-tab md-label="Fokuslista" to="/focus-list"></md-tab>
      </md-tabs>
    </div>
    <div class="md-layout-item buttonContainer">
      <md-button class="md-secondary" v-on:click="logout">Logg ut</md-button>
      <md-button class="md-icon-button" id="userInfo" to="/user-settings">
        <md-icon>person</md-icon>
      </md-button>
    </div>
  </md-content>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component
export default class TopBar extends Vue {
  private logout() {
    this.$store.dispatch("logout").then(() => {
      this.$router.push("/login");
    });
  }
}
</script>

<style scoped lang="scss">
#navTabs {
  margin: 0;
  width: 50%;
}
#navLogo {
  height: 45px;
  padding: 5px;
}
.buttonContainer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
#userInfo {
  float: right;
}
</style>
