from app import create_app
from app.extensions import db
from app.models.asset import Asset


def test_assets_list_and_detail():
    app = create_app("testing")

    with app.app_context():
        db.create_all()

        asset = Asset(
            symbol="ABC",
            name="ABC Holdings",
            asset_type="stock",
            exchange="USE",
            currency="UGX",
            price=1200.50,
            change_percent=1.8,
            description="Seed asset for tests.",
        )
        db.session.add(asset)
        db.session.commit()

        client = app.test_client()

        list_response = client.get("/api/v1/assets")
        assert list_response.status_code == 200
        list_data = list_response.get_json()
        assert list_data["success"] is True
        assert len(list_data["data"]["assets"]) == 1

        detail_response = client.get(f"/api/v1/assets/{asset.id}")
        assert detail_response.status_code == 200
        detail_data = detail_response.get_json()
        assert detail_data["data"]["symbol"] == "ABC"

        db.drop_all()