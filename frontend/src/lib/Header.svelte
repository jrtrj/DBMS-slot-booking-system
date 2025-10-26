<script>

	let {h1,h2,showBell=true} = $props()
	// Notification logic
	let notifications = [
		{ id: 1, message: 'New booking request', read: false },
		{ id: 2, message: 'Venue approved', read: true }
	];

	// Returns true if there are any unread notifications
	function hasUnread() {
		return notifications.some((n) => !n.read);
	}
</script>

<main>
	<div class="header-row">
		<div class="header-title">
			<h1>{h1}</h1>
			<div class="header-separator"></div>
			<h2>{h2}</h2>
		</div>
		{#if showBell}
			<a href="/notifications" class="bell-btn">
				<img src="/bell.svg" alt="Notifications" class="bell-icon" />
				{#if hasUnread()}
					<span class="notif-dot"></span>
				{/if}
			</a>
		{/if}
	</div>
</main>

<style>
	.header-row {
		display: flex;
		align-items: center;
		justify-content: space-between;
		width: 100%;
		margin-bottom: 1em;
	}
	.header-title {
		position: relative;
		top: 1em;
		margin-left: 1em;
	}
	h1 {
		font-size: 2.5em;
		font-weight: 900;
	}

	h2 {
		position: relative;
		bottom: 0.4em;
		left: 0.1em;
		font-size: 1.2em;
	}
	.bell-btn {
		position: relative;
		background: #e0e0e0;
		border-radius: 50%;
		width: 40px;
		height: 40px;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-left: auto;
		transition: background 0.2s;
		margin-right: 0.8em;
	}
	.bell-icon {
		width: 20px;
		height: 20px;
		display: block;
	}
	.notif-dot {
		position: absolute;
		right: 8px;
		bottom: 7px;
		width: 9px;
		height: 9px;
		background: #e53935;
		border-radius: 50%;
		border: 2px solid #fff;
		z-index: 2;
	}

	.header-separator {
        position: relative;
        bottom: 0.5em;
		width: 80%;
		height: 2.5px;
		background: #7d7d7d;
		margin: 0.3em 0 0.3em 0;
		border: none;
		border-radius: 2px;
	}
</style>
