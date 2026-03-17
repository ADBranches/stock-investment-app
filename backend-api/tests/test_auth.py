from app import create_app
from app.extensions import db


def test_register_and_login():
    app = create_app("testing")

    with app.app_context():
        db.create_all()
        client = app.test_client()

        register_response = client.post(
            "/api/v1/auth/register",
            json={
                "email": "user@example.com",
                "password": "securePassword123",
                "full_name": "John Investor",
            },
        )
        assert register_response.status_code == 201
        register_data = register_response.get_json()
        assert register_data["success"] is True
        assert "access_token" in register_data["data"]

        login_response = client.post(
            "/api/v1/auth/login",
            json={
                "email": "user@example.com",
                "password": "securePassword123",
            },
        )
        assert login_response.status_code == 200
        login_data = login_response.get_json()
        assert login_data["success"] is True
        assert login_data["data"]["user"]["email"] == "user@example.com"

        db.drop_all()