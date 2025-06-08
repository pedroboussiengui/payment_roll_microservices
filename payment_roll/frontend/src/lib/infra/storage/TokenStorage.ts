// export class TokenStorage {

//     static getAccessToken(): string | null {
//         return localStorage.getItem('accessToken');
//     }

//     static getRefreshToken(): string | null {
//         return localStorage.getItem('refreshToken');
//     }

//     // static getPartialToken(): string | null {
//     //     return localStorage.getItem('partialToken');
//     // }

//     static setAccessToken(accessToken: string): void {
//         localStorage.setItem('accessToken', accessToken);
//     }

//     // static removePartialToken(): void {
//     //     localStorage.removeItem('partialToken');
//     // }

//     static setTokens({ accessToken, refreshToken, sessionId }: { accessToken: string, refreshToken: string, sessionId: string }): void {
//         localStorage.setItem('accessToken', accessToken);
//         localStorage.setItem('refreshToken', refreshToken);
//         localStorage.setItem('sessionId', sessionId);
//     }

//     static clearTokens() {
//         localStorage.removeItem('accessToken');
//         localStorage.removeItem('refreshToken');
//         localStorage.removeItem('sessionId');
//         // localStorage.removeItem('partialToken');
//     }
// }

export class TokenStorage {

    static getAccessToken(): string | null {
        return document.cookie
            .split(";")
            .find(row => row.startsWith('accessToken'))
            ?.split("=")[1] ?? null;
    }

    // static isAuthenticated(): boolean {
    //     const cookie = document.cookie
    //         .split(';')
    //         .find(c => c.startsWith('isAuthenticated'));
    //     return cookie ? cookie.split('=')[1] === 'true' : false;
    // }

    // static isAuthenticated2(): string {
    //     const cookie = document.cookie
    //         .split(';')
    //         .find(c => c.startsWith('isAuthenticated'))
    //         ?.split('=')[1];
    //     if (cookie === 'true') {
    //         return 'true';
    //     } else {
    //         return 'false';
    //     }
    // }

    static setSession(sessionId: string): void {
        localStorage.setItem('sessionId', sessionId);
    }
    
    static setToken(token: string): void {
        localStorage.setItem('token', token);
    }

    static getToken(): string | null {
        return localStorage.getItem('token');
    }

    static removeToken(): void {
        localStorage.removeItem('token');
    }

    static clear(): void {
        localStorage.removeItem('sessionId');
        localStorage.removeItem('token')
        TokenStorage.deleteCookie('accessToken');
    }

    private static deleteCookie(name: string): void {
        document.cookie = `${name}=; Max-Age=0; path=/`;
    }
}