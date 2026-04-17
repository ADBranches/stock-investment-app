import uuid


def _unique_email() -> str:
    return f"phase8_settings_{uuid.uuid4().hex[:10]}@example.com"


def _register_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": password,
            "full_name": "Phase Eight Settings User",
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


def test_get_default_settings(client):
    headers = _auth_headers(client)

    response = client.get("/api/v1/settings", headers=headers)
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("privacy_accepted") is False
    assert data.get("analytics_consent") is False
    assert data.get("marketing_consent") is False


def test_update_settings(client):
    headers = _auth_headers(client)

    response = client.patch(
        "/api/v1/settings",
        headers=headers,
        json={
            "privacy_accepted": True,
            "analytics_consent": True,
            "marketing_consent": False,
        },
    )
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("privacy_accepted") is True
    assert data.get("analytics_consent") is True
    assert data.get("marketing_consent") is False


def test_update_account_profile(client):
    headers = _auth_headers(client)

    response = client.patch(
        "/api/v1/users/me/profile",
        headers=headers,
        json={
            "full_name": "Phase Eight Updated",
            "phone_number": "+256700111222",
            "country": "Uganda",
            "date_of_birth": "2001-01-01",
        },
    )
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("full_name") == "Phase Eight Updated"