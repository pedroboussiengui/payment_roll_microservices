<script lang="ts">
    export let isOpen = false;
    export let onConfirm;
    export let onCancel;

    let data = {
        matricula: '',
        entryDate: '',
        exerciseDate: '',
        contractType: '',
        department: '',
        position: '',
        function: ''
    }

    const handleConfirm = () => {
        onConfirm(data);
        data = {
            matricula: '',
            entryDate: '',
            exerciseDate: '',
            contractType: '',
            department: '',
            position: '',
            function: ''
        }
    };
    
    const handleCancel = () => {
        onCancel();
        data = {
            matricula: '',
            entryDate: '',
            exerciseDate: '',
            contractType: '',
            department: '',
            position: '',
            function: ''
        }
    };
</script>

{#if isOpen}
<div class="fixed inset-0 bg-black bg-black/50 flex items-center justify-center z-50">
    <div class="bg-white rounded-lg shadow-lg w-full max-w-md p-6 relative">
        
        <h2 class="text-xl font-semibold mb-4">Evento de admissão</h2>
        
        <div>
            <label for="matricula">Matricula</label>
            <input 
                type="text" 
                class="w-full border border-gray-300 rounded"
                bind:value={data.matricula} 
                placeholder="matricula"
            />
        </div>
        
        <div>
            <label for="entryDate">Data de entrada</label>
            <input 
                type="text"
                class="w-full border border-gray-300 rounded"
                bind:value={data.entryDate}
            />
        </div>
        
        <div>
            <label for="exerciseDate">Data de exercício</label>
            <input 
                type="text"
                class="w-full border border-gray-300 rounded"
                bind:value={data.exerciseDate}
            />
        </div>
        
        <div>
            <label for="contractType">Tipo de contrato</label>
            <select
                class="w-full border border-gray-300 rounded"
                bind:value={data.contractType}
            >
                <option value="celetista">Celetista</option>
                <option value="estagiario">Estagiário</option>
                <option value="temporario">Temporário</option>
            </select>
        </div>
        
        <div>
            <label for="department">Departamento</label>
            <input 
                type="text"
                class="w-full border border-gray-300 rounded"
                bind:value={data.department}
            />
        </div>
        
        <div>
            <label for="position">Cargo</label>
            <input 
                type="text" 
                class="w-full border border-gray-300 rounded"
                bind:value={data.position}
            />
        </div>

        <div>
            <label for="function">Função</label>
            <input 
                type="text" 
                class="w-full border border-gray-300 rounded"
                bind:value={data.function}
            />
        </div>
        
        <div class="flex justify-end space-x-2 mt-4">
            <button on:click={handleCancel} class="bg-gray-300 hover:bg-gray-400 text-gray-800 p-2 rounded">Cancelar</button>
            <button on:click={handleConfirm} class="bg-blue-500 hover:bg-blue-600 text-white p-2 rounded">Confirmar</button>
        </div>
    </div>
</div>
{/if}

<!-- <style>
.modal-backdrop {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.modal {
    background: white;
    padding: 2rem;
    border-radius: 8px;
    min-width: 300px;
}

.modal-actions {
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    margin-top: 1rem;
}

input {
    width: 100%;
    padding: 0.5rem;
    margin: 0.5rem 0;
    box-sizing: border-box;
}
</style> -->