import { jwtDecode } from "jwt-decode";

export interface JwtPayload {
    sub: string,
    type: string,
    name: string,
    contract_id: string
}

export function decodeJwt(token: string): JwtPayload {
    return jwtDecode(token);
}