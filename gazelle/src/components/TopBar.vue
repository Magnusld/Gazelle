<template>
  <div>
    <md-toolbar
      v-if="this.$store.getters.isLoggedIn"
      class="md-layout md-primary md-elevation-2 md-dense"
      id="topBar"
    >
      <mq-layout class="child" :mq="['mobile', 'tablet']">
        <div class="buttonContainer">
          <md-button class="md-icon-button" @click="showNavigation = true">
            <md-icon>menu</md-icon>
          </md-button>
        </div>
      </mq-layout>
      <mq-layout mq="laptop+" class="child md-layout-item md-layout">
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
        class="child md-primary"
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
      <div class="md-layout-item child buttonContainer" id="userInfo">
        <md-button class="md-secondary logoutButton" v-on:click="logout"
          >Logg ut</md-button
        >
        <mq-layout mq="laptop+">
          <div>
            {{
              $store.getters.loggedInUser.firstName +
                " " +
                $store.getters.loggedInUser.lastName
            }}
          </div>
        </mq-layout>
      </div>
    </md-toolbar>

    <md-drawer :md-active.sync="showNavigation" md-swipeable>
      <md-toolbar class="md-transparent" md-elevation="0">
        <span class="md-title">Gazelle</span>
      </md-toolbar>

      <md-list>
        <md-list-item to="/my-courses">
          <md-icon>list</md-icon>
          <span class="md-list-item-text">Mine løp</span>
        </md-list-item>

        <md-list-item to="/followed-courses">
          <md-icon>playlist_add_check</md-icon>
          <span class="md-list-item-text">Fulgte løp</span>
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
  }
}
</script>

<style scoped lang="scss">
#topBar {
  display: flex;
  justify-content: space-between;
}
.child {
  width: 33%;
}
#navTabs {
  margin: 0;
  width: 100%;
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
  align-items: center;
}
#userInfo {
  justify-content: flex-end;
}
.md-list-item-text {
  color: black;
}
</style>
