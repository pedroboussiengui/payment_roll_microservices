<script lang="ts">
    import { onMount } from "svelte";
    import { decodeJwt } from "$lib/utils/jwt";
    import { goto } from "$app/navigation";
    import { IdentityProviderHttpGateway } from "$lib/infra/http/IdentityProviderHttpGateway";
    import type { Contract, SessionTokens } from "$lib/utils/types";
    import { TokenStorage } from "$lib/infra/storage/TokenStorage";
    import { ListUserContracts } from "$lib/application/employee/ListEmployee";
    
    const identityProviderGateway = new IdentityProviderHttpGateway();

    const listUserContracts = new ListUserContracts()

    let token: string | null = null;

    let contracts: Contract[] = []

    let sessionTokens: SessionTokens;

    onMount(async () => {
        token = TokenStorage.getAccessToken() ?? TokenStorage.getPartialToken();

        const payload = decodeJwt(token!!)
        const userId = payload.sub;
        try {
            contracts = await listUserContracts.execute(userId, token!!);
        } catch (error) {
            console.error("Error fetching contracts:", error);
            window.location.href = 'http://localhost:8080/auth';
        }
    });

    async function setContract(contractId: string) {
        try {
            sessionTokens = await identityProviderGateway.setContract(contractId, token!!);
            TokenStorage.removePartialToken();
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
