<script lang="ts">
    import type { Contract, Employee } from "$lib/types";
	import { onMount } from 'svelte';
    import payroll from "$lib/services/axios/payroll";
    import Navbar from "$lib/components/navbar.svelte";
	import { page } from "$app/stores";
	import { Icon, Plus } from "svelte-hero-icons";

    let employee: Employee | null = null;
	let contracts: Contract[] = [];
    let error: string | null = null;
    
    $: employeeId = $page.params.id;

    onMount(async () => {
        try {
            console.log(employeeId);
            const res = await payroll.get(`/employees/${employeeId}`);
            employee = res.data;
			const resContracts = await payroll.get(`/employees/${employeeId}/contracts`);
			contracts = resContracts.data;
        } catch(err: any) {
            error = err.message;
            console.log(err);
        }
    });

	$: hasContract = contracts.length > 0

	async function admissionEvent(employeeId: string) {
		console.log(`New admission event for employee ${employeeId}`);
	}
</script>

<Navbar />

{#if error}
	<p class="text-red-600 font-semibold p-4">{error}</p>
{:else if !employee}
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
		</div>/
	</fieldset>

	<fieldset class="border border-gray-300 rounded-lg p-6 m-2">
		<legend class="text-lg font-semibold text-gray-700 px-2">Job contracts</legend>

		<button 
			on:click={() => admissionEvent(employeeId)}
			class="flex items-center gap-x-2 bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded">
			<Icon src="{Plus}" solid size="24" /> Add job contract
		</button>
		
		{#if !hasContract}
			<p>No job contract was registred</p>
		{:else}
			{#each contracts as contract}
				<p>{contract.matricula} - {contract.contractType} - {contract.position} - {contract.function} - {contract.entryDate}</p>
			{/each}
		{/if}
	</fieldset>
{/if}