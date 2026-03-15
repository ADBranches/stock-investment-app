# Architecture Overview

## Architecture Summary
The product follows a mobile client and backend API architecture.

- Android app acts as the primary client.
- Flask backend exposes REST APIs.
- PostgreSQL stores application data.
- Secure authentication is used for protected actions.

## High-Level Architecture

```text
Android App (Kotlin + Compose)
        |
        | REST API
        v
Flask Backend
        |
        | ORM / Services
        v
PostgreSQL Database