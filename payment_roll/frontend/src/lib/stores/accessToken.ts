import { decodeJwtExplicit } from "$lib/utils/jwt";
import { writable } from "svelte/store";

function createAccessTokenStore() {
    const { subscribe, set } = writable<string | null>(null);

    let currentValue: string | null = null;

    function loadFromStorage() {
        const accessToken = localStorage.getItem('accessToken');
        const expiresAt = Number(localStorage.getItem('accessTokenExpiresAt'));
        const now = Date.now();

        if (!accessToken || now > expiresAt) {
            clear();
            set(null);
        } else {
            set(accessToken);
        }
    }

    function update(accessToken: string) {
        const payload = decodeJwtExplicit(accessToken);
        if (!payload.exp) {
            console.warn('JWT inválido: propriedade "exp" ausente');
            clear();
            return;
        }
        const expiresAtMs = payload.exp * 1000;

        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('accessTokenExpiresAt', expiresAtMs.toString());
        set(accessToken);
    }


    function clear() {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('accessTokenExpiresAt');
        set(null)
    }

    subscribe((value) => {
        currentValue = value;
    });

    return {
        subscribe,
        set: update,
        clear,
        check: loadFromStorage,
        get: () => currentValue
    }
}

export const accessToken = createAccessTokenStore();