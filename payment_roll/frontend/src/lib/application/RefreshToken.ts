import { IdentityProviderHttpGateway } from "$lib/infra/http/IdentityProviderHttpGateway";
import { TokenStorage } from "$lib/infra/storage/TokenStorage";
import type { SessionTokens } from "$lib/utils/types";


export class RefreshToken {
    constructor(private readonly gateway = new IdentityProviderHttpGateway()) {}

    async execute(): Promise<SessionTokens> {
        const refreshToken = TokenStorage.getRefreshToken();
        if (!refreshToken) {
            throw new Error("No refresh token found");
        }

        return await this.gateway.refreshTokens(refreshToken);
    }
}