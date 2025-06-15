<script lang="ts">
    import payroll from "$lib/services/axios/payroll";
    import { createEventDispatcher } from "svelte";
    export let isOpen = false;
    export let employeeId: string = ''
    export let contractId: string = '';

    const dispatch = createEventDispatcher();

    let data = {
        reason: ''
    }

    const handleConfirm = () => {
        console.log(data);
        const res = afastar();
        dispatch('confirm', res);
        data = { reason: '' }
    };
    
    const handleCancel = () => {
        dispatch('cancel');
        data = { reason: '' }
    };

    async function afastar() {
        await payroll.post(`/employees/${employeeId}/contracts/${contractId}/afastamento`, data)
            .then((res) => {
                console.log(res.data);
                return true;
            })
            .catch((err) => {
                console.log(err.response.data);
                return false;
            });
    }

</script>

{#if isOpen}
    <div class="fixed inset-0 bg-black bg-black/50 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg shadow-lg w-full max-w-md p-6 relative">
            
            <h2 class="text-xl font-semibold mb-4">Evento de afastamento</h2>
            
            <div>
                <label for="matricula">Motivo do afastamento</label>
                <input 
                    type="text" 
                    class="w-full border border-gray-300 rounded"
                    bind:value={data.reason} 
                    placeholder="matricula"
                />
            </div>
            
            <div class="flex justify-end space-x-2 mt-4">
                <button on:click={handleCancel} class="bg-gray-300 hover:bg-gray-400 text-gray-800 p-2 rounded">Cancelar</button>
                <button on:click={handleConfirm} class="bg-blue-500 hover:bg-blue-600 text-white p-2 rounded">Confirmar</button>
            </div>
        </div>
    </div>
{/if}