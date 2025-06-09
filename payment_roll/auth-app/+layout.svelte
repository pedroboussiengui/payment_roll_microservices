<!-- <script lang="ts">
    import { sessionStatus } from "$lib/store/auth";
    import { onMount, tick } from "svelte";
    import { get } from "svelte/store";

    let hydrated = false;

    onMount(async () => {
        await tick();
        const session = get(sessionStatus);
        if (session !== Session) {
            window.location.href = "http://localhost:8080/auth"in
        }
    });
</script> -->