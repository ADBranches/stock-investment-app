# Functional Requirements

## 1. Onboarding
- The system shall display a splash screen on launch.
- The system shall display onboarding screens to first-time users.
- The system shall allow users to skip or complete onboarding.

## 2. Authentication
- The system shall allow users to register with email and password.
- The system shall allow users to log in.
- The system shall validate incorrect login credentials.
- The system shall persist authenticated sessions.
- The system shall allow users to log out.

## 3. Profile Setup
- The system shall allow newly registered users to complete profile setup.
- The system shall collect basic identity information.
- The system shall allow profile editing in later phases.

## 4. KYC
- The system shall provide a KYC introduction screen.
- The system shall collect personal verification details.
- The system shall allow document upload UI.
- The system shall allow KYC review before submission.
- The system shall show KYC success or submission feedback.

## 5. Dashboard
- The system shall display a user dashboard after authentication.
- The dashboard shall show welcome information.
- The dashboard shall show a summary of watchlist and portfolio data.
- The dashboard shall highlight market activity and key actions.

## 6. Market Discovery
- The system shall display a list of available assets.
- The system shall allow search and filter.
- The system shall allow access to asset details.
- The system shall display price, symbol, and summary information.

## 7. Watchlist
- The system shall allow users to add assets to a watchlist.
- The system shall allow users to remove assets from a watchlist.
- The system shall display all saved watchlist items.

## 8. Portfolio
- The system shall display a portfolio overview.
- The system shall display holdings summary.
- The system shall display transaction records.
- The system shall display balance and allocation summary.

## 9. Trade Flow
- The system shall allow buy and sell actions through a simulation or controlled request flow.
- The system shall allow quantity input.
- The system shall display a confirmation step.
- The system shall display trade result feedback.

## 10. Backend Integration
- The system shall communicate with backend APIs for auth, user profile, assets, watchlist, portfolio, KYC, and trade actions.
- The system shall handle loading, success, and error responses.

## 11. Session Persistence
- The system shall store authentication tokens securely.
- The system shall automatically restore sessions where valid.