<template>
  <div id="topBar">
    <md-content class="md-layout md-primary md-elevation-2">
      <div class="md-layout-item md-layout">
        <router-link to="/">
          <img src="../assets/logo.png" alt="logo" id="navLogo"/>
        </router-link>
        <md-tabs class="md-primary" id="navTabs" md-sync-route>
          <md-tab md-label="Mine løp" to="/my-courses"></md-tab>
          <md-tab md-label="Fokuslista" to="/focus-list"></md-tab>
        </md-tabs>
      </div>
      <div class="md-layout-item" id="buttonContainer">
        <md-button class="md-icon-button" id="userInfo" to="/user-settings">
          <md-icon>person</md-icon>
        </md-button>
      </div>
    </md-content>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component
export default class TopBar extends Vue {
}
</script>

<style scoped lang="scss">
#topBar{
  z-index: 100;
  width: 100%;
}
#navTabs{
  margin:0;
  width: 50%;
}
#navLogo {
  height: 45px;
  padding: 5px;
}
#buttonContainer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
#userInfo {
  float:right;
}
</style>
