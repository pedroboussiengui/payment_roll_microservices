<script lang="ts">
    import { onMount } from "svelte";
    import { ContractHttpGateway } from "../../infra/ContractHttpGateway";
    const gateway = new ContractHttpGateway();

    let userToken: string | null = null;

    type Contract = {
        id: string,
        matricula: string,
        position: string,
        department: string
    }
    
    let contracts: Contract[] = []

    onMount(async () => {
        userToken = localStorage.getItem('token');
        if (!userToken) {
            window.location.href = 'http://localhost:8080/auth';
        }
        try {
            contracts = await gateway.listAll();
        } catch (error) {
            console.error("Error fetching contracts:", error);
        }
    });

    function setContract(contractId: string) {
        console.log(contractId)
    }
</script>

Choose one of your contract
{#each contracts as contract}
    <div class="flex flex-col gap-2">
        <button on:click={() => setContract(contract.id)}>
            {contract.matricula} - {contract.position} - {contract.department}
        </button>
    </div>
{/each}