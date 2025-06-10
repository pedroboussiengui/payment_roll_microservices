<script lang="ts">
    import { decodeJwt } from "$lib/jwt";
    import { logout } from "$lib/services/auth";
    import { Icon, ArrowLeftStartOnRectangle } from "svelte-hero-icons";

    let username: string | null = null;

    const payload = decodeJwt()
    username = payload.name;
</script>

<nav class="bg-slate-300 px-2 py-2 flex justify-between items-center">
    
    <ol class="flex space-x-6">
        <li><a href="/home">Home</a></li>
        <li><a href="/employees">Employees</a></li>
    </ol>

    <div class="flex items-center gap-x-4">
        olá, {username}
        <button 
            on:click={() => logout()}
            class="flex items-center gap-x-2 bg-red-600 hover:bg-red-700 text-white font-semibold py-2 px-4 rounded"
        >
            <Icon src="{ArrowLeftStartOnRectangle}" solid size="16" />
            Logout
        </button>
    </div>
</nav>


