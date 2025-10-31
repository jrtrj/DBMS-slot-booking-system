<script>
	import Header from '$lib/Header.svelte';
	import Navigation from '$lib/Navigation.svelte';
	import { user as authStore } from '$lib/authStore';
	import { goto } from '$app/navigation';
	import { onMount } from 'svelte';

	const BASE_URL = import.meta.env.VITE_BASE_URL || 'http://localhost:8080';

	// store fetched departments
	let departments = [];
	let departmentName = '';
	let fetchError = '';

	// derived current user value from the auth store
	$: currentUser = $authStore;

	function handleLogOut() {
		authStore.logout();
		goto('/login');
	}

	// fetch departments on mount
	onMount(async () => {
		try {
			const res = await fetch(`http://localhost:8080/api/departments`);
			if (!res.ok) throw new Error('Failed to fetch departments');
			departments = await res.json();
		} catch (err) {
			fetchError = err.message || String(err);
		}
	});

	// whenever currentUser or departments update, compute the department name
	$: if (currentUser && departments && departments.length) {
		const dep = departments.find(
			(d) => d.id === currentUser.departmentId || d.id === Number(currentUser.departmentId)
		);
		departmentName = dep ? dep.name : 'Not assigned';
	}
</script>

<main>
    
    {#if currentUser}
	<Header h1="Profile" h2="" showBell={true} />
	<Navigation role="user" />
	<section class="profile-card">
		<h1 class="profile-label">Name</h1>
		<div class="profile-field">
			<h2 class="name">{currentUser.name}</h2>
		</div>
		<h1 class="profile-label">Email</h1>
		<div class="profile-field">
			<h2 class="name">{currentUser.email}</h2>
		</div>
		<h1 class="profile-label">Role</h1>
		<div class="profile-field">
			<h2 class="name">{currentUser.role}</h2>
		</div>
		<h1 class="profile-label">Department</h1>
		<div class="profile-field">
			<h2 class="name">{departmentName}</h2>
		</div>
        <div class="login-button">
		<button class="logout" on:click={handleLogOut}>Log out</button>
        </div>
	</section>
	{:else}
	<section class="no-user">
			<p>You are not logged in.</p>
			<a href="/login" style="text-decoration: underline;">Go to Login</a>
		</section>
	{/if}

	{#if fetchError}
		<p class="error">{fetchError}</p>
	{/if}
</main>

<style>
	.profile-card {
		max-width: 45rem; /* ~720px */
		margin: 2rem auto;
		padding: 1.5rem;
		border: 0.0625rem solid #e0e0e0; /* 1px */
		border-radius: 0.5rem; /* ~8px */
		background: #fff;
	}
	.profile-field {
		width: 100%;
		height: 3rem;
		background-color: rgb(236, 236, 236);
		border-radius: 1.2rem;
		border: 0.125rem solid black; /* 2px */
		margin-bottom: 2rem; /* gap between the field and the next label */
		display: flex;
		align-items: center;
		padding: 0 1rem; /* space between label and content */
	}
	.name {
		margin: 0;
		font-size: 1.15rem;
		color: rgb(50, 50, 50);
	}
	.logout {
		width: 100%;
		background-color: grey;
		margin-top: 2rem;
		padding: 0.6rem 1.5rem;

		border-radius: 1.5rem;
		border: 0.0625rem solid #111; /* 1px */
		background: #f0efef;
		cursor: pointer;
	}
  .logout:hover{
    background-color: black;
    color: white;
    
  }
	.error {
		color: #e53935;
		text-align: center;
		margin-top: 1rem;
	}
	.profile-label {
		font-size: 1.05rem;
		font-weight: 700;
		margin: 0.6rem 0 0.35rem 0; /* gap between label and its field */
	}
    .login-button{
        display: flex;
        align-items: center;
        justify-content: center;
    }
</style>
