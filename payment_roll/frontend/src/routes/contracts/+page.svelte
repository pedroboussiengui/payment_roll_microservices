<script lang="ts">
    import { onMount } from "svelte";
    import { PayrollHttpGateway } from "../../infra/PayrollHttpGateway";
    import { decodeJwt } from "$lib/utils/jwt";
    import { goto } from "$app/navigation";
    import { IdentityProviderHttpGateway } from "../../infra/IdentityProviderHttpGateway";
    
    const payrollHttpGateway = new PayrollHttpGateway();
    const identityProviderGateway = new IdentityProviderHttpGateway();

    let userToken: string | null = null;

    type Contract = {
        id: string,
        matricula: string,
        entryDate: string,
        contractType: string,
        position: string,
        function: string,
        department: string,
    }
    
    let contracts: Contract[] = []

    type SetContractResponse = {
        sessionId: string,
        accessToken: string,
        refreshToken: string
    }
    let sessionTokens: SetContractResponse;

    onMount(async () => {
        userToken = localStorage.getItem("accessToken")
        if (!userToken) {
            userToken = localStorage.getItem('token');
        }
        if (!userToken) {
            window.location.href = 'http://localhost:8080/auth';
        }
        const payload = decodeJwt(userToken!!)
        const userId = payload.sub;
        try {
            contracts = await payrollHttpGateway.listAll(userId, userToken!!);
        } catch (error) {
            console.error("Error fetching contracts:", error);
            window.location.href = 'http://localhost:8080/auth';
        }
    });

    async function setContract(contractId: string) {
        try {
            sessionTokens = await identityProviderGateway.setContract(contractId, userToken!!);
            localStorage.setItem("accessToken", sessionTokens.accessToken);
            localStorage.setItem("refreshToken", sessionTokens.refreshToken);
            localStorage.setItem("sessionId", sessionTokens.sessionId);
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
