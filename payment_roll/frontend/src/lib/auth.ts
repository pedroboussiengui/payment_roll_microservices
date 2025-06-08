import idp from "./idp_api";
// import { TokenStorage } from "./infra/storage/TokenStorage";
import { accessToken } from "./stores/accessToken";
import { authn } from "./stores/authn";

export function logout() {
    idp.post("/logout")
        .then(res => {
            console.log(res)
        })
        .catch(err => {
            console.log(err)
        })
    authn.logout();
    accessToken.clear();
    window.location.href = "http://localhost:8080/auth"
}