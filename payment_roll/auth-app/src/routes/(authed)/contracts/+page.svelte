<script lang="ts">
    import { decodeJwt } from "$lib/jwt";
    import { getTokens } from "$lib/services/auth";
    import payroll from "$lib/services/axios/payroll";
    import type { Contract } from "$lib/types";
    import { onMount } from "svelte";

    let contracts: Contract[] = []

    onMount(async () => {
        try {
            console.log("Fetching contracts...");
            const payload = decodeJwt()
            const userId = payload.sub;
            const res = await payroll.get(`/employees/${userId}/contracts`)
            contracts = res.data;
        } catch (err) {
            console.error("Error fetching contracts:", err);
        }
    });
</script>

<h1>Choose one of your contract</h1>

{#each contracts as contract}
    <div>
        <button on:click={() => getTokens(contract.id)}>
            {contract.matricula} - {contract.position} - {contract.department}
        </button>
    </div>
{/each}