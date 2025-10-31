import { writable, get } from 'svelte/store';
import { browser } from '$app/environment';

// Get the stored user from localStorage, if it exists
const storedUser = browser ? localStorage.getItem('currentUser') : null;

// Create a writable store, initializing with the stored user
const store = writable(storedUser ? JSON.parse(storedUser) : null);

// Create a custom store that updates localStorage on every change
export const user = {
	subscribe: store.subscribe,

	login: (userData) => {
		// 1. Save user data to localStorage
		if (browser) {
			localStorage.setItem('currentUser', JSON.stringify(userData));
		}
		// 2. Set the store value
		store.set(userData);
	},

	logout: () => {
		// 1. Remove user from localStorage
		if (browser) {
			localStorage.removeItem('currentUser');
		}
		// 2. Set the store to null
		store.set(null);
	},

	// Helper to get the current user's ID
	getId: () => {
		const currentUser = get(store);
		return currentUser ? currentUser.id : null;
	}
};
