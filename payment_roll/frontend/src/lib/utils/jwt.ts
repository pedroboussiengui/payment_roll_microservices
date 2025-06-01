import { TokenStorage } from "$lib/infra/storage/TokenStorage";
import { jwtDecode } from "jwt-decode";

export interface JwtPayload {
    sub: string,
    type: string,
    name: string,
    contract_id: string
}

export function decodeJwt(): JwtPayload {
    const accessToken = TokenStorage.getAccessToken();
    if (!accessToken) {
        throw new Error("No access token found");
    }
    return jwtDecode(accessToken);
}