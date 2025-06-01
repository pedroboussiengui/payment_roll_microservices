import { TokenStorage } from "./infra/storage/TokenStorage";

export function logout() {
    TokenStorage.clearTokens()
    window.location.href = "'http://localhost:8080/auth"
}