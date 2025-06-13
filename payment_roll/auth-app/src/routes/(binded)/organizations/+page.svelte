<script lang="ts">
  import { goto } from "$app/navigation";
    import Navbar from "$lib/components/navbar.svelte";
    import payroll from "$lib/services/axios/payroll";
    import type { Organization } from "$lib/types";
    import { onMount } from "svelte";
    import { Icon, Eye, Plus } from "svelte-hero-icons";

    let organizations: Organization[] = [];

    onMount(async () => {
        await payroll.get('/organizations')
            .then((res) => {
                console.log(res.data);
                organizations = res.data;
            })
            .catch((err) => {
                console.log(err);
            })
    });

    async function detailOrganization(id: string) {
        goto(`/organizations/${id}`)
    }

    async function addOrganization() {
        console.log('Creating');
    }
</script>

<Navbar/>

<div class="flex justify-between items-center pr-4">
    <h2 class="p-4 text-2xl font-bold mb-4 text-gray-800">Employees registries</h2>
    <button 
        on:click={() => addOrganization()}
        class="flex items-center gap-x-2 bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded">
        <Icon src="{Plus}" solid size="24" /> Create organization
    </button>
</div>

<table class="table-auto border">
    <thead>
        <tr class="border">
            <th class="border">
                Name
            </th>
            <th class="border">
                Sigla
            </th>
            <th class="border">
                CNPJ
            </th>
            <th class="border">
                Created At
            </th>
            <th class="border">
                Actions
            </th>
        </tr>
    </thead>
    <tbody>
        {#each organizations as organization}
            <tr>
                <td class="border">{organization.name}</td>
                <td class="border">{organization.sigla}</td>
                <td class="border">{organization.cnpj}</td>
                <td class="border">{organization.createdAt}</td>
                <td class="border">
                    <button
                        class="text-blue-600 hover:text-blue-800"
                        on:click={() => detailOrganization(organization.id)}
                        title="View organization details">
                        <Icon src="{Eye}" solid size="16" />
                    </button>
                </td>
            </tr>
        {/each}
    </tbody>
</table>

