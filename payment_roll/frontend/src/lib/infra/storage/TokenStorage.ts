export class TokenStorage {

    static getAccessToken(): string | null {
        return localStorage.getItem('accessToken');
    }

    static getRefreshToken(): string | null {
        return localStorage.getItem('refreshToken');
    }

    static getPartialToken(): string | null {
        return localStorage.getItem('partialToken');
    }

    static setPartialToken(partialToken: string): void {
        localStorage.setItem('partialToken', partialToken);
    }

    static removePartialToken(): void {
        localStorage.removeItem('partialToken');
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
        localStorage.removeItem('partialToken');
    }
}