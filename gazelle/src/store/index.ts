import Vue from "vue";
import Vuex from "vuex";
import { User } from "@/types";

Vue.use(Vuex);

export type LogInStatus = "loggedOut" | "loggedIn" | "loading";

export interface State {
  status: LogInStatus;
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
    authFailed(state: State) {
      state.status = "loggedOut";
      state.token = undefined;
      state.user = undefined;
    },
    logout(state: State) {
      state.status = "loggedOut";
      state.token = undefined;
      state.user = undefined;
    }
  },
  actions: {},
  modules: {},
  getters: {
    isLoggedIn: state => state.status == "loggedIn",
    awaitingSignIn: state => state.status == "loading",
    authStatus: state => state.status,
    loggedInUser: state => state.user,
    token: state => state.token
  }
});
