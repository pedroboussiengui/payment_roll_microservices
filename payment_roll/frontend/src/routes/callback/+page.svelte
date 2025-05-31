<script lang="ts">
    import { goto } from "$app/navigation";
    import { TokenStorage } from "$lib/infra/storage/TokenStorage";
    import { onMount } from "svelte";

    onMount(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const token = urlParams.get("token");

        if (token) {
            TokenStorage.setPartialToken(token);
            goto("/contracts");
        } else {
            console.log("erro ao obter token no callback")
            window.location.href = 'http://localhost:8080/auth';
        }
    });
</script>