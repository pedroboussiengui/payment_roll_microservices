<script lang="ts">
    import { goto } from "$app/navigation";
    import Navbar from "$lib/components/navbar.svelte";
    import payroll from "$lib/services/axios/payroll";
    import z from "zod";

    type FormFields = {
        name: string;
        document: string;
        birthDate: string;
        identity: string;
        maritalStatus: string;
        gender: string;
        motherName: string;
        fatherName: string | null;
    };

    let data: FormFields = {
        name: '',
        document: '',
        birthDate: '',
        identity: '',
        maritalStatus: '',
        gender: '',
        motherName: '',
        fatherName: null
    }

    let touched:  Record<keyof FormFields, boolean> = {
		name: false,
		document: false,
		birthDate: false,
        identity: false,
        maritalStatus: false,
        gender: false,
        motherName: false,
        fatherName: false
	};

    let errors: FormFields = {...data}

    const schema = z.object({
        name: z.string().min(1, 'This field is required'),
        document: z.string().regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, 'Invalid format (XXX.XXX.XXX-XX)'),
        birthDate: z.string().refine((date) => new Date(date) < new Date(), 'Date must be in the past'),
        identity: z.string().min(1, 'This field is required'),
        maritalStatus: z.string().min(1, 'Please, select an option'),
        gender: z.string().min(1, 'Please, select an option'),
        motherName: z.string().min(1, 'This field is required'),
        fatherName: z
            .string()
            .trim()
            .optional()
            .refine(val => !val || val.length >= 3, 'Once defined, fatherName has to be at least 3 character long')
    });

    $: {
        const result = schema.safeParse(data);

        errors = Object.keys(data).reduce((acc, key) => {
            acc[key as keyof FormFields] = '';
            return acc;
        }, {} as FormFields);

        if (!result.success) {
            for (const issue of result.error.issues) {
				const key = issue.path[0] as keyof FormFields;
                if (key in errors) {
                    errors[key] = issue.message;
                }
			}
        }
    }

    $: isFormValid = 
        Object.entries(data).every(([key, value]) => {
            // Allow fatherName to be null or empty string
            return key === 'fatherName' ? true : value !== '';
        }) &&
        Object.values(errors).every(error => error === '')

    async function handleSubmit(event: Event) {
        event.preventDefault();
        console.log(data);
        console.log(isFormValid);

        payroll.post('/employees', data)
            .then((res) => {
                console.log(res.data);
                goto(`/employees/${res.data.employeeId}`)
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

    <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
            Identity
            <input 
                type="text" 
                bind:value={data.identity} 
                on:blur={() => touched.identity = true}
                class="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition {touched.identity && errors.identity ? 'border-red-500 focus:ring-red-500' : 'border-gray-300 focus:ring-blue-500'}"
            >
        </label>
        {#if touched.identity && errors.identity}
			<p class="text-sm text-red-600 mt-1">{errors.identity}</p>
		{/if}
    </div>

    <div>
        <label for="maritalStatus" class="block text-sm font-medium text-gray-700 mb-1">
            Marital status
        </label>
        <select
            id = "maritalStatus"
            bind:value={data.maritalStatus} 
            on:blur={() => touched.maritalStatus = true}
            class="mt-1 block w-full rounded-md border border-gray-300 bg-white py-2 px-3 shadow-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500 sm:text-sm {touched.maritalStatus && errors.maritalStatus ? 'border-red-500 focus:ring-red-500' : 'border-gray-300 focus:ring-blue-500'}"
        >
            <option value="">Select an option</option>
            <option value="single">Single</option>
            <option value="married">Married</option>
            <option value="divorced">Divorced</option>
            <option value="widowed">Widowed</option>
        </select>
        {#if touched.maritalStatus && errors.maritalStatus}
			<p class="text-sm text-red-600 mt-1">{errors.maritalStatus}</p>
		{/if}
    </div>

    <div>
        <label for="gender" class="block text-sm font-medium text-gray-700 mb-1">
            Gender
        </label>
        <select
            id = "gender"
            bind:value={data.gender} 
            on:blur={() => touched.gender = true}
            placeholder = "Choose..."
            class="mt-1 block w-full rounded-md border border-gray-300 bg-white py-2 px-3 shadow-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500 sm:text-sm {touched.gender && errors.gender ? 'border-red-500 focus:ring-red-500' : 'border-gray-300 focus:ring-blue-500'}"
        >
            <option value="male">Male</option>
            <option value="female">Female</option>
        </select>
        {#if touched.gender && errors.gender}
			<p class="text-sm text-red-600 mt-1">{errors.gender}</p>
		{/if}
    </div>

    <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
            Mother name:
            <input 
                type="text" 
                bind:value={data.motherName} 
                on:blur={() => touched.motherName = true}
                class="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition {touched.motherName && errors.motherName ? 'border-red-500 focus:ring-red-500' : 'border-gray-300 focus:ring-blue-500'}"
            >
        </label>
        {#if touched.motherName && errors.motherName}
			<p class="text-sm text-red-600 mt-1">{errors.motherName}</p>
		{/if}
    </div>

    <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
            Father name:
            <input 
                type="text" 
                bind:value={data.fatherName} 
                on:blur={() => touched.fatherName = true}
                class="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition {touched.fatherName && errors.fatherName ? 'border-red-500 focus:ring-red-500' : 'border-gray-300 focus:ring-blue-500'}"
            >
        </label>
        {#if touched.fatherName && errors.fatherName}
			<p class="text-sm text-red-600 mt-1">{errors.fatherName}</p>
		{/if}
    </div>

    <button 
        type="submit" 
        class="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed text-white font-semibold py-2 px-4 rounded-md transition"
        disabled={!isFormValid}
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