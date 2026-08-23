/* ============================================================
   auth.js — login page behaviour
   ============================================================ */

document.addEventListener("DOMContentLoaded", () => {
  // If already logged in, skip straight to the dashboard.
  if (AppData.isLoggedIn()) {
    window.location.href = "dashboard.html";
    return;
  }

  const emailField = document.getElementById("email");
  const passwordField = document.getElementById("password");
  const loginBtn = document.getElementById("loginBtn");
  const errorMessage = document.getElementById("errorMessage");

  function showError(text) {
    errorMessage.textContent = text;
    errorMessage.classList.remove("hidden");
  }

  function hideError() {
    errorMessage.classList.add("hidden");
  }

  function attemptLogin() {
    hideError();
    const email = emailField.value.trim();
    const password = passwordField.value;

    if (!email || !password) {
      showError("Please enter both email and password.");
      return;
    }

    if (email !== DEMO_ACCOUNT.email || password !== DEMO_ACCOUNT.password) {
      showError("Invalid email or password. Please try again.");
      return;
    }

    // Fresh, deterministic state on every successful login — this keeps
    // manual testing and Selenium automation predictable.
    AppData.resetState();
    window.location.href = "dashboard.html";
  }

  loginBtn.addEventListener("click", attemptLogin);

  passwordField.addEventListener("keyup", (e) => {
    if (e.key === "Enter") attemptLogin();
  });
});
