<script lang="ts">
    import { goto } from "$app/navigation";
    import Navbar from "$lib/components/navbar.svelte";
    import payroll from "$lib/services/axios/payroll";
    import z from "zod";

    let data = {
        name: '',
        document: '',
        birthDate: ''
    }

    let touched = {
		name: false,
		document: false,
		birthDate: false
	};

    let errors = {
        name: '',
        document: '',
        birthDate: ''
    }

    const schema = z.object({
        name: z.string().min(1, 'This field is required'),
        document: z.string().regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, 'Invalid format (XXX.XXX.XXX-XX)'),
        birthDate: z
			.string()
			.refine((date) => new Date(date) < new Date(), 'Date must be in the past'),
    });

    $: {
        const result = schema.safeParse(data);
        if (!result.success) {
            errors = { name: '', document: '', birthDate: '' };
            for (const issue of result.error.issues) {
				const key = issue.path[0];
                if (key === 'name' || key === 'document' || key === 'birthDate') {
                    errors[key] = issue.message;
                }
			}
        } else {
			errors = { name: '', document: '', birthDate: '' };
		}
    }

    const isFormValid = () => {
        return (
            data.name !== '' &&
            data.document !== '' &&
            data.birthDate !== '' &&
            !errors.name &&
            !errors.document &&
            !errors.birthDate
        );
    }

    async function handleSubmit(event: Event) {
        event.preventDefault();
        console.log(data);

        payroll.post('/employees', data)
            .then((res) => {
                console.log(res.data);
                goto(`/employees/${res.data.id}`)
            })
            .catch((err) => {
                console.log(err);
            })
    }
</script>

<Navbar />

<h2 class="p-4 text-2xl font-bold mb-4 text-gray-800">New employee</h2>

<form on:submit|preventDefault={handleSubmit} class="max-w-md mx-auto bg-white p-6 rounded-lg shadow-md space-y-4">

    <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
            Name:
            <input 
                type="text" 
                bind:value={data.name} 
                on:blur={() => touched.name = true}
                class="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition {touched.name && errors.name ? 'border-red-500 focus:ring-red-500' : 'border-gray-300 focus:ring-blue-500'}"
            >
        </label>
        {#if touched.name && errors.name}
			<p class="text-sm text-red-600 mt-1">{errors.name}</p>
		{/if}
    </div>

    <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
            Document
            <input 
                type="text" 
                bind:value={data.document} 
                on:blur={() => touched.document = true}
                class="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition {touched.document && errors.document ? 'border-red-500 focus:ring-red-500' : 'border-gray-300 focus:ring-blue-500'}"
            >
        </label>
        {#if touched.document && errors.document}
			<p class="text-sm text-red-600 mt-1">{errors.document}</p>
		{/if}
    </div>

    <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
            BirthDate:
            <input 
                type="date" 
                bind:value={data.birthDate} 
                on:blur={() => touched.birthDate = true}
                class="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition {touched.birthDate && errors.birthDate ? 'border-red-500 focus:ring-red-500' : 'border-gray-300 focus:ring-blue-500'}"
            >
        </label>
        {#if touched.birthDate && errors.birthDate}
			<p class="text-sm text-red-600 mt-1">{errors.birthDate}</p>
		{/if}
    </div>

    <button 
        type="submit" 
        class="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed text-white font-semibold py-2 px-4 rounded-md transition"
        disabled={!isFormValid()}
	>Add</button>
</form>

<!--
{
    "id": "6696dbc0-73e8-4f99-a6d7-8223ca0ad8ac",
    "name": "Tommie Bartell",
    "document": "111.111.111-11",
    "birthDate": "1988-11-17"
}
-->

