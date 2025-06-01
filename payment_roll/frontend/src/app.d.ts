// See https://svelte.dev/docs/kit/types#app.d.ts
// for information about these interfaces
declare global {
	namespace App {
		// interface Error {}
		// interface Locals {}
		// interface PageData {}
		// interface PageState {}
		// interface Platform {}
	}
}

export {};

/**
 * CSP Directives
 * - default-src 'self': JS, CSS, images devem vir do mesmo domain http://localhost, impedindo o carregamento de scripts externos.
 * - script-src 'self': só permite execução de JS do mesmo dominio
 * - object-src 'none': impede uso de <object>, <embed> e <applet>
 * - style-src 'self' 'unsafe-inline': só permite CSS do mesmo domain, mas permite inline styles
 * - base-uri 'self': protege contra redirecionamentos
 * - connect-src 'self' http://localhost:8081: permite fetch(), WebSocket() e axios() apenas para mesmo domain ou para o url
 * - img-src 'self' https://images.unsplash.com:  permite carregar imagens apenas do próprio domain ou da url
 * - frame-ancestors 'none': impede que outros sites carreguem dentro do seu <iframe> (clickjacking)
 * 
 */

// """
// Content-Security-Policy:
//   default-src 'self';
//   script-src 'self';
//   style-src 'self' https://fonts.googleapis.com;
//   font-src 'self' https://fonts.gstatic.com;
//   object-src 'none';
//   connect-src 'self' http://localhost:8081;
//   img-src 'self';
//   frame-ancestors 'none';
//   base-uri 'self';
// """

