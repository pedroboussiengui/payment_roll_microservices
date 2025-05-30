<script lang="ts">
    import { IdentityProviderHttpGateway } from "../infra/ContractHttpGateway";

    const identityProviderGateway = new IdentityProviderHttpGateway();

    async function logout() {
        const refreshToken = localStorage.getItem('refreshToken');
        if (!refreshToken) {
            window.location.href = 'http://localhost:8080/auth';
        }
        await identityProviderGateway.logout(refreshToken!!);
        localStorage.removeItem('token');
        localStorage.removeItem('accessToken');
        localStorage.removeItem('sessionId');
        localStorage.removeItem('refreshToken');
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