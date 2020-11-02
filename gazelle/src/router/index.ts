import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import MyCourses from "@/views/MyCourses.vue";
import FocusList from "@/views/FocusList.vue";
import UserSettings from "@/views/UserSettings.vue";
import LoginPage from "@/components/LoginPage.vue";
import SignUpPage from "@/components/SignUpPage.vue";

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
  },
  {
    path: "/",
    name: "Login Page",
    component: LoginPage
  },
  {
    path: "/signUp",
    name: "Sign Up Page",
    component: SignUpPage
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

export default router;
