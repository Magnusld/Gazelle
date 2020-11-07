import Vue from "vue";
import Vuex from "vuex";
import restClient, {RestError} from "@/client/RestClient";
import { User } from "@/types";

Vue.use(Vuex);

export type LogInStatus = "loggedOut" | "loggedIn" | "loading" | "error";

export interface State {
  status: LogInStatus;
  errorString?: string;
  token?: string;
  user?: User;
}

const defaultState = (): State => ({
  status: "loading"
});

export default new Vuex.Store({
  state: defaultState(),
  mutations: {
    authRequest(state: State) {
      state.status = "loading";
    },
    authSuccess(state: State, { token, user }: { token: string; user: User }) {
      state.status = "loggedIn";
      state.token = token;
      state.user = user;
    },
    authError(state: State, errorString: string) {
      state.status = "error";
      state.errorString = errorString;
    },
    logout(state: State) {
      state.status = "loggedOut";
      state.token = undefined;
      state.user = undefined;
    }
  },
  actions: {
    async login(
      { commit },
      { email, password }: { email: string; password: string }
    ) {

      commit("authRequest");
      try {
        const response = await restClient.post("/login", {email, password})
      } catch(e) {

      }


      return new Promise((resolve, reject) => {

        axios({
          url: "http://localhost:8088/login",
          data: { email, password },
          method: "POST"
        })
          .then(resp => {
            const token = resp.data.token;
            const user = resp.data.user;
            localStorage.setItem("token", token);
            axios.defaults.headers.common["Authorization"] = token;
            commit("authSuccess", { token, user });
            resolve(resp);
          })
          .catch(err => {
            commit("authError");
            localStorage.removeItem("token");
            reject(err);
          });
      });
    },
    register(
      { commit },
      {
        firstname,
        lastname,
        email,
        password
      }: {
        firstname: string;
        lastname: string;
        email: string;
        password: string;
      }
    ) {
      return new Promise((resolve, reject) => {
        commit("authRequest");
        axios({
          url: "http://localhost:8088/signup",
          data: { firstname, lastname, email, password },
          method: "POST"
        })
          .then(resp => {
            const token = resp.data.token;
            const user = resp.data.user;
            localStorage.setItem("token", token);
            axios.defaults.headers.common["Authorization"] = token;
            commit("authSuccess", token, user);
            resolve(resp);
          })
          .catch(err => {
            commit("authError", err);
            localStorage.removeItem("token");
            reject(err);
          });
      });
    },
    logout({ commit }) {
      return new Promise((resolve, reject) => {
        commit("logout");
        localStorage.removeItem("token");
        delete axios.defaults.headers.common["Authorization"];
        resolve();
      });
    }
  },
  modules: {},
  getters: {
    isLoggedIn: state => !!state.token,
    authStatus: state => state.status,
    loggedInUser: state => state.user,
    token: state => state.token
  }
});
