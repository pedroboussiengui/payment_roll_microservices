import { writable } from "svelte/store";

const REFRESH_TOKEN_TTL = 120;

function createAuthnStore() {
    const { subscribe, set, update } = writable<boolean>(false);


    function loadFromStorage(): boolean {
        const isAuth = localStorage.getItem('isAuthenticated') === 'true';
        const expiresAt = Number(localStorage.getItem('isAuthenticatedExpiresAt'));
        const now = Date.now();

        if (!isAuth || !expiresAt || now > expiresAt) {
            clear();
            set(false)
            return false;
        } else {
            set(true);
            return true;
        }
    }

    function login() {
        const now = Date.now();
        localStorage.setItem('isAuthenticated', 'true');
        localStorage.setItem('isAuthenticatedExpiresAt', (now + REFRESH_TOKEN_TTL * 1000).toString());
        set(true);
    }

    function logout() {
        clear();
        set(false);
        window.location.href = 'http://localhost:8080/auth'
    }

    function clear() {
        localStorage.removeItem('isAuthenticated');
        localStorage.removeItem('isAuthenticatedExpiresAt');
    }

    return {
        subscribe,
        login,
        logout,
        check: loadFromStorage
    }
}

export const authn = createAuthnStore();