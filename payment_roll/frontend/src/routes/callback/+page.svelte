<script lang="ts">
    import { goto } from "$app/navigation";
    import { onMount } from "svelte";

    onMount(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const token = urlParams.get("token");

        if (token) {
            localStorage.setItem("token", token);
            goto("/home")
        } else {
            console.log("erro ao obter token no callback")
            window.location.href = 'http://localhost:8080/auth';
        }
    });
</script>