from sqlalchemy import or_

from app.extensions import db
from app.models.asset import Asset


class AssetRepository:
    @staticmethod
    def list_assets(search: str | None = None, active: bool | None = None):
        query = Asset.query

        if active is not None:
            query = query.filter(Asset.is_active == active)

        if search:
            query = query.filter(
                or_(
                    Asset.symbol.ilike(f"%{search}%"),
                    Asset.name.ilike(f"%{search}%"),
                )
            )

        return query.order_by(Asset.symbol.asc()).all()

    @staticmethod
    def get_by_id(asset_id: int):
        return Asset.query.get(asset_id)

    @staticmethod
    def get_by_symbol(symbol: str):
        return Asset.query.filter_by(symbol=symbol.upper()).first()

    @staticmethod
    def create_asset(**kwargs):
        asset = Asset(**kwargs)
        db.session.add(asset)
        db.session.commit()
        return asset