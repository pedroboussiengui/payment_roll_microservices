<script lang="ts">
    import { onMount } from "svelte";
    import type { Employee } from "$lib/utils/types";
    import Navbar from "../../components/navbar.svelte";
    import payroll from "$lib/payroll_api";

    let employees: Employee[] = []

    onMount(async () => {
        try {
            // console.log("Verificando autenticação...");
            // console.log(TokenStorage.isAuthenticated2());
            const res = await payroll.get('/employees')
            employees = res.data as Employee[];
        } catch (err: any) {
            console.error('Erro ao carregar funcionários:', err);
            console.log(err.response.data);
        }
    });
</script>

<Navbar />

<h2>Employees registries</h2>

<table border="1" cellpadding="8" cellspacing="0">
    <thead>
        <tr>
            <th>Name</th>
            <th>Document</th>
        </tr>
    </thead>
    <tbody>
        {#each employees as employee}
            <tr>
                <td>{employee.name}</td>
                <td>{employee.document}</td>
            </tr>
        {/each}
    </tbody>
</table>
