from app import create_app
from app.extensions import db


def test_health_check():
    app = create_app("testing")

    with app.app_context():
        db.create_all()
        client = app.test_client()

        response = client.get("/api/v1/health")
        data = response.get_json()

        assert response.status_code == 200
        assert data["status"] == "ok"

        db.drop_all()