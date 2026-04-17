import uuid

from app.extensions import db
from app.models.holding import Holding
from app.models.portfolio import Portfolio
from app.repositories.asset_repository import AssetRepository
from app.repositories.portfolio_repository import PortfolioRepository


def _unique_email() -> str:
    return f"phase6_trades_{uuid.uuid4().hex[:10]}@example.com"


def _register_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": password,
            "full_name": "Phase Six Trade User",
        },
    )


def _login_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/login",
        json={
            "email": email,
            "password": password,
        },
    )


def _auth_context(client):
    email = _unique_email()

    register_response = _register_user(client, email)
    assert register_response.status_code in (200, 201), register_response.get_json()

    login_response = _login_user(client, email)
    assert login_response.status_code == 200, login_response.get_json()

    body = login_response.get_json() or {}
    data = body.get("data") or {}
    token = data.get("access_token") or data.get("token")
    user = data.get("user") or {}
    user_id = user.get("id")
    assert token and user_id, body

    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json",
    }
    return headers, user_id


def _ensure_portfolio(user_id: int):
    portfolio = PortfolioRepository.get_primary_for_user(user_id)
    if portfolio:
        if portfolio.cash_balance < 10000:
            portfolio.cash_balance = 10000.0
            db.session.commit()
        return portfolio

    portfolio = Portfolio(
        user_id=user_id,
        name="Main Portfolio",
        cash_balance=10000.0,
    )
    db.session.add(portfolio)
    db.session.commit()
    return portfolio


def _seed_asset():
    suffix = uuid.uuid4().hex[:6].upper()
    return AssetRepository.create_asset(
        symbol=f"TR{suffix}",
        name=f"Trade Asset {suffix}",
        price=100.0,
        change_percent=1.8,
        is_active=True,
    )


def test_create_buy_trade(client):
    headers, user_id = _auth_context(client)
    portfolio = _ensure_portfolio(user_id)
    asset = _seed_asset()

    response = client.post(
        "/api/v1/trades",
        headers=headers,
        json={
            "asset_id": asset.id,
            "trade_type": "buy",
            "quantity": 5,
        },
    )
    body = response.get_json() or {}

    assert response.status_code in (200, 201), body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("trade_type") == "buy"
    assert data.get("asset_id") == asset.id

    holding = Holding.query.filter_by(portfolio_id=portfolio.id, asset_id=asset.id).first()
    assert holding is not None
    assert holding.quantity == 5


def test_create_sell_trade(client):
    headers, user_id = _auth_context(client)
    portfolio = _ensure_portfolio(user_id)
    asset = _seed_asset()

    buy_response = client.post(
        "/api/v1/trades",
        headers=headers,
        json={
            "asset_id": asset.id,
            "trade_type": "buy",
            "quantity": 5,
        },
    )
    assert buy_response.status_code in (200, 201), buy_response.get_json()

    sell_response = client.post(
        "/api/v1/trades",
        headers=headers,
        json={
            "asset_id": asset.id,
            "trade_type": "sell",
            "quantity": 2,
        },
    )
    body = sell_response.get_json() or {}

    assert sell_response.status_code in (200, 201), body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("trade_type") == "sell"

    holding = Holding.query.filter_by(portfolio_id=portfolio.id, asset_id=asset.id).first()
    assert holding is not None
    assert holding.quantity == 3