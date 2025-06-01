<script lang="ts">
    import { onMount } from 'svelte';
    import { decodeJwt } from '$lib/utils/jwt';
    import Navbar from '../../components/navbar.svelte';

    let username: string | null = null;

    onMount(() => {
        try {
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
