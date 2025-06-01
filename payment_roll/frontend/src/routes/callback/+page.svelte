<script lang="ts">
    import { goto } from "$app/navigation";
    import { TokenStorage } from "$lib/infra/storage/TokenStorage";
    import { onMount } from "svelte";

    onMount(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const accessToken = urlParams.get("token");

        if (accessToken) {
            TokenStorage.setAccessToken(accessToken);
            goto("/contracts");
        } else {
            console.log("erro ao obter token no callback")
            window.location.href = 'http://localhost:8080/auth';
        }
    });
</script>