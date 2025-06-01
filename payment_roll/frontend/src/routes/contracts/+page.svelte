<script lang="ts">
    import { onMount } from "svelte";
    import { decodeJwt } from "$lib/utils/jwt";
    import { goto } from "$app/navigation";
    import type { Contract, SessionTokens } from "$lib/utils/types";
    import { TokenStorage } from "$lib/infra/storage/TokenStorage";
    import payroll from "$lib/payroll_api";
    import idp from "$lib/idp_api";

    let contracts: Contract[] = []

    onMount(async () => {
        try {
            const payload = decodeJwt()
            const userId = payload.sub;
            // contracts = await listUserContracts.execute(userId, token!!);
            const res = await payroll.get(`/employees/${userId}/contracts`)
            contracts = res.data as Contract[];
        } catch (error) {
            console.error("Error fetching contracts:", error);
            window.location.href = 'http://localhost:8080/auth';
        }
    });

    async function setContract(contractId: string) {
        try {
            const res = await idp.get(`/users/set-contract/${contractId}`);
            const sessionTokens = res.data as SessionTokens;
            TokenStorage.setTokens(sessionTokens);
            goto("/home")
        } catch (error) {
            console.error("Error setting contract:", error);
            window.location.href = 'http://localhost:8080/auth';
        }
    }
</script>

<h1>Choose one of your contract</h1>

{#each contracts as contract}
    <div class="flex flex-col gap-2">
        <button on:click={() => setContract(contract.id)}>
            {contract.matricula} - {contract.position} - {contract.department}
        </button>
    </div>
{/each}
