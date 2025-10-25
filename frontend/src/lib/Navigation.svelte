<script>
	import { page } from '$app/stores';

	function isActive(path) {
		return $page.url.pathname === path ? 'active' : '';
	}
</script>

<main>
	<nav class="bottom-nav">
		<a href="/home" class="nav-btn {isActive('/home')}">
			<img src="/house-regular.svg" alt="Home" class="nav-icon" />
		</a>
		<a href="/profile" class="nav-btn {isActive('/profile')}">
			<img src="/user-regular.svg" alt="Profile" class="nav-icon" />
		</a>
	</nav>
</main>

<style>
	.bottom-nav {
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0;
		width: 100vw;
		display: flex;
		justify-content: center;
		gap: 2em;
		padding: 1em 0 1.2em 0;
		background: transparent;
		z-index: 100;
	}
	.nav-btn {
		background: #e0e0e0;
		border-radius: 50%;
		width: 48px;
		height: 48px;
		display: flex;
		align-items: center;
		justify-content: center;
		transition: background 0.2s;
	}
	.nav-btn.active {
		background: #111;
	}
	.nav-btn .nav-icon {
		width: 22px;
		height: 22px;
		filter: invert(0.3);
		transition: filter 0.2s;
	}
	.nav-btn.active .nav-icon {
		filter: invert(1);
	}
</style>
