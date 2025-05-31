
export async function withAuthRetry<T>(fn: (token: string) => Promise<T>, accessToken: string) {
    try {
        return await fn(accessToken);
    } catch (e) {
        if (e instanceof Error) {
            console.log(e);
        }
        // if (e instanceof Error && e.message === 'Unauthorized') {
        //     const newAccessToken = await refreshToken();
        //     return await fn(newAccessToken)
        // }
        throw e;
    }    
}