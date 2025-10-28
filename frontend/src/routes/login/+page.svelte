<script>
	import { goto } from "$app/navigation";
	import Header from "$lib/Header.svelte";

	let email = $state('');
	let passsword = $state('');
	let selected = $state('user');
	let loginError = $state('');

	const dummyUsers = [
		// Students
		{
			id: 7,
			email: 'amit.cs@college.com',
			password: 'password',
			firstName: 'Amit',
			lastName: 'Jain',
			role: 'student',
			departmentId: 1
		},
		{
			id: 8,
			email: 'deepa.ece@college.com',
			password: 'password',
			firstName: 'Deepa',
			lastName: 'M',
			role: 'student',
			departmentId: 2
		},
		{
			id: 9,
			email: 'riya.cs@college.com',
			password: 'password',
			firstName: 'Riya',
			lastName: 'Verma',
			role: 'student',
			departmentId: 1
		},
		// Admins (HODs)
		{
			id: 3,
			email: 'hod.cs@college.com',
			password: 'password',
			firstName: 'Dr. Anil',
			lastName: 'Kumar',
			role: 'HOD',
			departmentId: 1
		},
		{
			id: 4,
			email: 'hod.ece@college.com',
			password: 'password',
			firstName: 'Dr. Sunita',
			lastName: 'Reddy',
			role: 'HOD',
			departmentId: 2
		},
		{
			id: 5,
			email: 'hod.mech@college.com',
			password: 'password',
			firstName: 'Dr. Ramesh',
			lastName: 'Patel',
			role: 'HOD',
			departmentId: 3
		}
	];

	function select(role) {
		selected = role;
	}

	async function handleLogin(e) {
		e.preventDefault();
		loginError = '';
		const role = selected === 'user' ? 'student' : 'HOD';

		// Dummy user check (local only)
		if (role === 'student') {
			const user = dummyUsers.find(
				(u) => u.email === email && u.password === passsword && u.role === 'student'
			);
			if (user) {
				goto('/userhome');
				return;
			} else {
				loginError = 'Email or password is incorrect';
				return;
			}
		}
		if (role === 'HOD') {
			const admin = dummyUsers.find(
				(u) => u.email === email && u.password === passsword && u.role === 'HOD'
			);
			if (admin) {
				goto('/adminhome');
				return;
			} else {
				loginError = 'Email or password is incorrect';
				return;
			}
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
