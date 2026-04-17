from app.repositories.asset_repository import AssetRepository
from app.repositories.watchlist_repository import WatchlistRepository


class WatchlistService:
    @staticmethod
    def _serialize_item(item):
        asset = AssetRepository.get_by_id(item.asset_id)

        asset_dict = {}
        if asset and hasattr(asset, "to_dict"):
            asset_dict = asset.to_dict() or {}

        return {
            "asset_id": item.asset_id,
            "symbol": asset_dict.get("symbol", getattr(asset, "symbol", "")),
            "name": asset_dict.get("name", getattr(asset, "name", "")),
            "price": asset_dict.get("price", getattr(asset, "price", 0.0)),
        }

    @staticmethod
    def list_watchlist(user_id: int):
        items = WatchlistRepository.list_for_user(user_id)
        return [WatchlistService._serialize_item(item) for item in items]

    @staticmethod
    def add_asset(user_id: int, asset_id: int):
        asset = AssetRepository.get_by_id(asset_id)
        if not asset:
            return None, 404, "Asset not found."

        existing = WatchlistRepository.find_item(user_id, asset_id)
        if existing:
            return None, 409, "Asset already exists in watchlist."

        item = WatchlistRepository.add_item(user_id, asset_id)
        return WatchlistService._serialize_item(item), 201, "Asset added to watchlist successfully."

    @staticmethod
    def remove_asset(user_id: int, asset_id: int):
        item = WatchlistRepository.find_item(user_id, asset_id)
        if not item:
            return False, 404, "Watchlist item not found."

        WatchlistRepository.remove_item(item)
        return True, 200, "Asset removed from watchlist successfully."