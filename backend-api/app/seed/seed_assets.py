from app import create_app
from app.extensions import db
from app.models.asset import Asset

SEED_ASSETS = [
    {
        "symbol": "ABC",
        "name": "ABC Holdings",
        "asset_type": "stock",
        "exchange": "USE",
        "currency": "UGX",
        "price": 1200.50,
        "change_percent": 1.8,
        "description": "Starter seeded asset for development.",
    },
    {
        "symbol": "XYZ",
        "name": "XYZ Capital",
        "asset_type": "stock",
        "exchange": "USE",
        "currency": "UGX",
        "price": 840.00,
        "change_percent": -0.7,
        "description": "Starter seeded asset for development.",
    },
    {
        "symbol": "UGBOND1",
        "name": "Uganda Treasury Bond 1",
        "asset_type": "bond",
        "exchange": "OTC",
        "currency": "UGX",
        "price": 100.00,
        "change_percent": 0.2,
        "description": "Starter seeded bond asset for development.",
    },
]


def seed_assets():
    inserted = 0
    for item in SEED_ASSETS:
        existing = Asset.query.filter_by(symbol=item["symbol"]).first()
        if not existing:
            db.session.add(Asset(**item))
            inserted += 1

    db.session.commit()
    return inserted


if __name__ == "__main__":
    app = create_app()
    with app.app_context():
        inserted = seed_assets()
        print(f"Seed completed. Inserted {inserted} asset(s).")