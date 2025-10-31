<script>
	import { goto } from "$app/navigation";

    function handleClick(){
        goto('/login')
    }
</script>

<div class="skillforce-tag">
	<h1 style="color:black">Book<span style="color:red;font-size:1.2em">my</span>Slot</h1>
</div>
<div class="container">
	<div class="container">
		<div class="top-row">
			<div class="top-spans">
				<span class="smart">BOOK.</span>
				<span class="matches">HOST.</span>
				<span class="seamless">CELEBRATE.</span>
				<span class="work">REPEAT.</span>
			</div>
			<img src="/mic.png" alt="Mic" class="mic-img" />
		</div>
		<div class="content"></div>
		<div class="button-row">
			<div class="button-container-relative">
				<img src="/desktop.png" alt="Desktop" class="desktop-img-behind" />
				<button class="my-button" on:click={handleClick}> Get Started </button>
			</div>
		</div>
		<img src="/people.png" alt="People" class="bottom-img" />
	</div>
</div>

<style>
	.skillforce-tag {
		width: 90%;
		text-align: center;
		font-size: 1.2rem;
		font-family: 'Roboto condensed', Arial, sans-serif;
		font-weight: 700;
		letter-spacing: 0.2em;
		color: #fff;
		padding: 0.5rem 0;
        position: fixed;
        top: 7rem;
        left:-1rem;
        font-size: 30px;
		margin-bottom: -6.5rem;
		box-sizing: border-box;
	}
	.container {
		margin-top: 0;
	}
	.button-container-relative {
		position: relative;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	.desktop-img-behind {
		position: absolute;
		left: -10rem;
		top: -7rem;
		transform: translate(-50%, 40%);
		width: 20rem;
		height: 20rem;
		object-fit: contain;
		z-index: 1;
		transform: scaleX(-1);
		opacity: 1;
		pointer-events: none;
	}
	.my-button {
		position: relative;
		z-index: 1;
	}
	.top-row {
		display: flex;
		flex-direction: row;
		align-items: flex-start;
		justify-content: flex-start;
		gap: 1rem;
		margin-top: 10rem;
		margin-bottom: 1.5rem;
		padding-left: 2vw;
	}
	.mic-img {
		width: 20rem;
		height: 20rem;
		object-fit: contain;
		margin-left: -9rem;
		margin-top: 5.5rem;
	}
	.button-row {
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: center;
		gap: 1rem;
		margin-bottom: 0.5rem;
	}
	/* .desktop-img {
		width: 15rem;
		height: 15rem;
		object-fit: contain;
		margin-right: 0.5rem;
	} */
	.top-spans {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		margin-top: 10rem;
		margin-bottom: 1.5rem;
		gap: 0.2em;
		padding-left: 2vw;
	}
	.smart,
	.seamless {
		font-size: 60px;
		font-family: 'Roboto Condensed';
		font-weight: 600;
	}
	.matches {
		color: #ff0000;
		font-weight: bold;
		font-size: 40px;
	}
	.work {
		color: #e40505;
		font-weight: bold;
		font-size: 40px;
	}
	.matches,
	.work {
		font-family: 'Roboto Mono';
	}
	.container {
		min-height: 100vh;
		display: flex;
		flex-direction: column;
	}
	.content {
		flex: 1;
	}
	.bottom-img {
		width: 98vw;
		height: 25vh;
		object-fit: cover;
		object-position: center bottom;
		margin-top: 5rem;
		display: block;
	}
	/* .button_container {
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 0.5rem;
        margin-left: -2.5rem;
	} */
	.my-button {
		background-color: black;
		display: flex;
		align-items: center;
		justify-content: center;
		color: white;
		padding: 10px 20px;
		font-size: 30px;
		font-family: 'Roboto Condensed';
		border: none;
		border-radius: 55px;
		cursor: pointer;
	}
	.my-button:hover {
		opacity: 0.85;
	}
</style>
