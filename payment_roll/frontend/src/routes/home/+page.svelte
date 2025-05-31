<script lang="ts">
    import { onMount } from 'svelte';
    import { decodeJwt } from '$lib/utils/jwt';
    import Navbar from '../../components/navbar.svelte';
    import { TokenStorage } from '$lib/infra/storage/TokenStorage';

    let username: string | null = null;

    onMount(() => {
        const accessToken = TokenStorage.getAccessToken();
        if (!accessToken) {
            window.location.href = 'http://localhost:8080/auth';
            return
        }
        const payload = decodeJwt(accessToken!!)
        username = payload.name;
    });
</script>

<Navbar />

<h1>Home</h1>

<div>
    <p>Olá, {username}!</p>
</div>
