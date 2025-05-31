<script lang="ts">
    import { onMount } from "svelte";
    import { PayrollHttpGateway, type Employee } from "../../infra/PayrollHttpGateway";
    import Navbar from "../../components/navbar.svelte";
    import { IdentityProviderHttpGateway } from "../../infra/IdentityProviderHttpGateway";

    const payrollHttpGateway = new PayrollHttpGateway();
    const identityProviderGateway = new IdentityProviderHttpGateway();
    
    let employees: Employee[] = []

    const accessToken = localStorage.getItem("accessToken")
    const refreshToken = localStorage.getItem("refreshToken")

    type SessionTokens = {
        sessionId: string,
        accessToken: string,
        refreshToken: string
    }
    let sessionTokens: SessionTokens;

    onMount(async () => {
        if (!accessToken) {
            window.location.href = 'http://localhost:8080/auth';
        }
        // employees = await withAuthRetry(tk => payrollHttpGateway.getAllEmployees(tk), accessToken!!);
        try {
            employees = await payrollHttpGateway.getAllEmployees(accessToken!!)
        } catch(e: any) {
            console.error('Error in list employees: ', e)
            if (e.status === 401) {
                try {
                    sessionTokens = await identityProviderGateway.refreshTokens(refreshToken!!)
                    localStorage.setItem('accessToken', sessionTokens.accessToken);
                    localStorage.setItem('refreshToken', sessionTokens.refreshToken);
                    localStorage.setItem('sessionId', sessionTokens.sessionId);

                    const newAccessToken = localStorage.getItem("accessToken")
                    employees = await payrollHttpGateway.getAllEmployees(newAccessToken!!)
                } catch(e: any) {
                    console.error('Error in refresh tokens: ', e)
                    window.location.href = 'http://localhost:8080/auth';
                }
            }
        }
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
