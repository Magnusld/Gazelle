import {createLocalVue} from "@vue/test-utils";
import VueMaterial from "vue-material";
import VueRouter from "vue-router";
import nock from "nock";
import {UserResponse} from "@/client/types";
import store from "@/store";

const localVue = createLocalVue();
localVue.use(VueMaterial);
const router = new VueRouter();
localVue.use(VueRouter);

const baseURL = "http://localhost:8088/";

const scope: nock.Scope = nock(baseURL);

function delay(ms: number) {
    return new Promise(resolve => setTimeout(resolve, ms));
}
const user: UserResponse = {
    id: 1,
    firstname: "Ola",
    lastname: "Nordmann"
};

describe("FocusView og FocusPost tester", () => {
    beforeEach(() => {
        store.commit("logout");
        nock.cleanAll();
        store.commit("authSuccess", {user, token: 4567});
    });
    it("Test av FocusView", async () => {

    });
});