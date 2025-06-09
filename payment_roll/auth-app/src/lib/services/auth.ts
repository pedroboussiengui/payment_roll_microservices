import { goto } from "$app/navigation";
import { accessToken } from "$lib/store/auth";
import idp from "./axios/idp";

export async function authenticate(token: string) {
    accessToken.set(token)
}

export async function getTokens(contractId: string) {
    const res = await idp.post(`/tokens/${contractId}`);
    accessToken.set(res.data.accessToken);
    goto('/home')
}

export async function refreshToken(): Promise<string | null> {
    return await idp.post("/refresh-tokens")
        .then((res) => {
            accessToken.set(res.data.accessToken);
            return res.data.accessToken;
        })
        .catch((err) => {
            console.error('Error refreshing token:', err);
            return null;
        });
}

export function logout() {
    idp.post("/logout")
    accessToken.set(null)
    window.location.href = "http://localhost:8080/auth"
}