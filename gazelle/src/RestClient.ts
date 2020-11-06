import axios, {AxiosPromise} from "axios";

import store from './store';
import type { Course } from './types';

export interface RestError {
    status: number;
    reason?: string;
    message?: string;
}

export default class RestClient {

    private get loggedInUser() {
        return store.getters.loggedInUser;
    }
    private get loggedInUserId() {
        return this.loggedInUser.id;
    }

    private wrapPromise<T>(axiosPromise: AxiosPromise<T>, okStatus: number[]):Promise<T> {
        return new Promise<T>(((resolve, reject) => {
            axiosPromise
                .then(it => {
                    if(!okStatus.includes(it.status))
                        reject({status: it.status, reason: it.data});
                    resolve(it.data)
                }).catch(e => {

                });
        }));
    }

    private get<T>(path: string): Promise<T> {
        return this.wrapPromise(axios({
            url: path,
            method: "GET"
        }));
    }

    public async getFollowedCourses():Promise<Course[]> {
        return this.get("https://localhost:8088/users/"+this.loggedInUserId+"/followedCourses");
    }
}