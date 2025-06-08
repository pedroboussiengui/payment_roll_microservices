import { TokenStorage } from "$lib/infra/storage/TokenStorage";
import { accessToken } from "$lib/stores/accessToken";
import { jwtDecode } from "jwt-decode";
import { get } from "svelte/store";

export interface JwtPayload {
    sub: string,
    type: string,
    name: string,
    contract_id: string,
    exp: number,
    iat: number
}

export function decodeJwt(): JwtPayload {
    // console.log("decoding the token");
    // const accessToken = TokenStorage.getAccessToken() ?? TokenStorage.getToken();

    // if (accessToken) {
    //     if (TokenStorage.getAccessToken()) {
    //         console.log("Token lido do COOKIE (accessToken)");
    //     } else {
    //         console.log("Token lido do LOCALSTORAGE (fallback)");
    //     }
    // } else {
    //     console.log("Nenhum token encontrado!");
    // }

    // accessToken.check();
    const bearerToken = get(accessToken);
    if (!bearerToken) {
        throw new Error("No access token found");
    }
    // console.log("token found");
    return jwtDecode(bearerToken);
}

export function decodeJwtExplicit(jwtToken: string): JwtPayload {;
    return jwtDecode(jwtToken);
}