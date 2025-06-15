<script lang="ts">
    import { page } from "$app/stores";
    import { clickOutside } from "$lib/actions/clickOutside";
    import AfastamentoModal from "$lib/components/AfastamentoModal.svelte";
    import Navbar from "$lib/components/navbar.svelte";
    import payroll from "$lib/services/axios/payroll";
    import type { Employee, EmployeeContract, ContractEvent } from "$lib/types";
    import { onMount } from "svelte";
    import { Icon, EllipsisVertical } from "svelte-hero-icons";
    
    $: employeeId = $page.params.id;
    $: contractId = $page.params.contractId;

    let employee: Employee | null = null;
    let contract: EmployeeContract | null = null;
    let events: ContractEvent[] = []
    let error: string | null = null;

    onMount(async () => {
        try {
            const employeeRes = await payroll.get(`/employees/${employeeId}`);
            employee = employeeRes.data;
			const contractRes = await payroll.get(`/employees/${employeeId}/contracts/${contractId}`);
			contract = contractRes.data;
            getContractEvents()
        } catch(err: any) {
            error = err.message;
            console.log(err);
        }
    });

    async function getContractEvents() {
        await payroll.get(`/employees/${employeeId}/contracts/${contractId}/events`)
            .then((res) => {
                events = res.data;
                console.log('Eventos');
                console.log(res.data);
            })
            .catch((err) => {
                console.log(err.response.data);
            });
    }

    let showEventsMenu = false;

    function toogleEventsMenu() {
        showEventsMenu = !showEventsMenu;
    }

    function handleAction(event: string) {
        console.log("Ação:", event);
        if (event === "Afastamento") {
            isModalOpen = true;
        }
        showEventsMenu = false;
    }

    let isModalOpen = false;
</script>

<Navbar />

{#if error}
	<p class="text-red-600 font-semibold p-4">{error}</p>
{:else if !employee || !contract}
	<p class="p-4 text-gray-500">Loading...</p>
{:else}
    <h2 class="p-4 text-2xl font-bold mb-4 text-gray-800">{employee.name}</h2>

    <fieldset class="border border-gray-300 rounded-lg p-6 m-2">
        <legend class="text-lg font-semibold text-gray-700 px-2">Personal informations</legend>

        <div class="grid grid-cols-1 sm:grid-cols-3 gap-6">
            <div>
                <p class="text-sm text-gray-500 font-medium">Name</p>
                <p class="text-base text-gray-800">{employee.name}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500 font-medium">Document</p>
                <p class="text-base text-gray-800">{employee.document}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500 font-medium">Birth Date</p>
                <p class="text-base text-gray-800">{employee.birthDate}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500 font-medium">Identity number</p>
                <p class="text-base text-gray-800">{employee.identity}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500 font-medium">Marial status</p>
                <p class="text-base text-gray-800">{employee.maritalStatus}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500 font-medium">Gender</p>
                <p class="text-base text-gray-800">{employee.gender}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500 font-medium">Mother name</p>
                <p class="text-base text-gray-800">{employee.motherName}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500 font-medium">Father name</p>
                <p class="text-base text-gray-800">{employee.fatherName}</p>
            </div>
        </div>
    </fieldset>
    
    <fieldset class="border border-gray-300 rounded-lg p-6 m-2 relative">
		<legend class="text-lg font-semibold text-gray-700 px-2">Job contract details</legend>
        
        <div class="absolute top-4 right-4 z-10">
            <button
                on:click={() => toogleEventsMenu()}
                class="p-2 rounded hover:bg-gray-100"
                aria-haspopup="true"
                aria-expanded={showEventsMenu}
            ><Icon src="{EllipsisVertical}" solid size="16" />
            </button>
            {#if showEventsMenu}
                <div 
                    use:clickOutside={() => (showEventsMenu = false)} 
                    class="absolute right-0 mt-2 w-40 bg-white border border-gray-200 rounded shadow-md"
                >
                    <div class="py-1">
                        {#each contract.possibleEvents as event}
                            <button 
                                class="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                                on:click={() => handleAction(event)}
                            >{event}</button>
                        {/each}
                    </div>
                </div>
            {/if}
        </div>

        <div class="flex space-x-2">
            <div>
                <p class="text-sm text-gray-500 font-medium">Matricula</p>
                <p class="text-base text-gray-800">{contract.matricula}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500 font-medium">Tipo de contrato</p>
                <p class="text-base text-gray-800">{contract.contractType}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500 font-medium">Status</p>
                <p class="text-base text-gray-800">{contract.contractState}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500 font-medium">Data de entrada</p>
                <p class="text-base text-gray-800">{contract.entryDate}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500 font-medium">Data de exercício</p>
                <p class="text-base text-gray-800">{contract.entryDate}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500 font-medium">Departamento</p>
                <p class="text-base text-gray-800">{contract.department}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500 font-medium">Cargo</p>
                <p class="text-base text-gray-800">{contract.position}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500 font-medium">Função</p>
                <p class="text-base text-gray-800">{contract.function}</p>
            </div>
        </div>
    </fieldset>

    <fieldset class="border border-gray-300 rounded-lg p-6 m-2 relative">
		<legend class="text-lg font-semibold text-gray-700 px-2">Contract events</legend>
        
        {#each events as event}
            {#if event.eventType === "Admission"}
                <p>{event.eventType}</p>
                <p>{event.createdAt}</p>
                <p>{event.exerciseDate}</p>
            {:else if event.eventType === "Afastamento"}
                <p>{event.eventType}</p>
                <p>{event.createdAt}</p>
                <p>{event.reason}</p>
            {/if}
        {/each}

    </fieldset>

    <AfastamentoModal 
        isOpen={isModalOpen}
        employeeId={employeeId}
        contractId={contractId}
        on:confirm={(result) => {
            console.log("resultado" + result.detail);
            if (result.detail) {
                getContractEvents();
            }
            isModalOpen = false;
        }}
        on:cancel={() => (isModalOpen = false)}
    />
{/if}
