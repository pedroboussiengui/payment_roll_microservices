<script lang="ts">
    import { goto } from "$app/navigation";
    import { accessToken } from "$lib/store/auth";
    import { onMount } from "svelte";

    onMount(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const token = urlParams.get("token");

        console.log(token);
        
        if (token) {
            console.log('Saving token...');
            accessToken.set(token);
            goto("/contracts");
        } else {
            console.log("erro ao obter token no callback")
            window.location.href = 'http://localhost:8080/auth';
        }
    });
</script>
