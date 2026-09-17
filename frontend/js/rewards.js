/* ============================================================
   rewards.js — rewards page behaviour
   ============================================================ */

document.addEventListener("DOMContentLoaded", () => {
  requireLogin();

  const pointsEl = document.getElementById("currentPoints");
  const listEl = document.getElementById("rewardsList");

  function refreshPoints() {
    const state = AppData.getState();
    pointsEl.textContent = state.points;
  }

  function renderRewards() {
    listEl.innerHTML = DEMO_REWARDS.map(r => `
      <div class="reward-card" data-reward-id="${r.id}">
        <h3 class="reward-name">${r.name}</h3>
        <p class="reward-cost">${r.cost} points</p>
        <button class="redeem-btn" data-id="${r.id}">Redeem</button>
        <div class="inline-message hidden" data-message-for="${r.id}"></div>
      </div>
    `).join("");
  }

  listEl.addEventListener("click", (e) => {
    const btn = e.target.closest("button.redeem-btn");
    if (!btn) return;
    const id = btn.getAttribute("data-id");
    const reward = DEMO_REWARDS.find(r => r.id === id);
    if (!reward) return;

    const result = AppData.redeemPoints(reward.cost);
    const msgEl = btn.closest(".reward-card").querySelector(`[data-message-for="${reward.id}"]`);

    if (result.success) {
      AppData.addTransaction(reward.name, "Reward", reward.cost);
      msgEl.textContent = `Success! ${reward.name} redeemed. ${result.points} points remaining.`;
      msgEl.className = "inline-message success";
      refreshPoints();
    } else {
      msgEl.textContent = "Insufficient points to redeem this reward.";
      msgEl.className = "inline-message error";
    }
  });

  document.getElementById("navLogout").addEventListener("click", () => {
    AppData.clearState();
    window.location.href = "index.html";
  });

  refreshPoints();
  renderRewards();
});
