import { accessToken } from "$lib/store/auth";
import axios from "axios";
import { get } from "svelte/store";
import { logout, refreshToken } from "../auth";


const payroll = axios.create({
    baseURL: import.meta.env.VITE_PAYROLL_API_URL
});

payroll.interceptors.request.use((config) => {
    const token = get(accessToken);
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config
}, Promise.reject);

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