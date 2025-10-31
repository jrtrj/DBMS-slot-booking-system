<script>
	import '../app.css';
	import favicon from '$lib/assets/favicon.svg';
	import { page } from '$app/stores';
	import { goto } from '$app/navigation';
	import { browser } from '$app/environment';
	import { user } from '$lib/authStore.js'; // Your auth store

	let { children } = $props();

	// 1. Define the pages a non-logged-in user IS allowed to see
	const publicPages = ['/login', '/signup','/'];

	// 2. Create a state to track if the user is allowed to see the page
	// Start it as 'false' so nothing renders by default
	let isAuthorized = $state(false);

	// 3. Use Svelte's reactive '$effect' to run this check
	//    whenever the user or the page URL changes.
	$effect(() => {
		// This logic MUST only run in the browser (client-side)
		if (browser) {
			const currentPath = $page.url.pathname;
			const isLoggedIn = !!$user;
			const isPublicPage = publicPages.includes(currentPath);

			// 4. THE LOGIC:
			// If the user IS logged in OR they are on a public page...
			if (isLoggedIn || isPublicPage) {
				isAuthorized = true; // ...it's OK to show the page.
			} else {
				// If the user is NOT logged in and on a PRIVATE page...
				isAuthorized = false; // ...do NOT show the page...
				goto('/login'); // ...and redirect them to login.
			}
		}
	});
</script>

<svelte:head>
	<link rel="icon" href={favicon} />
</svelte:head>

{#if isAuthorized}
	{@render children?.()}
{/if}