import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import restClient from "@/client/restClient";
import VueMaterial from "vue-material";
import VueMq from "vue-mq";
import "vue-material/dist/vue-material.min.css";
import "vue-material/dist/theme/default.css";

restClient.setBaseURL("http://localhost:8088");
Vue.config.productionTip = false;
Vue.use(VueMaterial);
Vue.use(VueMq, {
  breakpoints: {
    mobile: 450,
    tablet: 800,
    laptop: 1250,
    desktop: Infinity
  }
});

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
