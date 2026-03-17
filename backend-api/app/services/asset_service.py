from app.repositories.asset_repository import AssetRepository


class AssetService:
    @staticmethod
    def list_assets(search: str | None = None, active: bool | None = None):
        return AssetRepository.list_assets(search=search, active=active)

    @staticmethod
    def get_asset(asset_id: int):
        return AssetRepository.get_by_id(asset_id)