<script lang="ts">
    import { onMount } from "svelte";
    import { decodeJwt } from "$lib/utils/jwt";
    import { goto } from "$app/navigation";
    import type { Contract, SessionTokens } from "$lib/utils/types";
    // import { TokenStorage } from "$lib/infra/storage/TokenStorage";
    import payroll from "$lib/payroll_api";
    import idp from "$lib/idp_api";
    import { accessToken } from "$lib/stores/accessToken";
    import { get } from "svelte/store";

    let contracts: Contract[] = []

    onMount(async () => {
        // accessToken.check();
        console.log("contract");
        console.log(`accessToken: ${get(accessToken)}`);
        try {
            const payload = decodeJwt()
            const userId = payload.sub;
            const res = await payroll.get(`/employees/${userId}/contracts`)
            contracts = res.data as Contract[];
        } catch (error) {
            console.error("Error fetching contracts:", error);
            // window.location.href = 'http://localhost:8080/auth';
        }
    });

    async function setContract(contractId: string) {
        try {
            console.log("setando contrato");
            const res = await idp.post(`/tokens/${contractId}`);
            const sessionTokens = res.data as SessionTokens;
            // TokenStorage.setSession(sessionTokens.sessionId);
            // TokenStorage.removeToken();
            accessToken.set(sessionTokens.accessToken);
            console.log("redirecionando para home");
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
