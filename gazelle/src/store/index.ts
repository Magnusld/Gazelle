import Vue from "vue";
import Vuex from "vuex";
import { User } from "@/types";

Vue.use(Vuex);

export type LogInStatus = "loggedOut" | "loggedIn" | "fakeLoggedIn" | "loading";

export interface State {
  status: LogInStatus;
  token?: string;
  user?: User;
}

const defaultState = (): State => {
  const token = localStorage.getItem("token");
  const userId = localStorage.getItem("userId");
  if (token != null && userId != null) {
    return {
      status: "fakeLoggedIn",
      token,
      user: { id: +userId }
    };
  }
  return { status: "loggedOut" };
};

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
    isLoggedIn: state =>
      state.status == "loggedIn" || state.status == "fakeLoggedIn",
    awaitingSignIn: state => state.status == "loading",
    isFakeLoggedIn: state => state.status == "fakeLoggedIn",
    authStatus: state => state.status,
    loggedInUser: state => state.user,
    loggedInUserId: state => state.user?.id,
    token: state => state.token
  }
});
