<script lang="ts">
    import { onMount } from "svelte";

    onMount(() => {
        const token = localStorage.getItem("token");
        if (!token) {
            window.location.href = "http://localhost:8080/auth";
        }
    });
</script>

<slot />