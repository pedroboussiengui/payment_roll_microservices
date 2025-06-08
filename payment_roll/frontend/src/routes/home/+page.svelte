<script lang="ts">
    import { onMount } from 'svelte';
    import { decodeJwt } from '$lib/utils/jwt';
    import Navbar from '../../components/navbar.svelte';
    import { TokenStorage } from '$lib/infra/storage/TokenStorage';

    let username: string | null = null;

    onMount(() => {
        try {
            // console.log("entrei na home")
            // console.log(TokenStorage.getAccessToken());
            const payload = decodeJwt()
            username = payload.name;
        } catch(err) {
            window.location.href = 'http://localhost:8080/auth';
        } 
    });
</script>

<Navbar />

<h1>Home</h1>

<div>
    <p>Olá, {username}!</p>
</div>
