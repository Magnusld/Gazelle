import Vue from "vue";
import Vuex from "vuex";
import axios from "axios";
import { User } from "@/types";

Vue.use(Vuex);

export interface State {
  status: string;
  token?: string;
  user?: User;
}

const defaultState = (): State => ({
  status: "",
  token: localStorage.getItem('token') || undefined
});

export default new Vuex.Store({
  state: defaultState(),
  mutations: {
    authRequest(state: State) {
      state.status = "loading";
    },
    authSuccess(state: State, { token, user }: { token: string; user: User }) {
      state.status = "success";
      state.token = token;
      state.user = user;
    },
    authError(state: State) {
      state.status = "error";
    },
    logout(state: State) {
      state.status = "";
      state.token = "";
    }
  },
  actions: {
    login(
      { commit },
      { username, password }: { username: string; password: string }
    ) {
      return new Promise((resolve, reject) => {
        commit("authRequest");
        axios({
          url: "http://localhost:8088/login",
          data: { username, password },
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
      { username, password }: { username: string; password: string }
    ) {
      return new Promise((resolve, reject) => {
        commit("authRequest");
        axios({
          url: "http://localhost:8088/signup",
          data: { username, password },
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
    authStatus: state => state.status
  }
});
