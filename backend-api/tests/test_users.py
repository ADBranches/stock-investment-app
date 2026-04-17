import uuid


def _unique_email() -> str:
    return f"phase4_users_{uuid.uuid4().hex[:10]}@example.com"


def _register_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": password,
            "full_name": "Phase Four User",
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


def test_get_current_user_profile(client):
    headers = _auth_headers(client)

    response = client.get("/api/v1/users/me", headers=headers)
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True
    assert body.get("data") is not None


def test_update_current_user_profile(client):
    headers = _auth_headers(client)

    response = client.patch(
        "/api/v1/users/me/profile",
        headers=headers,
        json={
            "full_name": "Phase Four Updated",
            "phone_number": "+256700000000",
            "country": "Uganda",
            "date_of_birth": "2000-01-01",
        },
    )
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("full_name") == "Phase Four Updated"
    assert data.get("phone_number") == "+256700000000"
    assert data.get("country") == "Uganda"
    assert data.get("date_of_birth") == "2000-01-01"