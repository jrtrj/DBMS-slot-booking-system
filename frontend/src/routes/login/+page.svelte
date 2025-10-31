<script>
	import { goto } from '$app/navigation';
	import Header from '$lib/Header.svelte';

	let baseURL = import.meta.env.VITE_BASE_URL;
	let email = $state('');
	// Typo fix: 'passsword' corrected to 'password'
	let password = $state(''); 
	let selected = $state('user');
	let loginError = $state('');
	let isLoading = $state(false);

	// The dummyUsers array is no longer needed.

	function select(role) {
		selected = role;
	}

	async function handleLogin(e) {
		e.preventDefault();
		loginError = '';
		isLoading = true;

		// 1. Create the payload for the backend
		const payload = {
			email: email,
			password: password
		};

		try {
			// 2. Call your real login endpoint
			const res = await fetch(`http://localhost:8080/api/users/login`, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(payload)
			});

			if (!res.ok) {
				throw new Error('Invalid email or password');
			}

			// 3. Get the user data from the successful response
			const user = await res.json(); 

			// 4. Check the user's role from the response
			const isAdminRole = user.role === 'HOD' || user.role === 'PRINCIPAL' || user.role === 'SUPERADMIN';
			const isUserRole = user.role === 'STUDENT' || user.role === 'TEACHER';

			// 5. Redirect based on the role and the toggle selection
			if (selected === 'admin' && isAdminRole) {
				goto('/adminhome');
			} else if (selected === 'user' && isUserRole) {
				goto('/userhome');
			} else {
				// This happens if they log in as an admin but had "USER" toggled
				loginError = `You logged in as a ${user.role}, but selected ${selected.toUpperCase()}.`;
			}

		} catch (err) {
			loginError = err.message;
		} finally {
			isLoading = false;
		}
	}
</script>

<main>
	<Header h1="Welcome," h2="" showBell={false} />
	<div class="container">
		<div class="toggle-switch">
			<button
				class="toggle-btn {selected === 'user' ? 'active' : ''}"
				onclick={() => select('user')}
			>
				USER
			</button>
			<button
				class="toggle-btn {selected === 'admin' ? 'active' : ''}"
				onclick={() => select('admin')}
			>
				ADMIN
			</button>
		</div>
		<form class="login-form">
			{#if loginError}
				<p class="login-error">{loginError}</p>
			{/if}
			<input type="email" placeholder="Email" class="login-input" bind:value={email} required />
			<input
				type="password"
				placeholder="Password"
				class="login-input"
				bind:value={password}
				required
			/>
			<a href="/forgot-password" class="forgot-link">Forgot password?</a>
			<button type="submit" class="login-btn" onclick={handleLogin}>LOGIN</button>
		</form>
		<p class="signup-p">New User?<a href="/signup">Sign UP</a></p>
	</div>
</main>

<style>
	.login-error {
		color: #e53935;
		font-size: 0.95em;
		margin-bottom: 0.5em;
		text-align: center;
	}
	.login-form {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 1em;
		width: 100%;
		max-width: 320px;
		margin-top: 5em;
	}
	.login-input {
		width: 95%;
		padding: 0.9em 1em;
		border: 1px solid #ffffff;
		border-radius: 1.5em;
		border: 1.5px solid rgb(126, 125, 125);
		font-size: 1em;
		outline: none;
		background: #fafafa;
		transition: border 0.2s;
	}
	.login-input:focus {
		border: 1.5px solid #111;
	}
	.forgot-link {
		align-self: flex-end;
		font-size: 0.7em;
		color: #555;
		text-decoration: none;
		margin-top: -0.8em;
		margin-bottom: 0.5em;
		transition: color 0.2s;
	}
	.forgot-link:hover {
		color: #111;
		text-decoration: underline;
	}
	.login-btn {
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
	.login-btn:hover {
		background: #222;
		color: white;
	}
	.container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		margin-top: 2em;
		min-height: 40vh;
	}

	.toggle-switch {
		display: flex;
		align-items: center;
		justify-content: center;
		background: #ffffff;
		border-radius: 2em;
		padding: 0.3em 0.4em;
		gap: 0.2em;
		margin: 2em 0 1.5em 0;
		width: fit-content;
		border: 1.5px solid black;
	}

	.toggle-btn {
		border: none;
		background: transparent;
		color: #222;
		font-size: 1.1em;
		font-weight: 500;
		padding: 0.5em 1.5em;
		border-radius: 2em;
		cursor: pointer;
		transition:
			background 0.2s,
			color 0.2s;
	}
	.toggle-btn.active {
		background: #111;
		color: #fff;
	}

	.signup-p{
		margin-top: 12em;
	}
	.signup-p a{
		font-weight: 500;
		text-decoration: underline;
	}
</style>
