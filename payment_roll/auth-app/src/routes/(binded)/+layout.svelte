<script lang="ts">
    import { refreshToken } from "$lib/services/auth";
    import { accessToken } from "$lib/store/auth";
    import { onMount } from "svelte";
    import { get, writable } from "svelte/store";

    const tokenReady = writable(false);

    onMount(async () => {
        console.log("Fetching the token...");
    
        const token = get(accessToken);

        if (!token) {
            console.log("Refresing the tokens...");
            await refreshToken();
        }
        tokenReady.set(true);
    });
</script>

{#if $tokenReady}
	<slot />
{:else}
	<p>Carregando...</p>
{/if}


