/* ============================================================
   transactions.js — transactions page behaviour
   ============================================================ */

document.addEventListener("DOMContentLoaded", () => {
  requireLogin();

  const bodyEl = document.getElementById("transactionsBody");
  const searchBox = document.getElementById("searchTransaction");

  function renderTransactions() {
    const state = AppData.getState();
    const query = searchBox.value.trim().toLowerCase();

    const filtered = state.transactions.filter(tx =>
      !query || tx.name.toLowerCase().includes(query)
    );

    if (filtered.length === 0) {
      bodyEl.innerHTML = '<tr><td colspan="4" class="no-results">No transactions match your search.</td></tr>';
      return;
    }

    bodyEl.innerHTML = filtered.map(tx => `
      <tr>
        <td>${tx.name}</td>
        <td>${tx.type}</td>
        <td>${tx.points}</td>
        <td><span class="status-badge status-success">${tx.status}</span></td>
      </tr>
    `).join("");
  }

  searchBox.addEventListener("input", renderTransactions);

  document.getElementById("navLogout").addEventListener("click", () => {
    AppData.clearState();
    window.location.href = "index.html";
  });

  renderTransactions();
});
