import type { Handle } from "@sveltejs/kit";

export const handle: Handle = async ({event, resolve}) => {
    console.log("✅ Hook executado");
    
    const response = await resolve(event, {
        filterSerializedResponseHeaders(name) {
            return name === 'content-type'
        },
    });

    response.headers.set(
        'Content-Security-Policy',
        [
            "default-src 'self'",
            "script-src 'self' 'unsafe-inline'",
            "object-src 'none'",
            "style-src 'self' 'unsafe-inline'",
            "base-uri 'self'",
            "connect-src 'self' http://localhost:8081 http://localhost:8080", // libera IDP e backend
            "img-src 'self'",
            "frame-ancestors 'none'",
            "form-action 'self' http://localhost:8080", // permite redirecionamentos via POST/form
            "frame-src http://localhost:8080",          // permite o uso de iframes do IDP, se houver
        ].join('; ')
    );

    return response;
};