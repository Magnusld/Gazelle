import axios, {AxiosInstance, AxiosPromise} from "axios";

import store from '@/store';

export interface RestError {
    status: number;
    reason?: string;
    message?: string;
}

export function wrapPromise<T>(axiosPromise: AxiosPromise<T>):Promise<T> {
    return new Promise<T>(((resolve, reject) => {
        axiosPromise
            .then(it => resolve(it.data))
            .catch(error => {
                if(error.response) {
                    let restError: RestError = {
                        status: error.response.status,
                        reason: error.response.data.reason,
                        message: error.response.data.message
                    }
                    reject(restError);
                } else
                    reject({
                        status: 0,
                        message: error.message
                    });
            });
    }));
}

export const validGet = (code: number) => code == 200;
export const validPost = (code: number) => code == 200 || code == 201 || code == 204;

export class RestClient {
    private readonly http: AxiosInstance;
    private readonly unwatchToken: Function;

    public constructor() {
        this.http = axios.create();

        // Whenever the token changes, change the default header
        this.unwatchToken = store.watch(
            () => store.getters.token,
            (token) => this.http.defaults.headers.common['Authorization'] = `Bearer ${token}`
        );
    }

    public setBaseURL(baseURL: string) {
        this.http.defaults.baseURL = baseURL;
    }

    public get<T>(path: string, params: any = {}): Promise<T> {
        return wrapPromise(this.http.get(path, {params, validateStatus: validGet}));
    }

    public post<T>(path: string, data?: any): Promise<T> {
        return wrapPromise(this.http.post(path, data, {validateStatus: validPost}));
    }
}

export default new RestClient();