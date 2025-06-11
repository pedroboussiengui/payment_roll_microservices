<script lang="ts">
    import { refreshToken } from "$lib/services/auth";
    import { accessToken, tokenReady } from "$lib/store/auth";
    import { onMount } from "svelte";
    import { get } from "svelte/store";

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

<slot />

<!-- {#if $tokenReady}
	<slot />
{:else}
	<p>Carregando...</p>
{/if} -->


