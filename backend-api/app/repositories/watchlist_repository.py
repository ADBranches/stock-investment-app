from app.extensions import db
from app.models.watchlist import WatchlistItem


class WatchlistRepository:
    @staticmethod
    def list_for_user(user_id: int):
        return (
            WatchlistItem.query.filter_by(user_id=user_id)
            .order_by(WatchlistItem.created_at.desc())
            .all()
        )

    @staticmethod
    def find_item(user_id: int, asset_id: int):
        return WatchlistItem.query.filter_by(user_id=user_id, asset_id=asset_id).first()

    @staticmethod
    def add_item(user_id: int, asset_id: int):
        item = WatchlistItem(user_id=user_id, asset_id=asset_id)
        db.session.add(item)
        db.session.commit()
        return item

    @staticmethod
    def remove_item(item: WatchlistItem):
        db.session.delete(item)
        db.session.commit()