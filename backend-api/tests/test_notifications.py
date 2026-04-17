import uuid

from app.services.notification_service import NotificationService


def _unique_email() -> str:
    return f"phase7_notifications_{uuid.uuid4().hex[:10]}@example.com"


def _register_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": password,
            "full_name": "Phase Seven Notification User",
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


def test_list_notifications(client):
    headers, user_id = _auth_context(client)

    NotificationService.create_notification(
        user_id=user_id,
        title="Trade completed",
        body="Your recent trade has been completed.",
        notification_type="trade",
    )

    response = client.get("/api/v1/notifications", headers=headers)
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    items = data.get("items") or []
    assert len(items) >= 1


def test_mark_notification_read(client):
    headers, user_id = _auth_context(client)

    notification = NotificationService.create_notification(
        user_id=user_id,
        title="KYC submitted",
        body="Your KYC review is pending.",
        notification_type="kyc",
    )

    response = client.patch(
        f"/api/v1/notifications/{notification.id}/read",
        headers=headers,
    )
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("id") == notification.id
    assert data.get("is_read") is True