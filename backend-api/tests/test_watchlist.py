import uuid

from app.repositories.asset_repository import AssetRepository


def _unique_email() -> str:
    return f"phase5_watchlist_{uuid.uuid4().hex[:10]}@example.com"


def _register_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": password,
            "full_name": "Phase Five User",
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


def _auth_headers(client):
    email = _unique_email()

    register_response = _register_user(client, email)
    assert register_response.status_code in (200, 201), register_response.get_json()

    login_response = _login_user(client, email)
    assert login_response.status_code == 200, login_response.get_json()

    body = login_response.get_json() or {}
    data = body.get("data") or {}
    token = data.get("access_token") or data.get("token")
    assert token, body

    return {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json",
    }


def _seed_asset():
    suffix = uuid.uuid4().hex[:6].upper()
    return AssetRepository.create_asset(
        symbol=f"WL{suffix}",
        name=f"Watchlist Asset {suffix}",
        price=150.75,
        change_percent=3.2,
        is_active=True,
    )


def test_add_to_watchlist(client):
    headers = _auth_headers(client)
    asset = _seed_asset()

    response = client.post(
        "/api/v1/watchlist",
        headers=headers,
        json={"asset_id": asset.id},
    )
    body = response.get_json() or {}

    assert response.status_code in (200, 201), body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("asset_id") == asset.id
    assert data.get("symbol") == asset.symbol


def test_get_watchlist(client):
    headers = _auth_headers(client)
    asset = _seed_asset()

    add_response = client.post(
        "/api/v1/watchlist",
        headers=headers,
        json={"asset_id": asset.id},
    )
    assert add_response.status_code in (200, 201), add_response.get_json()

    response = client.get("/api/v1/watchlist", headers=headers)
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    items = data.get("items") or []
    assert any(item.get("asset_id") == asset.id for item in items), items


def test_remove_from_watchlist(client):
    headers = _auth_headers(client)
    asset = _seed_asset()

    add_response = client.post(
        "/api/v1/watchlist",
        headers=headers,
        json={"asset_id": asset.id},
    )
    assert add_response.status_code in (200, 201), add_response.get_json()

    response = client.delete(f"/api/v1/watchlist/{asset.id}", headers=headers)
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True