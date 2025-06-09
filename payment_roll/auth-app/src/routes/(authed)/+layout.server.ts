import { redirect } from "@sveltejs/kit";

export async function load( { cookies, url }: { cookies: any, url: any } ) {
    const session = cookies.get('isAuth')
    const path = url.pathname;

    console.log(`session: ${session} e path: ${path}`)

    if (!session) {
        throw redirect(302, 'http://localhost:8080/auth');
    }

    return {}
}