<script lang="ts">
    import { onMount } from 'svelte';
    import { decodeJwt } from '$lib/utils/jwt';
    import Navbar from '../../components/navbar.svelte';
    import { IdentityProviderHttpGateway } from '../../infra/IdentityProviderHttpGateway';
    
    const identityProviderGateway = new IdentityProviderHttpGateway()

    let username: string | null = null;

    onMount(() => {
        const userToken = localStorage.getItem('accessToken');

        if (!userToken) {
            window.location.href = 'http://localhost:8080/auth';
        }
        const payload = decodeJwt(userToken!!)
        username = payload.name;
    });


    type SessionTokens = {
        sessionId: string,
        accessToken: string,
        refreshToken: string
    }
    let sessionTokens: SessionTokens;


    async function refreshTokens() {
        const refreshToken = localStorage.getItem('refreshToken');

        if (!refreshToken) {
            window.location.href = 'http://localhost:8080/auth';
        }
        try {
            sessionTokens = await identityProviderGateway.refreshTokens(refreshToken!!)
            localStorage.setItem('accessToken', sessionTokens.accessToken);
            localStorage.setItem('refreshToken', sessionTokens.refreshToken);
            localStorage.setItem('sessionId', sessionTokens.sessionId);
        } catch (e) {
            localStorage.removeItem('token');
            localStorage.removeItem('accessToken');
            localStorage.removeItem('sessionId');
            localStorage.removeItem('refreshToken');
            window.location.href = 'http://localhost:8080/auth';
        }
    }
</script>

<h1>Home</h1>

<Navbar />

<div>
    <p>Olá, {username}!</p>
</div>

<button on:click={() => refreshTokens()}>
    Refresh tokens
</button>
