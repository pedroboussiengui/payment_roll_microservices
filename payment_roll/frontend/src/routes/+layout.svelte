<script lang="ts">
  import { TokenStorage } from "$lib/infra/storage/TokenStorage";
    import { onMount } from "svelte";

    onMount(() => {
        const token = TokenStorage.getPartialToken();
        if (!token) {
            window.location.href = "http://localhost:8080/auth";
        }
    });
</script>

<slot />