import uuid


def _unique_email() -> str:
    return f"phase8_security_{uuid.uuid4().hex[:10]}@example.com"


def _register_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": password,
            "full_name": "Phase Eight Security User",
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


def _auth_headers(client, password: str = "Password123!"):
    email = _unique_email()

    register_response = _register_user(client, email, password)
    assert register_response.status_code in (200, 201), register_response.get_json()

    login_response = _login_user(client, email, password)
    assert login_response.status_code == 200, login_response.get_json()

    body = login_response.get_json() or {}
    data = body.get("data") or {}
    token = data.get("access_token") or data.get("token")
    assert token, body

    return email, {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json",
    }


def test_change_password(client):
    email, headers = _auth_headers(client)

    response = client.post(
        "/api/v1/security/change-password",
        headers=headers,
        json={
            "current_password": "Password123!",
            "new_password": "NewPassword123!",
        },
    )
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    login_response = _login_user(client, email, "NewPassword123!")
    assert login_response.status_code == 200, login_response.get_json()


def test_revoke_other_sessions(client):
    _, headers = _auth_headers(client)

    response = client.post(
        "/api/v1/security/revoke-other-sessions",
        headers=headers,
        json={},
    )
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert "revoked_sessions" in data