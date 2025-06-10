import { refreshToken } from "$lib/services/auth";
import { accessToken } from "$lib/store/auth";
import { redirect, type Cookies } from "@sveltejs/kit";
import { get } from "svelte/store";

export async function load( { cookies }: { cookies: Cookies } ) {
    const session = cookies.get('isAuth')

    console.log(`session: ${session}`)

    if (!session) {
        const token = get(accessToken);
        if (!token) {
            console.log("Getting a new accessToken");
            const newAccessToken = await refreshToken();
            if (!newAccessToken) {
                throw redirect(302, 'http://localhost:8080/auth');
            }
        }
    }

    if (session === '1') {
        throw redirect(302, '/contracts');
    }

    return {}
}