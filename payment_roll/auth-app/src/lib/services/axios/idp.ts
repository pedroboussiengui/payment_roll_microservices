import { accessToken } from "$lib/store/auth";
import axios from "axios";
import { get } from "svelte/store";

const idp = axios.create({
    baseURL: import.meta.env.VITE_IDENTITY_PROVIDER_URL,
    withCredentials: true
});

// idp.interceptors.request.use((config) => {
//     const token = get(accessToken);
//     if (token) {
//         // not pass as Bearer
//         config.headers.Authorization = token;
//     }
//     return config
// }, Promise.reject);

export default idp;