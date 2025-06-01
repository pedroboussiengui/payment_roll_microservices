<script lang="ts">
    import { redirect } from "@sveltejs/kit";

    export const load = () => {
        throw redirect(302, "/home");
    }
</script>