<script>
	// let {event_name,venue,date,time}=$props();
</script>

<main>
	<div class="card">
		<h2>Event name</h2>
        <div class="dateVenue">
            <p>18 OCT -<span>11:00AM - 12:00PM</span></p>
            <p class="venue">RB Seminar hall</p>
        </div>
	</div>
</main>

<style>
	.card {
		width: 100%;
		max-width: 500px;
		margin: 0 auto;
		box-sizing: border-box;
		border: 0.1em solid black;
		/* Add horizontal padding for small screens */
		padding: 1em;
        border-radius: 10px;
        padding-left: 1em;
	}

    h2{
        font-weight: 600;
    }
    .venue{
        color: rgb(157, 157, 157);
        padding-left: 0.05em;
       
    }
    p{
         font-size: 0.9em;
    }
    
    .dateVenue{
        margin-top: 1em;
    }

	@media (max-width: 600px) {
		.card {
			width: calc(100vw - 2em); /* leave 1em space on each side */
			max-width: none;
			margin: 1em auto;
		}
	}

</style>
