import { IdentityProviderHttpGateway } from "$lib/infra/http/IdentityProviderHttpGateway";
import { TokenStorage } from "$lib/infra/storage/TokenStorage";


export class RefreshToken {
    constructor(private readonly gateway = new IdentityProviderHttpGateway()) {}

    async execute(): Promise<void> {
        const refreshToken = TokenStorage.getRefreshToken();
        if (!refreshToken) throw new Error("No refresh token found");

        const tokens = await this.gateway.refreshTokens(refreshToken);
        TokenStorage.setTokens(tokens);
    }
}