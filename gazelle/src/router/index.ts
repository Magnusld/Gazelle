import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import MyCourses from "@/views/MyCourses.vue";
import FocusList from "@/views/FocusList.vue";
import UserSettings from "@/views/UserSettings.vue";
import Home from "@/views/Home.vue";
import SignUpPage from "@/components/SignUpPage.vue";
import store from "@/store";
import LoginView from "@/views/LoginView.vue";

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: "/",
    name: "home",
    component: Home,
    meta: {
      requiresAuth: true
    }
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
    component: LoginView,
    meta: {
      requiresLoggedOut: true
    }
  },
  {
    path: "/signUp",
    name: "Sign Up Page",
    component: SignUpPage,
    meta: {
      requiresLoggedOut: true
    }
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
  } else if (to.matched.some(record => record.meta.requiresLoggedOut)) {
    if (!store.getters.isLoggedIn) {
      next();
      return;
    }
    next("/");
  } else {
    next();
  }
});

export default router;
