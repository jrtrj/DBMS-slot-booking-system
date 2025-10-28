<script>
	import Header from '$lib/Header.svelte';
	import { goto } from '$app/navigation';

	let email = $state('');
	let passsword = $state('');
	let selected = $state('user');
	let loginError = $state('');

	function select(role) {
		selected = role;
	}

	async function handleLogin(e) {
		e.preventDefault();
		loginError = '';
		const role = selected === 'user' ? 'student' : 'HOD';
		try {
			const res = await fetch('http://localhost:8080/api/users/login', {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify({ email, password: passsword, role })
			});
			if (!res.ok) {
				throw new Error('Email or password is incorrect');
			}
			// Optionally check response for more info
			if (selected === 'user') {
				goto('/userhome');
			} else {
				goto('/adminhome');
			}
		} catch (err) {
			loginError = err.message;
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
				bind:value={passsword}
				required
			/>
			<a href="/forgot-password" class="forgot-link">Forgot password?</a>
			<button type="submit" class="login-btn" onclick={handleLogin}>LOGIN</button>
		</form>
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
</style>
