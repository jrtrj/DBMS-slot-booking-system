<script>
	import { onMount } from 'svelte';
	import Header from '$lib/Header.svelte';

	let email = $state('');
	let password = $state('');
	let firstName =$state('');
	let lastName = $state('');
	let role = $state('STUDENT');
	let departmentId = $state('');
	let departments = $state([]);
	let error = $state('');
	let success = $state('');
	let isLoading = $state(false);

	const BASE_URL = import.meta.env.VITE_BASE_URL;

	onMount(async () => {
		try {
			const res = await fetch(`http://localhost:8080/api/departments/1`);
			if (!res.ok) throw new Error('Failed to fetch departments');
			departments = await res.json();
		} catch (err) {
			error = err.message;
		}
	});

	async function handleSignup(e) {
		e.preventDefault();
		error = '';
		success = '';
		isLoading = true;
		const payload = {
			email,
			password,
			firstName,
			lastName,
			role,
			departmentId: Number(departmentId)
		};
		try {
			const res = await fetch(`http://localhost:8080/api/users/register`, {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(payload)
			});
			if (!res.ok) throw new Error('Registration failed');
			success = 'Registration successful!';
			email = password = firstName = lastName = departmentId = '';
			role = 'STUDENT';
		} catch (err) {
			error = err.message;
		} finally {
			isLoading = false;
		}
	}
</script>

<main>
	<Header h1="Sign Up" h2="Create your account" showBell={false} />
	<form class="signup-form" onsubmit={handleSignup}>
		{#if error}
			<p class="error">{error}</p>
		{/if}
		{#if success}
			<p class="success">{success}</p>
		{/if}
		<div class="form-group">
			<label for="email">Email</label>
			<input type="email" id="email" placeholder="Enter your email" bind:value={email} required />
		</div>
		<div class="form-group">
			<label for="password">Password</label>
			<input
				type="password"
				id="password"
				placeholder="Enter your password"
				bind:value={password}
				required
			/>
		</div>
		<div class="form-group">
			<label for="firstName">First Name</label>
			<input
				type="text"
				id="firstName"
				placeholder="Enter your first name"
				bind:value={firstName}
				required
			/>
		</div>
		<div class="form-group">
			<label for="lastName">Last Name</label>
			<input
				type="text"
				id="lastName"
				placeholder="Enter your last name"
				bind:value={lastName}
				required
			/>
		</div>
		<div class="form-group">
			<label for="role">Role</label>
			<select id="role" bind:value={role} required>
				<option value="STUDENT">Student</option>
				<option value="TEACHER">Teacher</option>
			</select>
		</div>
		<div class="form-group">
			<label for="department">Department</label>
			<select id="department" bind:value={departmentId} required>
				<option value="" disabled selected>Select Department</option>
				{#each departments as dept}
					<option value={dept.id}>{dept.name}</option>
				{/each}
			</select>
		</div>
		<button type="submit" disabled={isLoading}>{isLoading ? 'Registering...' : 'Sign Up'}</button>
	</form>
</main>

<style>
	.signup-form {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 1em;
		width: 100%;
		max-width: 350px;
		margin: 3em auto 0 auto;
	}
	.form-group {
		width: 100%;
		display: flex;
		flex-direction: column;
		gap: 0.5em;
	}
	.form-group label {
		font-size: 0.9em;
		font-weight: 700;
		color: #333;
		margin-left: 1em;
	}
	.signup-form input,
	.signup-form select {
		width: 95%;
		padding: 0.9em 1em;
		border: 1.5px solid #7e7d7d;
		border-radius: 1.5em;
		font-size: 1em;
		outline: none;
		background: #fafafa;
		transition: border 0.2s;
	}
	.signup-form input:focus,
	.signup-form select:focus {
		border: 1.5px solid #111;
	}
	.signup-form button {
		width: 80%;
		padding: 0.7em 0;
		background: #f0efef;
		color: #1b1b1b;
		border: 1.5px solid black;
		border-radius: 1.5em;
		font-size: 1.1em;
		font-weight: 500;
		cursor: pointer;
		transition: background 0.2s;
	}
	.signup-form button:hover {
		background: #222;
		color: white;
	}
	.error {
		color: #e53935;
		font-size: 0.95em;
		margin-bottom: 0.5em;
		text-align: center;
	}
	.success {
		color: #388e3c;
		font-size: 0.95em;
		margin-bottom: 0.5em;
		text-align: center;
	}
</style>
