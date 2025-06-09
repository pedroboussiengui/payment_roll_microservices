import { get } from "svelte/store";
import { accessToken } from "./store/auth";
import { jwtDecode } from "jwt-decode";

export interface JwtPayload {
    sub: string,
    type: string,
    name: string,
    contract_id: string,
    exp: number,
    iat: number
}

export function decodeJwt(): JwtPayload {
    const bearerToken = get(accessToken);
    if (!bearerToken) {
        throw new Error("No token found in store");
    }
    return jwtDecode(bearerToken);
}