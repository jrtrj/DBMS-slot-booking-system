<script>
    

	import EventCard from '$lib/EventCard.svelte';
	import Header from '$lib/Header.svelte';
	import Navigation from '$lib/Navigation.svelte';
	import { onMount } from 'svelte';

	let approvedEvents = [];
	let loading = true;
	let error = '';

	onMount(async () => {
		try {
			const res = await fetch('http://localhost:8080/api/events/approved');
			if (!res.ok) throw new Error('Failed to fetch events');
			approvedEvents = await res.json();
		} catch (err) {
			error = err.message;
		} finally {
			loading = false;
		}
	});

</script>
<main>
	<Header h1="Welcome," h2="Home" showBell={true}/>

	<div class="add-event-container">
		<a href="/inbox" class="add-event-link">INBOX</a>
	</div>

	<div class="events-section">
		{#if loading}
			<p>Loading events...</p>
		{:else if error}
			<p style="color:red">{error}</p>
		{:else if approvedEvents.length === 0}
			<p>No approved events.</p>
		{:else}
			<h3 class="events-count">Approved Events ({approvedEvents.length})</h3>
			<div class="event-cards-list">
				{#each approvedEvents as event}
					<EventCard event_name={event.name} venue={event.venue} date={event.date} time={event.time} />
				{/each}
			</div>
		{/if}
	</div>
    <!-- <EventCard event_name="Hash" venue="RB Seminar" date="18-OCTober" time="11:00AM-12:00PM" /> -->

	<Navigation routepath="/adminhome" />
	
</main>
<style>
    .add-event-container {
		display: flex;
		justify-content: center;
		margin: 2em 0 1.5em 0;
	}
	.add-event-link {
		display: inline-block;
		padding: 0.7em 1.5em;
		border: 2px solid #111;
		border-radius: 1.5em;
		font-size: 1.1em;
		font-weight: 500;
		color: #111;
		text-decoration: none;
		background: #fafafa;
		transition: background 0.2s, color 0.2s;
	}
	.add-event-link:hover {
		background: #111;
		color: #fff;
	}
	.events-section {
		max-width: 600px;
		margin: 0 auto 2em auto;
		padding: 0 1em;
	}
	.events-count {
		margin-bottom: 1em;
		font-size: 1.2em;
		font-weight: 600;
	}
	.event-cards-list {
		display: flex;
		flex-direction: column;
		gap: 1.2em;
	}
</style>