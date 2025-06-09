import { redirect, type Cookies } from "@sveltejs/kit";

export async function load( { cookies }: { cookies: Cookies } ) {
    const session = cookies.get('isAuth')

    console.log(`session: ${session}`)

    if (!session) {
        throw redirect(302, 'http://localhost:8080/auth');
    }

    if (session === '1') {
        throw redirect(302, '/contracts');
    }

    return {}
}