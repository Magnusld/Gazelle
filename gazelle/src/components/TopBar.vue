<template>
  <div class="navBar">
    <img src="../assets/logo.png" alt="logo" id="navLogo"/>

    <button text>Mine Løp</button>
    <button text>Fokuslista</button>

    <div onclick="" id="userInfo">
      <span>Fornavn Etternavn</span>
      <img src="../assets/images/profPic.png" alt="profilbilde" id="profPic"/>
    </div>

    <!--<div v-for="(route, index) in routes" :key="index">
      <router-link v-bind:to=route.path>{{ route.name }}</router-link>
    </div>-->
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component
export default class TopBar extends Vue {
  routes = [
        { name: "Mine Løp", path: "/" },
        { name: "Fokuslista", path: "/About" }];
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

  background-color: #075A86;
}
#navLogo {
  height: 55px;
}
#profPic{
  border-radius: 50%;
  height: 55px;
  margin-left: 5px;
}
#userInfo {
  display: flex;
  align-items: center;
  margin-left: auto;

  color: white;
}

</style>
