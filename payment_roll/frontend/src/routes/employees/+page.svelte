<script lang="ts">
    import { onMount } from "svelte";
    import { PayrollHttpGateway } from "$lib/infra/http/PayrollHttpClient";
    import { IdentityProviderHttpGateway } from "$lib/infra/http/IdentityProviderHttpGateway";
    import { RefreshToken } from "$lib/application/RefreshToken";
    import { ListEmployee } from "$lib/application/employee/ListEmployee";
    import type { Employee, SessionTokens } from "$lib/utils/types";
    import { TokenStorage } from "$lib/infra/storage/TokenStorage";
    import Navbar from "../../components/navbar.svelte";

    const payrollHttpGateway = new PayrollHttpGateway();
    const identityProviderGateway = new IdentityProviderHttpGateway();

    const refreshToken = new RefreshToken()
    const listEmployees = new ListEmployee()
    
    let employees: Employee[] = []

    onMount(async () => {
        const accessToken = TokenStorage.getAccessToken();
        if (!accessToken) {
            try {
                const sessionTokens = await refreshToken.execute();
                TokenStorage.setTokens(sessionTokens);
            } catch(e) {
                window.location.href = 'http://localhost:8080/auth';
            }
        }
        try {
            employees = await listEmployees.execute(accessToken!!)
        } catch(e: any) {
            console.error('Error in list employees: ', e)
            if (e.status === 401) {
                try {
                    const sessionTokens = await refreshToken.execute();
                    TokenStorage.setTokens(sessionTokens);
                    const accessToken = TokenStorage.getAccessToken();
                    employees = await listEmployees.execute(accessToken!!)
                } catch(e: any) {
                    console.error('Error in refresh tokens: ', e)
                    TokenStorage.clearTokens();
                    window.location.href = 'http://localhost:8080/auth';
                }
            }
        }
    });


    // onMount(async () => {
    //     if (!accessToken) {
    //         window.location.href = 'http://localhost:8080/auth';
    //     }
    //     // employees = await withAuthRetry(tk => payrollHttpGateway.getAllEmployees(tk), accessToken!!);
    //     try {
    //         employees = await payrollHttpGateway.getAllEmployees(accessToken!!)
    //     } catch(e: any) {
    //         console.error('Error in list employees: ', e)
    //         if (e.status === 401) {
    //             try {
    //                 sessionTokens = await identityProviderGateway.refreshTokens(refreshToken!!)
    //                 localStorage.setItem('accessToken', sessionTokens.accessToken);
    //                 localStorage.setItem('refreshToken', sessionTokens.refreshToken);
    //                 localStorage.setItem('sessionId', sessionTokens.sessionId);

    //                 const newAccessToken = localStorage.getItem("accessToken")
    //                 employees = await payrollHttpGateway.getAllEmployees(newAccessToken!!)
    //             } catch(e: any) {
    //                 console.error('Error in refresh tokens: ', e)
    //                 window.location.href = 'http://localhost:8080/auth';
    //             }
    //         }
    //     }
    // });

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
