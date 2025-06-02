import { TokenStorage } from "$lib/infra/storage/TokenStorage";
import { jwtDecode } from "jwt-decode";

export interface JwtPayload {
    sub: string,
    type: string,
    name: string,
    contract_id: string
}

export function decodeJwt(): JwtPayload {
    console.log("decoding the token");
    const accessToken = TokenStorage.getAccessToken() ?? TokenStorage.getToken();

    if (accessToken) {
        if (TokenStorage.getAccessToken()) {
            console.log("Token lido do COOKIE (accessToken)");
        } else {
            console.log("Token lido do LOCALSTORAGE (fallback)");
        }
    } else {
        console.log("Nenhum token encontrado!");
    }

    if (!accessToken) {
        throw new Error("No access token found");
    }
    console.log("token found");
    return jwtDecode(accessToken);
}