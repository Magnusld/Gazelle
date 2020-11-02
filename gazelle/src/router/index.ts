import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import MyCourses from "@/views/MyCourses.vue";
import FocusList from "@/views/FocusList.vue";
import UserSettings from "@/views/UserSettings.vue";
import LoginPage from "@/components/LoginPage.vue";
import Home from "@/views/Home.vue";
import SignUpPage from "@/components/SignUpPage.vue";
import store from "@/store";

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: '/',
    name: 'home',
    component: Home
  },
  {
    path: "/my-courses",
    name: "My courses",
    component: MyCourses,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/focus-list",
    name: "Focus list",
    component: FocusList,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/user-settings",
    name: "User settings",
    component: UserSettings,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/login",
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

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (store.getters.isLoggedIn) {
      next();
      return;
    }
    next("/login");
  } else {
    next();
  }
});

export default router;
