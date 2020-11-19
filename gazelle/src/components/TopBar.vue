<template>
  <div>
    <md-toolbar
      v-if="this.$store.getters.isLoggedIn"
      class="md-layout md-primary md-elevation-2 md-dense"
      id="topBar"
    >
      <mq-layout :mq="['mobile', 'tablet']">
        <div class="buttonContainer">
          <md-button class="md-icon-button" @click="showNavigation = true">
            <md-icon>menu</md-icon>
          </md-button>
        </div>
      </mq-layout>

      <mq-layout mq="laptop+" class="md-layout-item md-layout">
        <div class="buttonContainer">
          <md-button class="md-icon-button" to="/">
            <img src="../assets/applogo.svg" alt="applogo" />
          </md-button>
        </div>
        <md-tabs
          ref="tabs"
          class="md-primary"
          id="navTabs"
          :md-active-tab="activeTab"
        >
          <md-tab
            id="my-courses-tab"
            md-label="Mine løp"
            to="/my-courses"
          ></md-tab>
          <md-tab
            id="followed-courses-tab"
            md-label="Fulgte løp"
            to="/followed-courses"
          ></md-tab>
          <md-tab
            id="focus-list-tab"
            md-label="Fokuslista"
            to="/focus-list"
          ></md-tab>
        </md-tabs>
      </mq-layout>
      <md-autocomplete
        class="search md-primary"
        v-model="selectedCourse"
        :md-options="courses"
        md-layout="box"
        md-dense
        @md-selected="goToCourse"
        @md-changed="loadCourses"
      >
        <label>Søk...</label>
        <template slot="md-autocomplete-item" slot-scope="{ item, term }">
          <md-highlight-text :md-term="term">{{ item.name }}</md-highlight-text>
        </template>
      </md-autocomplete>
      <div class="md-layout-item buttonContainer" id="userInfo">
        <md-button class="md-secondary" v-on:click="logout">Logg ut</md-button>
        <md-button class="md-icon-button" to="/user-settings">
          <md-icon>person</md-icon>
        </md-button>
      </div>
    </md-toolbar>

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
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { logout } from "@/client/login";
import { CourseResponse } from "@/client/types";
import { findAllCourses } from "@/client/course";

@Component
export default class TopBar extends Vue {
  private showNavigation = false;
  private activeTab: string | null = null;
  private selectedCourse: string | null = null;
  private courses: Promise<CourseResponse[]> | CourseResponse[] = [];

  private logout() {
    logout();
  }

  private reloadPage() {
    window.location.reload();
  }

  async loadCourses() {
    if (this.courses instanceof Promise) return;
    this.courses = findAllCourses();
  }

  private goToCourse(course: CourseResponse) {
    this.$router.push(`/courses/${course.id}`).catch(() => {
      return;
    });
    this.$nextTick(() => (this.selectedCourse = ""));
  }

  @Watch("$route")
  onRouteChange(): void {
    this.showNavigation = false;
    if (this.$route.path === "/my-courses") this.activeTab = "my-courses-tab";
    else if (this.$route.path === "/focus-list")
      this.activeTab = "focus-list-tab";
    else this.activeTab = null;
  }
}
</script>

<style scoped lang="scss">
#navTabs {
  margin: 0;
  width: 80%;
  padding: 0;
}
.navigationButtons {
  display: flex;
  align-items: center;
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
.search {
  max-width: 500px;
  width: 40%;
  margin: 0;
}
</style>
