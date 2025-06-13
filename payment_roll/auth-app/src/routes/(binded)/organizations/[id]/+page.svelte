<script lang="ts">
    import { page } from "$app/stores";
    import Navbar from "$lib/components/navbar.svelte";
    import payroll from "$lib/services/axios/payroll";
    import type { Organization } from "$lib/types";
    import { onMount } from "svelte";

    let organization: Organization | null = null;
    let error: string | null = null

    $: organizationId = $page.params.id;

    onMount(async () => {
        await payroll.get(`/organizations/${organizationId}`)
            .then(res => {
                organization = res.data;
            })
            .catch(err => {
                error = err.response.data.message;
                console.log(err.response.data.message);
            });
    });
</script>

<Navbar />

{#if error}
    <p class="text-red-600 font-semibold p-4">{error}</p>
{:else if !organization}
	<p class="p-4 text-gray-500">Loading...</p>
{:else}
    <h2 class="text-2xl">{organization.name}</h2>
    <fieldset class="border">
        <legend class="text-lg">Organization details</legend>

        <div class="">
            <div>
                <p class="text-lgg font-semibold">Name</p>
                <p class="text-base text-gray-800">{organization.name}</p>
            </div>
            <div>
                <p class="text-lgg font-semibold">CNPJ</p>
                <p class="text-base text-gray-800">{organization.cnpj}</p>
            </div>
            <div>
                <p class="text-lgg font-semibold">Sigla</p>
                <p class="text-base text-gray-800">{organization.sigla}</p>
            </div>
            <div>
                <p class="text-lgg font-semibold">Created at</p>
                <p class="text-base text-gray-800">{organization.createdAt}</p>
            </div>
        </div>
    </fieldset>
{/if}