<!-- <script lang="ts">
    import { page } from "$app/stores";

    $: loggedIn = $page.data.session?.loggedIn;
</script>

{#if loggedIn}
    <slot />
{:else}
	<p>Redirecionando...</p>
{/if} -->

<!-- <script lang="ts">
    import { TokenStorage } from "$lib/infra/storage/TokenStorage";
    import { onMount } from "svelte";

    let isChecking = true;
	let isAuthenticated = false;

    onMount(() => {
        const token = TokenStorage.getAccessToken() ?? TokenStorage.getToken();
        if (!token) {
            window.location.href = "http://localhost:8080/auth";
        } else {
            isAuthenticated = true;
			isChecking = false;
        }
    });
</script>

{#if isChecking}
	<p>Carregando...</p>
{:else}
	{#if isAuthenticated}
		<slot />
	{/if}
{/if} -->

<!-- 

<script lang="ts">
    import { goto } from "$app/navigation";
    import { TokenStorage } from "$lib/infra/storage/TokenStorage";
    import { onMount } from "svelte";

    /**
     * Eu não posso simplesmente verificar a existencia do accessToken
     * o refreshToken pode existir e ai ele pode ser usado para conseguir 
     * um novo token
    */

    onMount(() => {
        // console.log("Verificando autenticação...");
        // const isAuthenticated = TokenStorage.isAuthenticated();
        // if (isAuthenticated) {
        //     console.log(`value: ${isAuthenticated}`);
        // }
        // const token = TokenStorage.getAccessToken() ?? TokenStorage.getToken();
        // if (!token) {
        //     const isAuthenticated = TokenStorage.isAuthenticated();
        //     console.log(`value: ${isAuthenticated}`);
        //     if (!isAuthenticated) {
        //         goto("http://localhost:8080/auth");
        //     } else {
        //         // continua
        //     }
        // } else {
        //     // continua
        // }
    });
</script>

<slot /> -->

<!-- 
localhost:5173/home
localhost:5173/contracts
localhost:5173/employees
localhost:5173/
localhost:5173
-->

<script lang="ts">
    import { authn } from "$lib/stores/authn";
    import { onMount } from "svelte";

    onMount(() => {
        console.log('verification de authentication....');
        if (!authn.check()) {
            window.location.href = 'http://localhost:8080/auth';
        }
    });
</script>

<slot />
