<script lang="ts">
    import { IdentityProviderHttpGateway } from "$lib/infra/http/IdentityProviderHttpGateway";
    import { TokenStorage } from "$lib/infra/storage/TokenStorage";

    const identityProviderGateway = new IdentityProviderHttpGateway();

    async function logout() {
        const refreshToken = TokenStorage.getRefreshToken();
        if (!refreshToken) {
            window.location.href = 'http://localhost:8080/auth';
        }
        await identityProviderGateway.logout(refreshToken!!);
        TokenStorage.clearTokens();
        window.location.href = 'http://localhost:8080/auth';
    }
</script>

<nav>
    <ol>
        <li><a href="/">Home</a></li>
        <li><a href="/employees">Employees</a></li>
    </ol>
</nav>

<div>
    <button on:click={() => logout()}>Logout</button>
</div>