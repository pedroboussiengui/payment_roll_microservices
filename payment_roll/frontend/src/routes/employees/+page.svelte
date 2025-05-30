<script lang="ts">
    import { onMount } from "svelte";
    import { PayrollHttpGateway, type Employee } from "../../infra/PayrollHttpGateway";
    import Navbar from "../../components/navbar.svelte";

    const payrollHttpGateway = new PayrollHttpGateway();
    let employees: Employee[] = []

    onMount(async () => {
        employees = await payrollHttpGateway.getAllEmployees();
    });

</script>

<h2>Employees registries</h2>

<Navbar />

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
