<script lang="ts">
    import { goto } from "$app/navigation";
    // import { TokenStorage } from "$lib/infra/storage/TokenStorage";
    import { accessToken } from "$lib/stores/accessToken";
    import { authn } from "$lib/stores/authn";
    import { onMount } from "svelte";

    onMount(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const token = urlParams.get("token");

        console.log(token);
        
        if (token) {
            accessToken.set(token);
            authn.login();
            goto("/contracts");
        } else {
            console.log("erro ao obter token no callback")
            window.location.href = 'http://localhost:8080/auth';
        }
    });
</script>

