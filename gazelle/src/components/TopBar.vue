<template>
  <md-content
    v-if="this.$store.getters.isLoggedIn"
    class="md-layout md-primary md-elevation-2"
    id="topBar"
  >
    <mq-layout mq="mobile">
      <div class="buttonContainer">
        <md-button class="md-icon-button" @click="showNavigation = true">
          <md-icon>menu</md-icon>
        </md-button>
      </div>
      <md-drawer :md-active.sync="showNavigation" md-swipeable>
        <md-toolbar class="md-transparent" md-elevation="0">
          <span class="md-title">Gazelle</span>
        </md-toolbar>

        <md-list>
          <md-list-item to="/">
            <md-icon>home</md-icon>
            <span class="md-list-item-text">Hjem</span>
          </md-list-item>

          <md-list-item to="/my-courses">
            <md-icon>list</md-icon>
            <span class="md-list-item-text">Mine løp</span>
          </md-list-item>

          <md-list-item to="/focus-list">
            <md-icon>event_available</md-icon>
            <span class="md-list-item-text">Fokuslista</span>
          </md-list-item>
        </md-list>
      </md-drawer>
    </mq-layout>
    <mq-layout mq="tablet+" class="md-layout-item md-layout">
      <div class="buttonContainer">
        <md-button class="md-icon-button" to="/">
          <img src="../assets/applogo.svg" alt="applogo" />
        </md-button>
      </div>
      <md-tabs class="md-primary" id="navTabs" md-sync-route>
        <md-tab md-label="Mine løp" to="/my-courses"></md-tab>
        <md-tab md-label="Fokuslista" to="/focus-list"></md-tab>
      </md-tabs>
    </mq-layout>
    <div class="md-layout-item buttonContainer" id="userInfo">
      <md-button
        class="md-secondary"
        v-on:click="
          logout();
          reloadPage();
        "
        >Logg ut</md-button
      >
      <md-button class="md-icon-button" to="/user-settings">
        <md-icon>person</md-icon>
      </md-button>
    </div>
  </md-content>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { logout } from "@/client/login";

@Component
export default class TopBar extends Vue {
  private logout() {
    logout();
  }
  public showNavigation = false;

  private reloadPage() {
    window.location.reload();
  }

  @Watch("$route")
  onRouteChange(): void {
    this.showNavigation = false;
  }
}
</script>

<style scoped lang="scss">
#navTabs {
  margin: 0;
  width: 70%;
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
  width: 25%;
}
.md-list-item-text {
  color: black;
}
</style>
