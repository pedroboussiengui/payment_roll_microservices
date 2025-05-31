
export class IdentityProviderHttpGateway {
    private readonly baseUrl: string = 'http://localhost:8080'

    async setContract(contractId: string, partialToken: string) {
        const response = await fetch(`${this.baseUrl}/users/set-contract/${contractId}`, {
            method: 'GET',
            headers: {
                'Authorization': `${partialToken}`,
                'Content-Type': 'application/json',
            }
        });
        if (response.status === 401) {
            throw new Error("Unauthorized");
        }
        const data = await response.json()
        return data
    }

    async logout(refreshToken: string) {
        await fetch(`${this.baseUrl}/logout`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${refreshToken}`,
                'Content-Type': 'application/json',
            }
        }); 
    }

    async refreshTokens(refreshToken: string) {
        const response = await fetch(`${this.baseUrl}/refresh-tokens`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                refreshToken: refreshToken
            })
        });
        if (response.status === 401) {
            let errorData;
            try {
                errorData = await response.json();
            } catch {
                errorData = { title: 'Unknown error', status: response.status };
            }
            throw {
                status: response.status,
                title: errorData.title,
                detail: errorData.detail
            }
        }
        console.log(response); 
        const data = await response.json()
        return data
    }
}

