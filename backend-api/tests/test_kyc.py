import uuid


def _unique_email() -> str:
    return f"phase4_kyc_{uuid.uuid4().hex[:10]}@example.com"


def _register_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": password,
            "full_name": "KYC Test User",
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


def test_submit_kyc(client):
    headers = _auth_headers(client)

    response = client.post(
        "/api/v1/kyc/submit",
        headers=headers,
        json={
            "first_name": "Phase",
            "last_name": "User",
            "national_id_number": "CM1234567890AA",
            "document_type": "national_id",
            "document_number": "ID-12345678",
        },
    )
    body = response.get_json() or {}

    assert response.status_code in (200, 201), body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("first_name") == "Phase"
    assert data.get("last_name") == "User"
    assert data.get("document_type") == "national_id"


def test_get_my_kyc(client):
    headers = _auth_headers(client)

    submit_response = client.post(
        "/api/v1/kyc/submit",
        headers=headers,
        json={
            "first_name": "Phase",
            "last_name": "User",
            "national_id_number": "CM1234567890AA",
            "document_type": "national_id",
            "document_number": "ID-12345678",
        },
    )
    assert submit_response.status_code in (200, 201), submit_response.get_json()

    response = client.get("/api/v1/kyc/me", headers=headers)
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True
    assert body.get("data") is not None


def test_update_my_kyc(client):
    headers = _auth_headers(client)

    submit_response = client.post(
        "/api/v1/kyc/submit",
        headers=headers,
        json={
            "first_name": "Phase",
            "last_name": "User",
            "national_id_number": "CM1234567890AA",
            "document_type": "national_id",
            "document_number": "ID-12345678",
        },
    )
    assert submit_response.status_code in (200, 201), submit_response.get_json()

    response = client.patch(
        "/api/v1/kyc/me",
        headers=headers,
        json={
            "first_name": "Updated",
            "last_name": "User",
            "national_id_number": "CM1234567890AA",
            "document_type": "passport",
            "document_number": "P-99887766",
        },
    )
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("first_name") == "Updated"
    assert data.get("document_type") == "passport"
    assert data.get("document_number") == "P-99887766"
    