<script>
	import { onMount } from 'svelte';
	import Header from '$lib/Header.svelte';
	import Navigation from '$lib/Navigation.svelte';

	// let eventName = $state('');
	// let venue = $state('');
	let date = $state('');
	let timeFrom = $state('');
	let timeTo = $state('');
	let venues = $state([]);
	let venuesLoading = $state(true);
	let venuesError = $state('');

	onMount(async () => {
		try {
			const res = await fetch('http://localhost:8080/api/venues');
			if (!res.ok) throw new Error('Failed to fetch venues');
			venues = await res.json();
			venuesLoading = false;
		} catch (err) {
			venuesError = err.message;
			venuesLoading = false;
		}
	});
	let eventTitle = $state('');
	let eventDescription = $state('');
	let venueId = $state('');

	onMount(async () => {
		try {
			const res = await fetch('http://localhost:8080/api/venues');
			if (!res.ok) throw new Error('Failed to fetch venues');
			venues = await res.json();
			venuesLoading = false;
		} catch (err) {
			venuesError = err.message;
			venuesLoading = false;
		}
	});

	function buildDateTime(date, time) {
		if (!date || !time) return '';
		return date + 'T' + time;
	}

	async function handleSubmit(e) {
		e.preventDefault();
		const payload = {
			eventTitle,
			eventDescription,
			startTime: buildDateTime(date, timeFrom),
			endTime: buildDateTime(date, timeTo),
			venueId: Number(venueId)
		};
		try {
			const res = await fetch('http://localhost:8080/api/bookings/request', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(payload)
			});
			if (!res.ok) {
				throw new Error('Failed to submit event');
			}
			eventTitle = '';
			eventDescription = '';
			venueId = '';
			date = '';
			timeFrom = '';
			timeTo = '';
			alert('Event submitted successfully!');
		} catch (err) {
			alert('Error: ' + err.message);
		}
	}
</script>

<main>
	<Header h1="NEW EVENT!" h2="Add details" showBell={true} />
	<form class="request-form" onsubmit={handleSubmit}>
		<label class="input-label" for="eventTitle">Event Name</label>
		<input
			id="eventTitle"
			type="text"
			placeholder="Event Name"
			bind:value={eventTitle}
			class="input-field"
			required
		/>

		<label class="input-label" for="eventDescription">Event Description</label>
		<input
			id="eventDescription"
			type="text"
			placeholder="Event Description"
			bind:value={eventDescription}
			class="input-field"
			required
		/>

		<label class="input-label" for="venueId">Venue</label>
		<select id="venueId" bind:value={venueId} class="input-field" required>
			<option value="" disabled selected>Select Venue</option>
			{#if venuesLoading}
				<option disabled>Loading venues...</option>
			{:else if venuesError}
				<option disabled>{venuesError}</option>
			{:else}
				{#each venues as v}
					<option value={v.id}>{v.name}</option>
				{/each}
			{/if}
		</select>

		<label class="input-label" for="date">Date</label>
		<input id="date" type="date" bind:value={date} class="input-field" required />

		<div class="time-range-group">
			<label class="input-label" for="timeFrom">From</label>
			<input id="timeFrom" type="time" bind:value={timeFrom} class="input-field" required />
			<label class="input-label" for="timeTo">To</label>
			<input id="timeTo" type="time" bind:value={timeTo} class="input-field" required />
		</div>

		<button type="submit" class="submit-btn">Submit</button>

	</form>
	<!-- <Navigation role ="user"/> -->
</main>

<style>
	.time-range-group {
		display: flex;
		flex-direction: row;
		align-items: center;
		gap: 1em;
		width: 100%;
		justify-content: space-between;
	}
	.time-range-group .input-label {
		margin: 0;
		min-width: 2.5em;
	}
	.time-range-group .input-field {
		flex: 1 1 40%;
		min-width: 0;
	}
	.request-form {
		display: flex;
		flex-direction: column;
		gap: 1.5em;
		max-width: 600px;
		margin: 0em auto;
		padding: 1em 1.5em;
		/* background: #fff; */
		border-radius: 1.2em;
		/* box-shadow: 0 2px 16px 0 rgba(0,0,0,0.07); */
	}
	.input-label {
		font-size: 1em;
		font-weight: 500;
		margin-bottom: 0.2em;
		margin-top: 0.2em;
		color: #222;
	}
	.input-field {
		padding: 0.6em 1em;
		border: 1px solid #363636;
		border-radius: 0.5em;
		font-size: 1em;
		outline: none;
		background: #fafafa;
		transition: border 0.2s;
	}
	.input-field:focus {
		border: 1.5px solid #111;
	}
	.submit-btn {
		padding: 0.7em 0;
		background: #111;
		color: #fff;
		border: none;
		border-radius: 1em;
		font-size: 1.1em;
		font-weight: 500;
		cursor: pointer;
		transition: background 0.2s;
	}
	.submit-btn:hover {
		background: #222;
	}
</style>
