import restClient from "./restClient";
import store from "@/store";
import { User } from "@/types";
import { LogInRequest, LogInResponse, SignUpRequest } from "@/client/types";

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
    store.commit("authSuccess", response);
  } catch (e) {
    store.commit("authFailed");
    throw e;
  }
}

/**
 * Try using a previously acquired token to log in again.
 * Does not save to or load from localStorage.
 *
 * @param token
 * @throws RestError if the request fails
 */
export async function loginWithToken(token: string): Promise<void> {
  if (store.getters.isLoggedIn) throw { message: "Already logged in" };
  store.commit("authRequest");

  try {
    //Make an ad-hoc header
    const headers = {
      Authorization: `Bearer ${token}`
    };
    const user: User = await restClient.get("/login", { headers });
    store.commit("authSuccess", { user, token });
  } catch (e) {
    store.commit("authFailed");
    throw e;
  }
}

/**
 * Tries using the localStorage token to log in.
 * If it doesn't exist, goes to loggedOut state.
 * If it exists but doesn't work, deletes it.
 */
export async function tryLocalStorageLogin(): Promise<void> {
  const token = localStorage.getItem("token");
  if (token == undefined) {
    store.commit("logout");
    return;
  }

  try {
    await loginWithToken(token);
  } catch (e) {
    localStorage.removeItem("token");
  }
}

/**
 * Tells the server to invalidate our token. Changes state to logged out.
 * Removes the token from localStorage.
 * @throws if we are not logged in, or if the request fails.
 *     If the request gives
 */
export async function logout(): Promise<void> {
  if (!store.getters.isLoggedIn) throw { message: "Not logged in" };

  try {
    await restClient.post("/logout");
  } catch (e) {
    if (e.status != 401)
      //If the token was invalid, we don't really care
      throw e;
  }

  localStorage.removeItem("token");
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
    store.commit("authSuccess", response);
  } catch (e) {
    store.commit("authFailed");
    throw e;
  }
}
