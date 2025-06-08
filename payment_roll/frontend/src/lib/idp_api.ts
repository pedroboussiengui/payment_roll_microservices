
import axios from "axios";
// import { TokenStorage } from "./infra/storage/TokenStorage";
import { accessToken } from "./stores/accessToken";

const idp = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 5000,
    withCredentials: true
});

// intercept request: add the Bearer access token
idp.interceptors.request.use((config) => {
    const bearerToken = accessToken.get();
    // console.log(`interceptor token IDP: ${accessToken}`);
    if (bearerToken) {
        // not pass the Bearer
        config.headers.Authorization = `${bearerToken}`;
    }
    return config
}, Promise.reject);

export default idp;