# Stock Investment App

A modern Android-first fintech platform that helps users learn investing, discover assets, manage watchlists, monitor portfolios, and execute responsible trade actions through a secure and beginner-friendly mobile experience.

## Product Vision

We are building a mobile investment platform designed for the next generation of investors. The app combines financial education, intuitive market discovery, portfolio visibility, and guided trade execution into one trusted experience.

Our goal is not just to let users trade, but to help them invest responsibly.

## Core Value Proposition

- Beginner-friendly investing experience
- Secure onboarding and identity verification
- Market discovery with clean visual design
- Watchlist and portfolio tracking
- Guided buy/sell flows
- Responsible investing prompts and education
- Persistent authentication and trusted account management

## MVP Goal

The functional MVP will be delivered by the end of Phase 6.

The MVP must support:

- onboarding
- sign up and login
- profile setup
- KYC submission UI
- dashboard
- asset listing
- asset details
- watchlist
- portfolio summary
- buy/sell simulation or controlled trade request flow
- backend API integration
- persistent authentication

## Product Style Direction

The product should feel visually polished, premium, and modern, similar to the clean confidence of high-quality React + Tailwind web products, while still following Android-native design standards.

Design qualities we are targeting:

- bold but elegant typography
- generous spacing
- strong visual hierarchy
- card-based layout
- soft elevation and clean surfaces
- vibrant but trustworthy accent colors
- premium onboarding and dashboard feel
- modern charts, metrics, and guided action patterns

## Tech Stack

### Mobile
- Kotlin
- Jetpack Compose
- MVVM
- Retrofit
- Room
- Hilt
- Coroutines

### Backend
- Flask
- PostgreSQL
- SQLAlchemy
- Flask-JWT-Extended
- Alembic

## Project Structure

```text
stock-investment-app/
├── README.md
├── .gitignore
├── docs/
├── mobile-android/
├── backend-api/
└── assets/
