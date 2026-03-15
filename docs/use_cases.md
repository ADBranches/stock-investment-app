# Use Cases

## Primary Actor
User

## Secondary Actors
- Admin or compliance staff
- Backend system
- Notification service

---

## Use Case 1: View Onboarding
**Actor:** User  
**Goal:** Understand the app before creating an account

**Main Flow:**
1. User opens the app.
2. App displays splash screen.
3. App shows onboarding screens.
4. User proceeds or skips.

---

## Use Case 2: Register Account
**Actor:** User  
**Goal:** Create an account

**Main Flow:**
1. User opens sign-up screen.
2. User enters email and password.
3. User accepts terms.
4. App sends registration request.
5. App confirms account creation.

---

## Use Case 3: Log In
**Actor:** User  
**Goal:** Access the system

**Main Flow:**
1. User enters credentials.
2. App validates input.
3. Backend authenticates user.
4. Token is returned.
5. User enters authenticated area.

---

## Use Case 4: Complete Profile Setup
**Actor:** User  
**Goal:** Personalize account and prepare for investing

**Main Flow:**
1. User enters profile details.
2. App validates fields.
3. Backend saves profile.
4. User continues to dashboard or KYC.

---

## Use Case 5: Submit KYC
**Actor:** User  
**Goal:** Complete identity verification

**Main Flow:**
1. User opens KYC flow.
2. User fills personal information.
3. User uploads required documents.
4. User reviews data.
5. User submits KYC.

---

## Use Case 6: Browse Assets
**Actor:** User  
**Goal:** Discover investment opportunities

**Main Flow:**
1. User opens market screen.
2. App shows available assets.
3. User searches or filters.
4. User taps an asset.
5. App shows asset details.

---

## Use Case 7: Manage Watchlist
**Actor:** User  
**Goal:** Save assets of interest

**Main Flow:**
1. User views an asset.
2. User adds asset to watchlist.
3. App saves the item.
4. User views watchlist later.

---

## Use Case 8: View Portfolio
**Actor:** User  
**Goal:** Track investment position

**Main Flow:**
1. User opens portfolio screen.
2. App displays holdings, summary, and history.

---

## Use Case 9: Place Trade
**Actor:** User  
**Goal:** Simulate or request buy/sell action

**Main Flow:**
1. User opens trade screen.
2. User selects buy or sell.
3. User enters quantity.
4. App shows summary.
5. User confirms action.
6. Backend processes request or simulation.
7. Result is shown.

---

## Use Case 10: Restore Session
**Actor:** User  
**Goal:** Continue using the app without repeated login

**Main Flow:**
1. User reopens app.
2. App checks saved auth token.
3. If token is valid, user is taken into the app.