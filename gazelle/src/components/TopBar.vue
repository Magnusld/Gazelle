<template>
  <md-app>
    <md-app-toolbar class="navBar md-primary">
      <img src="../assets/logo.png" alt="logo" id="navLogo" />

      <md-button>Mine Løp</md-button>
      <md-button>Fokuslista</md-button>

      <md-button id="userInfo">
        <span>Fornavn Etternavn</span>
        <img
          src="../assets/images/profPic.png"
          alt="profilbilde"
          id="profPic"
        />
      </md-button>

      <!--<div v-for="(route, index) in routes" :key="index">
        <router-link v-bind:to=route.path>{{ route.name }}</router-link>
      </div>-->
    </md-app-toolbar>
  </md-app>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component
export default class TopBar extends Vue {
  routes = [
    { name: "Mine Løp", path: "/" },
    { name: "Fokuslista", path: "/About" }
  ];
}
</script>

<style scoped lang="scss">
.navBar {
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 10px;
  position: sticky;
  top: 0;
  color: white;
  background-color: #075a86;
}
#navLogo {
  height: 40px;
}
#profPic {
  border-radius: 50%;
  height: 30px;
  margin-left: 5px;
}
#userInfo {
  display: flex;
  align-items: center;
  margin-left: auto;
}
</style>
