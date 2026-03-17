# Backend API — Phase 3 Foundation

This backend provides the server-side foundation for the Stock Investment App.

## Included in this phase
- Flask application factory
- PostgreSQL-ready configuration
- SQLAlchemy and migrations setup
- JWT authentication setup
- CORS support
- Health, auth, and asset blueprint registration skeleton

## Setup
```bash
cd backend-api
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
cp .env.example .env
