<script lang="ts">
  import { goto } from "$app/navigation";
    import Navbar from "$lib/components/navbar.svelte";
    import payroll from "$lib/services/axios/payroll";
    import type { Employee } from "$lib/types";
    import { onMount } from "svelte";
    import { Icon, Eye, Plus } from "svelte-hero-icons";

    let employees: Employee[] = []

    onMount(async () => {
        try {
            const res = await payroll.get('/employees')
            employees = res.data as Employee[];
        } catch (err: any) {
            console.error('Erro ao carregar funcionários:', err);
            console.log(err.response.data);
        }
    });

    function detailEmployee(employeeId: string) {
        console.log(`Mostrando detalhes do funcionário com ID: ${employeeId}`);
        goto(`/employees/${employeeId}`);
    }

    function addEmployee() {    
        console.log('Adicionando novo funcionário');
        goto('/employees/register');
    }
</script>

<Navbar />

<div class="flex justify-between items-center pr-4">
    <h2 class="p-4 text-2xl font-bold mb-4 text-gray-800">Employees registries</h2>
    <button 
        on:click={() => addEmployee()}
        class="flex items-center gap-x-2 bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded">
        <Icon src="{Plus}" solid size="24" /> Add employee
    </button>
</div>

<table class="min-w-full table-auto border border-gray-300">
    <thead>
        <tr class="bg-gray-100">
            <th class="border border-gray-300 px-4 py-2 text-left font-semibold text-gray-700">
                Name
            </th>
            <th class="border border-gray-300 px-4 py-2 text-left font-semibold text-gray-700">
                Document
            </th>
            <th class="border border-gray-300 px-4 py-2 text-left font-semibold text-gray-700">
                Actions
            </th>
        </tr>
    </thead>
    <tbody>
        {#each employees as employee}
            <tr>
                <td class="border border-gray-300 px-4 py-2">{employee.name}</td>
                <td class="border border-gray-300 px-4 py-2">{employee.document}</td>
                <td class="border border-gray-300 px-4 py-2">
                    <button
                        class="text-blue-600 hover:text-blue-800"
                        on:click={() => detailEmployee(employee.id)}
                        title="View employee details">
                        <Icon src="{Eye}" solid size="16" />
                    </button>
                </td>
            </tr>
        {/each}
    </tbody>
</table>