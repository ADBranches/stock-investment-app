from flask import Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required

from app.repositories.asset_repository import AssetRepository
from app.repositories.watchlist_repository import WatchlistRepository
from app.utils.response import error_response, success_response

watchlist_bp = Blueprint("watchlist", __name__)


@watchlist_bp.get("")
@jwt_required()
def get_watchlist():
    items = WatchlistRepository.list_for_user(int(get_jwt_identity()))
    return success_response(
        "Watchlist retrieved successfully.",
        {"items": [item.to_dict() for item in items]},
    )


@watchlist_bp.post("")
@jwt_required()
def add_to_watchlist():
    payload = request.get_json(silent=True) or {}
    asset_id = payload.get("asset_id")
    if not asset_id:
        return error_response("asset_id is required.", status_code=400)

    asset = AssetRepository.get_by_id(int(asset_id))
    if not asset:
        return error_response("Asset not found.", status_code=404)

    existing = WatchlistRepository.find_item(int(get_jwt_identity()), int(asset_id))
    if existing:
        return error_response("Asset already exists in watchlist.", status_code=409)

    item = WatchlistRepository.add_item(int(get_jwt_identity()), int(asset_id))
    return success_response("Asset added to watchlist successfully.", item.to_dict(), 201)


@watchlist_bp.delete("/<int:asset_id>")
@jwt_required()
def remove_from_watchlist(asset_id: int):
    item = WatchlistRepository.find_item(int(get_jwt_identity()), asset_id)
    if not item:
        return error_response("Watchlist item not found.", status_code=404)

    WatchlistRepository.remove_item(item)
    return success_response("Asset removed from watchlist successfully.")