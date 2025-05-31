export class TokenStorage {

    static getAccessToken(): string | null {
        return localStorage.getItem('access_token');
    }

    static getRefreshToken(): string | null {
        return localStorage.getItem('refresh_token');
    }


    static setTokens({ accessToken, refreshToken, sessionId }: { accessToken: string, refreshToken: string, sessionId: string }): void {
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
        localStorage.setItem('sessionId', sessionId);
    }

    static clearTokens() {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('sessionId');
        localStorage.removeItem('token');
    }
}