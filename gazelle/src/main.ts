import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import VueMaterial from "vue-material";
import "vue-material/dist/vue-material.min.css";
import "vue-material/dist/theme/default.css";
import VueMarkdown from 'vue-markdown';

Vue.config.productionTip = false;
Vue.use(VueMaterial);

new Vue({
  components: {
    'vue-markdown': VueMarkdown
  },
  router,
  store,
  render: h => h(App)
}).$mount("#app");
