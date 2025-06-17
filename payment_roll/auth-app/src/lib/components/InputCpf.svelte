<script lang="ts">
    import { createEventDispatcher } from "svelte";

    export let value: string = '';
    const dispatch = createEventDispatcher();
    let inputEl: HTMLInputElement;

    function formatCpf(value: string): string {
        const digits = value.replace(/\D/g, "").slice(0, 11);
		return digits
			.replace(/^(\d{3})(\d)/, "$1.$2")
			.replace(/^(\d{3})\.(\d{3})(\d)/, "$1.$2.$3")
			.replace(/^(\d{3})\.(\d{3})\.(\d{3})(\d)/, "$1.$2.$3-$4");
    }

    function handleInput(event: Event) {
        const target = event.target as HTMLInputElement;
        value = formatCpf(target.value);
        inputEl.value = value;
        dispatch('input', value);
        dispatch('change', value);
    }
</script>

<input 
    bind:this={inputEl}
    type="text" 
    {value}
    on:input={handleInput}
    on:blur={(e) => dispatch('blur', e)}
    {...$$restProps}
/>