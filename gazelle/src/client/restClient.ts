import axios, { AxiosInstance, AxiosPromise } from "axios";

import store from "@/store";
import router from "@/router";

export interface RestError {
  status?: number;
  message?: string;
}

export function wrapPromise<T>(axiosPromise: AxiosPromise<T>): Promise<T> {
  return new Promise<T>((resolve, reject) => {
    axiosPromise
      .then(it => resolve(it.data))
      .catch(async error => {
        if (error.response) {
          const restError: RestError = {
            status: error.response.status,
            message: error.response.data.message
          };
          if (restError.status == 401) {
            //Unauthorized - token is invalid
            localStorage.removeItem("token");
            store.commit("loading");
            await router.replace("/login?reason=invalidated");
            store.commit("authFailed");
          }
          reject(restError);
        } else
          reject({
            message: error.message
          });
      });
  });
}

// Only 200 (OK) is valid with content for a GET
export const validGet = (code: number) => code == 200;

// 200 (OK), 201 (Created) and 204 (No content)
export const validPost = (code: number) =>
  code == 200 || code == 201 || code == 204;

// 200 (OK), 201 (Created) and 204 (No content)
export const validPut = (code: number) => code == 200 || code == 204;

// Only 200 (OK) has content in the post response
export const validPostWithResponseBody = (code: number) => code == 200;

// 200 (OK), 202 (Accepted) and 204 (No Content)
export const validDelete = (code: number) =>
  code == 200 || code == 202 || code == 204;

export class RestClient {
  private readonly http: AxiosInstance;
  private readonly unwatchToken: Function;

  public constructor() {
    this.http = axios.create();

    // Whenever the token changes, change the default header
    this.unwatchToken = store.watch(
      (state, getters) => getters.token,
      token =>
        (this.http.defaults.headers.common["Authorization"] = `Bearer ${token}`)
    );
  }

  public setBaseURL(baseURL: string) {
    this.http.defaults.baseURL = baseURL;
  }

  public get<T>(
    path: string,
    config?: { params?: object; headers?: object }
  ): Promise<T> {
    return wrapPromise(
      this.http.get(path, { ...config, validateStatus: validGet })
    );
  }

  public post(path: string, data?: object): Promise<void> {
    return wrapPromise(
      this.http.post(path, data, { validateStatus: validPost })
    );
  }

  public put(path: string, data?: object): Promise<void> {
    return wrapPromise(this.http.put(path, data, { validateStatus: validPut }));
  }

  public postWithResponse<T>(path: string, data?: object): Promise<T> {
    return wrapPromise(
      this.http.post(path, data, { validateStatus: validPostWithResponseBody })
    );
  }

  public delete(path: string): Promise<void> {
    return wrapPromise(this.http.delete(path, { validateStatus: validDelete }));
  }
}

export default new RestClient();
