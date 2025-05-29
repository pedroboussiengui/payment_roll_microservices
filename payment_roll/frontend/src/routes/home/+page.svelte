<script lang="ts">
    import { onMount } from 'svelte';

    let userToken: string | null = null;

    onMount(() => {
        userToken = localStorage.getItem('token');

        if (!userToken) {
            window.location.href = 'http://localhost:8080/auth';
        }
    });
</script>

<h1>Home</h1>

<div>
    <p>{userToken}</p>
</div>