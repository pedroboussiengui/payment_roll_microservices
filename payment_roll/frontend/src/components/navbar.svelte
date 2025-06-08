<script lang="ts">
    import { logout } from "$lib/auth";
    // import idp from "$lib/idp_api";
    // import { TokenStorage } from "$lib/infra/storage/TokenStorage";

    async function endSession() {
        // const refreshToken = TokenStorage.getRefreshToken();
        // if (refreshToken) {
        //     await idp.post("/refresh-tokens", {
        //         refreshToken: refreshToken
        //     });
        // }
        logout();
    }
</script>

<nav>
    <ol>
        <li><a href="/">Home</a></li>
        <li><a href="/employees">Employees</a></li>
    </ol>
</nav>

<div>
    <button on:click={() => endSession()}>Logout</button>
</div>