
import axios from "axios";
import { TokenStorage } from "./infra/storage/TokenStorage";
import idp from "./idp_api";
import type { SessionTokens } from "./utils/types";
import { logout } from "$lib/auth";

const payroll = axios.create({
    baseURL: 'http://localhost:8081',
    timeout: 5000
});

async function refreshToken(): Promise<string | null> {
    if (!refreshToken) {
        return null;
    }
    try {
        const res = await idp.post("/refresh-tokens", {}, {
            withCredentials: true
        });
        const SessionTokens: SessionTokens = res.data;
        TokenStorage.setSession(SessionTokens.sessionId);
        return TokenStorage.getAccessToken();
    } catch (err) {
        console.error('Error refreshing token:', err);
        return null;    
    }
}

// intercept request: add the Bearer access token
payroll.interceptors.request.use((config) => {
    const accessToken = TokenStorage.getAccessToken() ?? TokenStorage.getToken();
    console.log(`interceptor token: ${accessToken}`);
    if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config
}, Promise.reject);

// intercept response: refresh tokens e try again
payroll.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;
        // if error is 401 - unouthorized, i can generate a new access token
        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            const newAccessToken = await refreshToken();
            if (newAccessToken) {
                originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
                return payroll(originalRequest);
            } else {
                logout();
            }
        }
        // if other erro I just response the error
        return Promise.reject(error);
    }
);

export default payroll