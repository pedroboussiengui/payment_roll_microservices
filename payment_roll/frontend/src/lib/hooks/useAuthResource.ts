// import { TokenStorage } from "$lib/infra/storage/TokenStorage";
// import { writable, type Writable } from "svelte/store";
// import { RefreshToken } from '$lib/application/RefreshToken';

// export function useAuthResource<T, A extends any[]>(
//     fn: (accessToken: string, ...args: A) => Promise<T>,
//     ...args: A
// ): {
//     data: Writable<T | null>;
//     loading: Writable<boolean>;
//     error: Writable<any>;
//     reload: () => Promise<void>;
// }{
//     const data = writable<T | null>(null);
//     const loading = writable(true);
//     const error = writable<any>(null);

//     const refreshToken = new RefreshToken()

//     async function load() {
//         loading.set(true);
//         error.set(null);

//         let accessToken = TokenStorage.getAccessToken();
//         if (!accessToken) {
//             try {
//                 const sessionTokens = await refreshToken.execute();
//                 TokenStorage.setTokens(sessionTokens);
//                 accessToken = TokenStorage.getAccessToken();
//             } catch (e) {
//                 TokenStorage.clearTokens();
//                 window.location.href = 'http://localhost:8080/auth';
//                 return;
//             }
//         }
        
//         try {
//             const result = await fn(accessToken!!, ...args);
//             data.set(result);
//         } catch (e: any) {
//             if (e.status === 401) {
//                 try {
//                     const sessionTokens = await refreshToken.execute();
//                     TokenStorage.setTokens(sessionTokens);
//                     const accessToken = TokenStorage.getAccessToken();
//                     const result = await fn(accessToken!!, ...args);
//                     data.set(result);
//                 } catch (refreshError) {
//                     TokenStorage.clearTokens();
//                     window.location.href = 'http://localhost:8080/auth';
//                     error.set(refreshError);
//                 }
//             } else {
//                 error.set(e);
//             }
//         } finally {
//             loading.set(false);
//         }
//     }

//     load();

//     return {
//         data,
//         loading,
//         error,
//         reload: load
//     };
// }