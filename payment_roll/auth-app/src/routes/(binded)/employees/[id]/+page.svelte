<script lang="ts">
    import type { Employee } from "$lib/types";
	import { onMount } from 'svelte';
    import payroll from "$lib/services/axios/payroll";
    import Navbar from "$lib/components/navbar.svelte";
	import { page } from "$app/stores";

    let employee: Employee | null = null;
    let error: string | null = null;
    
    $: employeeId = $page.params.id;

    onMount(async () => {
        try {
            console.log(employeeId);
            const res = await payroll.get(`/employees/${employeeId}`);
            employee = res.data;
        } catch(err: any) {
            error = err.message;
            console.log(err);
        }
    });
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
				<p class="text-sm text-gray-500 font-medium">Name</p>
				<p class="text-base text-gray-800">Pedro henrique wilfride de lima boussiengui</p>
			</div>
			<div>
				<p class="text-sm text-gray-500 font-medium">Document</p>
				<p class="text-base text-gray-800">{employee.document}</p>
			</div>
			<div>
				<p class="text-sm text-gray-500 font-medium">Birth Date</p>
				<p class="text-base text-gray-800">{employee.birthDate}</p>
			</div>
		</div>
	</fieldset>
{/if}