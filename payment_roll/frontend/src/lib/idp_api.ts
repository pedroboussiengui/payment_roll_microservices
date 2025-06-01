
import axios from "axios";
import { TokenStorage } from "./infra/storage/TokenStorage";

const idp = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 5000
});

// intercept request: add the Bearer access token
idp.interceptors.request.use((config) => {
    const accessToken = TokenStorage.getAccessToken();
    if (accessToken) {
        config.headers.Authorization = `${accessToken}`;
    }
    return config
}, Promise.reject);

export default idp;