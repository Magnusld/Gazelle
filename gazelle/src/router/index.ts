import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import MyCourses from "@/views/MyCourses.vue";
import FocusList from "@/views/FocusList.vue";
import UserSettings from "@/views/UserSettings.vue";

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: "/my-courses",
    name: "My courses",
    component: MyCourses
  },
  {
    path: "/focus-list",
    name: "Focus list",
    component: FocusList
  },
  {
    path: "/user-settings",
    name: "User settings",
    component: UserSettings
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

export default router;
