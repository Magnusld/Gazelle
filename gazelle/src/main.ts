import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import restClient from "@/client/restClient";
import VueMaterial from "vue-material";
import VueMq from "vue-mq";
import "vue-material/dist/vue-material.min.css";
import "vue-material/dist/theme/default.css";

const baseURL =
  process.env.NODE_ENV == "production"
    ? "https://gazelle.haved.no/api"
    : window.location.hostname.includes("gitpod")
    ? `https://8088-${window.location.hostname.substr(5)}`
    : "http://localhost:8088";
restClient.setBaseURL(baseURL);
Vue.config.productionTip = false;
Vue.use(VueMaterial);
Vue.use(VueMq, {
  breakpoints: {
    mobile: 450,
    tablet: 965,
    laptop: 1250,
    desktop: Infinity
  }
});

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
