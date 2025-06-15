<script lang="ts">
    import { getTokens } from "$lib/services/auth";
    import idp from "$lib/services/axios/idp";
    import type { UserContract } from "$lib/types";
    import { onMount } from "svelte";

    let userContracts: UserContract[] = []

    // onMount(async () => {
    //     try {
    //         console.log("Fetching contracts...");
    //         const payload = decodeJwt()
    //         const userId = payload.sub;
    //         const res = await idp.get(`/employees/${userId}/contracts`)
    //         contracts = res.data;
    //     } catch (err) { 
    //         console.error("Error fetching contracts:", err);
    //     }
    // });

    onMount(async () => {
        try {
            console.log("Fetching contracts...");
            const res = await idp.get('/user-contracts')
            userContracts = res.data;
            console.log(userContracts);
        } catch (err) {
            console.error("Error fetching contracts:", err);
        }
    });

</script>

<h1 class="text-2xl font-bold text-center my-8 text-gray-800">
    Choose one of your contract
</h1>

<div class="min-h-screen flex flex-col items-center bg-gray-50">
    <div class="space-y-4 w-full max-w-md px-4">
        {#each userContracts as contract}
            <div class="w-full">
                <button 
                    on:click={() => getTokens(contract.id)}
                    class="w-full bg-white border border-gray-300 hover:border-blue-500 text-gray-800 hover:text-blue-600 font-medium py-3 px-4 rounded-lg shadow-sm hover:shadow transition duration-200 text-left"
                >
                    <div class="text-base font-semibold">
                        {contract.organization}
                    </div>
                    <div class="text-sm text-gray-400">
                        {contract.matricula} • {contract.department}
                    </div>
                </button>
            </div>
        {/each}
    </div>
</div>