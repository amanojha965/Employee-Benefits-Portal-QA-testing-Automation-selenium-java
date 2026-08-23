/* ============================================================
   benefits.js — benefits page behaviour
   ============================================================ */

document.addEventListener("DOMContentLoaded", () => {
  requireLogin();

  const listEl = document.getElementById("benefitsList");
  const searchBox = document.getElementById("searchBenefit");
  const categoryFilter = document.getElementById("categoryFilter");
  const modalOverlay = document.getElementById("benefitModal");
  const modalTitle = document.getElementById("modalBenefitName");
  const modalBody = document.getElementById("modalBenefitBody");
  const modalCloseBtn = document.getElementById("modalCloseBtn");

  function renderBenefits() {
    const query = searchBox.value.trim().toLowerCase();
    const category = categoryFilter.value;

    const filtered = DEMO_BENEFITS.filter(b => {
      const matchesQuery = !query || b.name.toLowerCase().includes(query);
      const matchesCategory = category === "All" || b.category === category;
      return matchesQuery && matchesCategory;
    });

    if (filtered.length === 0) {
      listEl.innerHTML = '<p class="no-results">No benefits match your search.</p>';
      return;
    }

    listEl.innerHTML = filtered.map(b => `
      <div class="benefit-card" data-benefit-id="${b.id}" data-category="${b.category}">
        <span class="benefit-category-tag">${b.category}</span>
        <h3 class="benefit-name">${b.name}</h3>
        <div class="benefit-discount">${b.discount}</div>
        <p class="benefit-description">${b.description}</p>
        <p class="reward-cost">${b.cost} points</p>
        <div class="card-actions">
          <button class="details-btn" data-action="details" data-id="${b.id}">View details</button>
          <button class="redeem-btn" data-action="redeem" data-id="${b.id}">Redeem</button>
        </div>
        <div class="inline-message hidden" data-message-for="${b.id}"></div>
      </div>
    `).join("");
  }

  function openDetails(benefit) {
    modalTitle.textContent = benefit.name;
    modalBody.innerHTML = `
      <p><strong>Category:</strong> ${benefit.category}</p>
      <p><strong>Discount:</strong> ${benefit.discount}</p>
      <p><strong>Cost:</strong> ${benefit.cost} points</p>
      <p>${benefit.description}</p>
    `;
    modalOverlay.classList.remove("hidden");
  }

  function closeDetails() {
    modalOverlay.classList.add("hidden");
  }

  function redeemBenefit(benefit, cardEl) {
    const result = AppData.redeemPoints(benefit.cost);
    const msgEl = cardEl.querySelector(`[data-message-for="${benefit.id}"]`);

    if (result.success) {
      AppData.addTransaction(benefit.name, "Benefit", benefit.cost);
      msgEl.textContent = `Redeemed successfully! ${result.points} points remaining.`;
      msgEl.className = "inline-message success";
    } else {
      msgEl.textContent = "Insufficient points to redeem this benefit.";
      msgEl.className = "inline-message error";
    }
  }

  listEl.addEventListener("click", (e) => {
    const btn = e.target.closest("button");
    if (!btn) return;
    const id = btn.getAttribute("data-id");
    const benefit = DEMO_BENEFITS.find(b => b.id === id);
    if (!benefit) return;

    if (btn.getAttribute("data-action") === "details") {
      openDetails(benefit);
    } else if (btn.getAttribute("data-action") === "redeem") {
      redeemBenefit(benefit, btn.closest(".benefit-card"));
    }
  });

  modalCloseBtn.addEventListener("click", closeDetails);
  modalOverlay.addEventListener("click", (e) => {
    if (e.target === modalOverlay) closeDetails();
  });

  searchBox.addEventListener("input", renderBenefits);
  categoryFilter.addEventListener("change", renderBenefits);

  document.getElementById("navLogout").addEventListener("click", () => {
    AppData.clearState();
    window.location.href = "index.html";
  });

  renderBenefits();
});
