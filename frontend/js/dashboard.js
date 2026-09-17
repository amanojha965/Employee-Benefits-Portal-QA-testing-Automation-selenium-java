/* ============================================================
   dashboard.js — dashboard page behaviour
   ============================================================ */

document.addEventListener("DOMContentLoaded", () => {
  requireLogin();

  const state = AppData.getState();
  if (!state) return;

  document.getElementById("employeeName").textContent = DEMO_ACCOUNT.name;
  document.getElementById("rewardPoints").textContent = state.points;
  document.getElementById("benefitsCount").textContent = DEMO_BENEFITS.length;

  const recentBody = document.getElementById("recentTransactionsBody");
  const recent = state.transactions.slice(0, 4);

  if (recent.length === 0) {
    recentBody.innerHTML = '<tr><td colspan="4" class="no-results">No transactions yet.</td></tr>';
  } else {
    recentBody.innerHTML = recent.map(tx => `
      <tr>
        <td>${tx.name}</td>
        <td>${tx.type}</td>
        <td>${tx.points}</td>
        <td><span class="status-badge status-success">${tx.status}</span></td>
      </tr>
    `).join("");
  }

  document.getElementById("navLogout").addEventListener("click", () => {
    AppData.clearState();
    window.location.href = "index.html";
  });
});
