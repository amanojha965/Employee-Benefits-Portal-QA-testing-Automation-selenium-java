/* ============================================================
   data.js — demo data + localStorage-backed "session" for the
   Employee Benefits Portal. No backend/database is used; this
   file simulates one so the app behaves consistently for both
   manual use and Selenium automation.
   ============================================================ */

const DEMO_ACCOUNT = {
  email: "employee@test.com",
  password: "Test@123",
  name: "Priya Sharma",
  startingPoints: 750
};

const DEMO_BENEFITS = [
  { id: "b1", name: "Amazon Shopping", category: "Shopping", discount: "10% off", cost: 150,
    description: "Enjoy 10% off on eligible purchases across Amazon's shopping catalog." },
  { id: "b2", name: "Flipkart Shopping", category: "Shopping", discount: "8% off", cost: 120,
    description: "Save 8% on electronics, fashion, and home essentials on Flipkart." },
  { id: "b3", name: "Zomato Food", category: "Food", discount: "15% off", cost: 100,
    description: "Get 15% off your next food delivery order through Zomato." },
  { id: "b4", name: "Swiggy Food", category: "Food", discount: "12% off", cost: 90,
    description: "Enjoy 12% off on Swiggy orders from partner restaurants." },
  { id: "b5", name: "MakeMyTrip Travel", category: "Travel", discount: "20% off", cost: 300,
    description: "Book flights and hotels with 20% off through MakeMyTrip." },
  { id: "b6", name: "Ola Cabs Travel", category: "Travel", discount: "10% off", cost: 80,
    description: "Ride with 10% off your next few Ola cab bookings." },
  { id: "b7", name: "PVR Cinemas", category: "Entertainment", discount: "25% off", cost: 150,
    description: "Catch the latest releases with 25% off PVR Cinemas tickets." },
  { id: "b8", name: "Spotify Premium", category: "Entertainment", discount: "30% off", cost: 200,
    description: "Upgrade to Spotify Premium with a 30% discounted subscription." },
  { id: "b9", name: "Cult Fitness", category: "Fitness", discount: "15% off", cost: 180,
    description: "Access gym and fitness classes at 15% off with Cult Fitness." },
  { id: "b10", name: "Decathlon Fitness", category: "Fitness", discount: "10% off", cost: 100,
    description: "Gear up for your next workout with 10% off at Decathlon." }
];

const DEMO_REWARDS = [
  { id: "r1", name: "Coffee Voucher", cost: 100 },
  { id: "r2", name: "Movie Voucher", cost: 250 },
  { id: "r3", name: "Shopping Voucher", cost: 500 },
  { id: "r4", name: "Dining Voucher", cost: 750 },
  { id: "r5", name: "Travel Voucher", cost: 1000 }
];

const DEMO_TRANSACTIONS = [
  { name: "Coffee Voucher", type: "Reward", points: 100, status: "Success" },
  { name: "Amazon Shopping", type: "Benefit", points: 150, status: "Success" },
  { name: "Movie Voucher", type: "Reward", points: 250, status: "Success" },
  { name: "Zomato Food", type: "Benefit", points: 100, status: "Success" }
];

const STORAGE_KEY = "benefitsPortalState";

/** All fictional/demo data — this app has no real partnerships. */
const AppData = {

  /** Resets the app to a clean starting state (called on every successful login). */
  resetState() {
    const state = {
      loggedIn: true,
      points: DEMO_ACCOUNT.startingPoints,
      transactions: JSON.parse(JSON.stringify(DEMO_TRANSACTIONS))
    };
    localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
    return state;
  },

  getState() {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? JSON.parse(raw) : null;
  },

  saveState(state) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
  },

  clearState() {
    localStorage.removeItem(STORAGE_KEY);
  },

  isLoggedIn() {
    const state = this.getState();
    return !!(state && state.loggedIn);
  },

  addTransaction(name, type, points) {
    const state = this.getState();
    if (!state) return;
    state.transactions.unshift({ name, type, points, status: "Success" });
    this.saveState(state);
  },

  redeemPoints(cost) {
    const state = this.getState();
    if (!state) return { success: false };
    if (state.points < cost) {
      return { success: false, points: state.points };
    }
    state.points -= cost;
    this.saveState(state);
    return { success: true, points: state.points };
  }
};

/** Guards pages that require a logged-in session; redirects to login if not. */
function requireLogin() {
  if (!AppData.isLoggedIn()) {
    window.location.href = "index.html";
  }
}
