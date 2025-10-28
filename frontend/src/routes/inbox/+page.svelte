<script>
	import Header from '$lib/Header.svelte';
	import Navigation from '$lib/Navigation.svelte';
	import { onMount } from 'svelte';

	let pending = $state([]);
	let venues = $state([]);
	let clubs = $state([]);
	let loading = $state(true);
	let error = $state('');
	let rejectReason = $state('');
	let rejectId = $state(null);

	function getVenueName(id) {
		const v = venues.find((v) => v.id === id);
		return v ? v.name : id;
	}
	function getClubName(id) {
		const c = clubs.find((c) => c.id === id);
		return c ? c.name : id;
	}

	async function fetchAll() {
		loading = true;
		error = '';
		try {
			const [pendingRes, venuesRes, clubsRes] = await Promise.all([
				fetch('http://localhost:8080/api/bookings/pending'),
				fetch('http://localhost:8080/api/venues'),
				fetch('http://localhost:8080/api/clubs')
			]);
			if (!pendingRes.ok || !venuesRes.ok || !clubsRes.ok) throw new Error('Failed to fetch data');
			pending = await pendingRes.json();
			venues = await venuesRes.json();
			clubs = await clubsRes.json();
		} catch (err) {
			error = err.message;
		} finally {
			loading = false;
		}
	}

	onMount(fetchAll);

	async function approve(id) {
		try {
			const res = await fetch(`http://localhost:8080/api/bookings/approve/${id}`, {
				method: 'POST'
			});
			if (!res.ok) throw new Error('Failed to approve');
			pending = pending.filter((p) => p.id !== id);
		} catch (err) {
			alert('Error: ' + err.message);
		}
	}

	function startReject(id) {
		rejectId = id;
		rejectReason = '';
	}
	function cancelReject() {
		rejectId = null;
		rejectReason = '';
	}
	async function confirmReject() {
		try {
			const res = await fetch(`http://localhost:8080/api/bookings/reject/${rejectId}`, {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify({ reason: rejectReason })
			});
			if (!res.ok) throw new Error('Failed to reject');
			pending = pending.filter((p) => p.id !== rejectId);
			rejectId = null;
			rejectReason = '';
		} catch (err) {
			alert('Error: ' + err.message);
		}
	}

	function formatTime(t) {
		if (!t) return '';
		// Try to format ISO or HH:mm
		const d = new Date(t);
		if (!isNaN(d)) return d.toLocaleString();
		return t;
	}
</script>

<main>
    <Header h1="Inbox" h2="Pending Event Requests" showBell={true}/>
	<!-- <h1 class="pending-text">Pending Event Requests</h1> -->
	{#if loading}
		<p>Loading...</p>
	{:else if error}
		<p style="color:red">{error}</p>
	{:else if pending.length === 0}
		<p>No pending requests.</p>
	{:else}
		<div class="pending-list">
			{#each pending as req}
				<div class="pending-card">
					<h2>{req.eventTitle}</h2>
					<p><b>Description:</b> {req.eventDescription}</p>
					<p><b>When:</b> {formatTime(req.startTime)} - {formatTime(req.endTime)}</p>
					<p><b>Where:</b> {getVenueName(req.venueId)}</p>
					<p><b>Who:</b> {req.requesterId}</p>
					<p>
						<b>For:</b>
						{req.forClubId
							? getClubName(req.forClubId)
							: req.forDepartmentId
								? req.forDepartmentId
								: ''}
					</p>
					<div class="actions">
						<button class="approve-btn" onclick={() => approve(req.id)}>Approve</button>
						<button class="reject-btn" onclick={() => startReject(req.id)}>Reject</button>
					</div>
					{#if rejectId === req.id}
						<div class="reject-popup">
							<input type="text" placeholder="Reason for rejection" bind:value={rejectReason} />
							<button onclick={confirmReject}>Confirm</button>
							<button onclick={cancelReject}>Cancel</button>
						</div>
					{/if}
				</div>
			{/each}
		</div>
	{/if}

    <Navigation />
</main>

<style>
	main {
		max-width: 700px;
		margin: 0.5em auto;
		padding: 1em;
	}
	.pending-list {
		display: flex;
		flex-direction: column;
		gap: 1.5em;
	}
	.pending-card {
		border: 2px solid #000;
		border-radius: 1.2em;
		padding: 1.2em 1.5em;
		background: #fafbfc;
		box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.04);
	}
	.actions {
		margin-top: 1em;
		display: flex;
		gap: 1em;
	}
	.approve-btn {
		background: #1a7f37;
		color: #fff;
		border: none;
		border-radius: 0.7em;
		padding: 0.5em 1.2em;
		font-weight: 500;
		cursor: pointer;
	}
	.reject-btn {
		background: #e53935;
		color: #fff;
		border: none;
		border-radius: 0.7em;
		padding: 0.5em 1.2em;
		font-weight: 500;
		cursor: pointer;
	}
	.reject-popup {
		margin-top: 1em;
		display: flex;
		gap: 0.5em;
		align-items: center;
	}
	.reject-popup input {
		padding: 0.4em 0.8em;
		border-radius: 0.5em;
		border: 1px solid #aaa;
	}
	.reject-popup button {
		padding: 0.4em 0.9em;
		border-radius: 0.5em;
		border: none;
		font-weight: 500;
		cursor: pointer;
	}
</style>
