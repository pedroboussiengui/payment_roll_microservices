<!-- <script lang="ts">
    import { page } from "$app/stores";

    $: loggedIn = $page.data.session?.loggedIn;
</script>

{#if loggedIn}
    <slot />
{:else}
	<p>Redirecionando...</p>
{/if} -->


<script lang="ts">
    import { TokenStorage } from "$lib/infra/storage/TokenStorage";
    import { onMount } from "svelte";

    onMount(() => {
        const token = TokenStorage.getAccessToken() ?? TokenStorage.getToken();
        if (!token) {
            window.location.href = "http://localhost:8080/auth";
        }
    });
</script>

<slot />