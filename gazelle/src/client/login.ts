import restClient from "./restClient";
import store from "@/store";
import {
  LogInRequest,
  LogInResponse,
  SignUpRequest,
  UserResponse
} from "@/client/types";
import router from "@/router";

/**
 * Logs in with email and password. Updates store when starting and if successful.
 * Also saves the acquired token to localStore.
 *
 * @param loginRequest the request
 * @throws RestError if the request fails
 */
export async function login(loginRequest: LogInRequest): Promise<void> {
  if (store.getters.isLoggedIn) throw { message: "Already logged in" };
  store.commit("authRequest");

  try {
    const response: LogInResponse = await restClient.postWithResponse(
      "/login",
      loginRequest
    );
    localStorage.setItem("token", response.token);
    localStorage.setItem("userId", `${response.user.id}`);
    store.commit("authSuccess", response);
  } catch (e) {
    store.commit("authFailed");
    throw e;
  }
}

/**
 * When we are already logged in, the token and userId is in localStorage.
 * Here we ask the server for the rest of the user object.
 * If we fail, the restClient will trigger automatic logout.
 *
 * If we are not logged in, this function is a NOP
 */
export async function tryGetRealUser() {
  if (!store.getters.isFakeLoggedIn) return;

  try {
    const user: UserResponse = await restClient.get("/login");
    store.commit("userObjectFound", user);
    localStorage.setItem("userId", `${user.id}`);
  } catch (e) {
    if (e.status != 401) throw e;
  }
}

/**
 * If a request failed with 401, and we are logged in we assume the token is invalid.
 * This will send the user to the login-page due to the router listening.
 */
export async function onTokenInvalid(): Promise<void> {
  localStorage.removeItem("token");
  localStorage.removeItem("userId");
  store.commit("authFailed");
}

/**
 * Tells the server to invalidate our token. Changes state to logged out.
 * Removes the token from localStorage.
 * @throws if we are not logged in, or if the request fails.
 *     If the request gives
 */
export async function logout(): Promise<void> {
  if (!store.getters.isLoggedIn) throw { message: "Not logged in" };

  // If the token is already invalid, this will throw a 401 which will log ut out either way
  await restClient.post("/logout");

  localStorage.removeItem("token");
  localStorage.removeItem("userId");
  store.commit("authRequest");
  await router.replace("/login");
  store.commit("logout");
}

/**
 * Create a new user and log in.
 * Will remember the token in localStorage.
 *
 * @param signupRequest
 * @throws RestError if the request fails
 */
export async function signup(signupRequest: SignUpRequest): Promise<void> {
  if (store.getters.isLoggedIn) throw { message: "Already logged in" };
  store.commit("authRequest");
  try {
    const response: LogInResponse = await restClient.postWithResponse(
      "/signup",
      signupRequest
    );
    localStorage.setItem("token", response.token);
    localStorage.setItem("userId", `${response.user.id}`);
    store.commit("authSuccess", response);
  } catch (e) {
    store.commit("authFailed");
    throw e;
  }
}
