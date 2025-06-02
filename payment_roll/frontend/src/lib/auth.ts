import idp from "./idp_api";
import { TokenStorage } from "./infra/storage/TokenStorage";

export function logout() {
    idp.post("/logout")
        .then(res => {
            console.log(res)
        })
        .catch(err => {
            console.log(err)
        })
    TokenStorage.clear()
    window.location.href = "http://localhost:8080/auth"
}