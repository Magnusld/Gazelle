import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import restClient from "@/client/restClient";
import VueMaterial from "vue-material";
import "vue-material/dist/vue-material.min.css";
import "vue-material/dist/theme/default.css";

const baseURL =
  process.env.NODE_ENV == "production"
    ? "https://gazelle.haved.no/api"
    : "http://localhost:8088";
restClient.setBaseURL(baseURL);
Vue.config.productionTip = false;
Vue.use(VueMaterial);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
