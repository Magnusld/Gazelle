import Vue from "vue";
import Vuex from "vuex";
import { UserResponse } from "@/client/types";

Vue.use(Vuex);

export type LogInStatus = "loggedOut" | "loggedIn" | "fakeLoggedIn" | "loading";

export interface State {
  status: LogInStatus;
  token: string | null;
  user: UserResponse | null;
}

const defaultState = (): State => {
  const token = localStorage.getItem("token");
  const userId = localStorage.getItem("userId");
  if (token != null && userId != null) {
    return {
      status: "fakeLoggedIn",
      token,
      user: { id: +userId, firstname: "", lastname: "" }
    };
  }
  return { status: "loggedOut", token: null, user: null };
};

export default new Vuex.Store({
  state: defaultState(),
  mutations: {
    authRequest(state: State) {
      state.status = "loading";
    },
    authSuccess(
      state: State,
      { token, user }: { token: string; user: UserResponse }
    ) {
      state.status = "loggedIn";
      state.token = token;
      state.user = user;
    },
    userObjectFound(state: State, user: UserResponse) {
      state.status = "loggedIn";
      state.user = user;
    },
    authFailed(state: State) {
      state.status = "loggedOut";
      state.token = null;
      state.user = null;
    },
    logout(state: State) {
      state.status = "loggedOut";
      state.token = null;
      state.user = null;
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
