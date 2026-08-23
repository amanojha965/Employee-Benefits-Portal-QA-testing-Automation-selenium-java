# Employee Benefits Portal

A beginner-level, fictional employee benefits/rewards web app, built as a
practice target for **Java + Selenium WebDriver + TestNG + Maven + Page
Object Model** automation.

> All benefit and reward brand names (Amazon, Zomato, MakeMyTrip, PVR, etc.)
> are used purely as realistic-looking demo content. They do **not**
> represent real partnerships or endorsements.

---

## 1. Project Objective

Demonstrate a working, hands-on understanding of:

- Building a simple multi-page HTML/CSS/JS web application
- Automating it end-to-end with **Selenium WebDriver**
- Structuring tests with the **Page Object Model (POM)**
- Running and organizing tests with **TestNG** and **Maven**

The app intentionally has **no backend, database, or framework** — it's just
static HTML/CSS/JS using `localStorage` to simulate a session, so the whole
project stays approachable for someone learning automation.

---

## 2. Features

### Login Page
- Email + password fields, login button, inline error message
- One demo account: `employee@test.com` / `Test@123`

### Dashboard
- Employee name, reward points, number of available benefits
- Recent transactions table
- Sidebar navigation: Dashboard, Benefits, Rewards, Transactions, Logout

### Benefits Page
- 10 demo benefits across 5 categories (Food, Shopping, Travel,
  Entertainment, Fitness)
- Search box + category dropdown filter
- "View details" modal and a "Redeem" button (deducts points, logs a
  transaction)

### Rewards Page
- Current point balance
- 5 demo rewards (Coffee, Movie, Shopping, Dining, Travel Vouchers)
- Redeem button with success / insufficient-points handling

### Transactions Page
- Table of past redemptions with a search field

Logging in always resets the demo data (750 starting points, a fixed set of
seed transactions), so both manual testing and automated tests always start
from the same, predictable state.

---

## 3. Technologies

| Layer            | Tech                                            |
|-------------------|--------------------------------------------------|
| Frontend          | HTML5, CSS3, vanilla JavaScript                  |
| Automation        | Java 11, Selenium WebDriver 4, TestNG, Maven      |
| Driver management | WebDriverManager (auto-downloads ChromeDriver)   |
| Design pattern    | Page Object Model (POM)                          |

---

## 4. Project Structure

```
employee-benefits-portal/
├── frontend/                     # The web application
│   ├── index.html                # Login page
│   ├── dashboard.html
│   ├── benefits.html
│   ├── rewards.html
│   ├── transactions.html
│   ├── css/
│   │   └── style.css
│   └── js/
│       ├── data.js               # Demo data + localStorage "session"
│       ├── auth.js               # Login page logic
│       ├── dashboard.js
│       ├── benefits.js
│       ├── rewards.js
│       └── transactions.js
│
└── automation/                   # Selenium + TestNG + Maven project
    ├── pom.xml
    ├── testng.xml
    ├── screenshots/               # Auto-created; failure screenshots land here
    └── src/test/
        ├── resources/
        │   └── config.properties # baseUrl, browser, wait timeout
        └── java/
            ├── pages/             # Page Object Model classes
            │   ├── LoginPage.java
            │   ├── DashboardPage.java
            │   ├── BenefitsPage.java
            │   ├── RewardsPage.java
            │   └── TransactionsPage.java
            ├── tests/             # TestNG test classes
            │   ├── BaseTest.java  # shared driver setup/teardown + screenshots
            │   ├── LoginTest.java
            │   ├── BenefitsTest.java
            │   ├── RewardsTest.java
            │   └── TransactionsTest.java
            └── utils/
                ├── DriverFactory.java
                ├── ConfigReader.java
                └── ScreenshotUtil.java
```

---

## 5. How to Run the Application

The app is static HTML/CSS/JS, so it just needs to be served over `http://`
(opening `index.html` directly as a `file://` URL works for manual browsing,
but a local server is recommended so `localStorage` behaves consistently for
Selenium).

**Option A — Python (already installed on most machines):**

```bash
cd employee-benefits-portal/frontend
python -m http.server 5500
```

Then open **http://localhost:5500** in your browser.

**Option B — VS Code "Live Server" extension:**
Right-click `frontend/index.html` → "Open with Live Server" (set it to port
`5500`, or update `automation/src/test/resources/config.properties`
accordingly).

Log in with:
- Email: `employee@test.com`
- Password: `Test@123`

---

## 6. How to Run the Selenium Tests

**Prerequisites:** Java 11+, Maven, and Google Chrome installed.

1. Make sure the frontend is running first (see step 5 above) at
   `http://localhost:5500`. If you used a different port, update
   `baseUrl` in `automation/src/test/resources/config.properties`.

2. From the `automation/` folder, run:

```bash
cd employee-benefits-portal/automation
mvn clean test
```

This uses the Surefire plugin configuration in `pom.xml`, which runs the
suite defined in `testng.xml` (all 4 test classes together).

To run a single test class instead:

```bash
mvn -Dtest=LoginTest test
```

Failure screenshots (if any) are saved automatically to
`automation/screenshots/`.

---

## 7. Automated Test Cases (18 total)

### LoginTest (4)
1. Verify successful login
2. Verify invalid email shows an error
3. Verify invalid password shows an error
4. Verify logout returns to the login page

### BenefitsTest (6)
5. Verify the Benefits page opens
6. Verify all 10 benefits are displayed
7. Search for a benefit by name
8. Filter benefits by category
9. Open a benefit's details modal
10. Redeem a benefit and see a success message

### RewardsTest (5)
11. Verify reward points are displayed
12. Verify all rewards are displayed
13. Successfully redeem a reward
14. Verify points are deducted after redemption
15. Verify insufficient-points validation on an unaffordable reward

### TransactionsTest (3)
16. Verify the Transactions page opens
17. Verify seeded transactions are displayed
18. Search for a transaction by name

---

## 8. Notes on Design Choices

- **No `Thread.sleep()`** — all waits use Selenium's `WebDriverWait` +
  `ExpectedConditions` (explicit waits) via each Page Object.
- **Stable locators** — mostly `id`, a few `css selector`s where a component
  repeats (e.g. `.benefit-card`); XPath is avoided entirely since nothing
  here needs it.
- **Deterministic state** — every successful login resets points and
  transactions to a fixed starting point, so tests never depend on the
  order they run in or on data left over from a previous run.
- **Screenshots on failure** — handled in `BaseTest#tearDown` via TestNG's
  `ITestResult`, no extra listener class needed.
