from flask import Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required

from app.services.watchlist_service import WatchlistService
from app.utils.response import error_response, success_response

watchlist_bp = Blueprint("watchlist", __name__)


@watchlist_bp.get("")
@jwt_required()
def get_watchlist():
    items = WatchlistService.list_watchlist(int(get_jwt_identity()))
    return success_response(
        "Watchlist retrieved successfully.",
        {"items": items},
    )


@watchlist_bp.post("")
@jwt_required()
def add_to_watchlist():
    payload = request.get_json(silent=True) or {}
    asset_id = payload.get("asset_id")
    if not asset_id:
        return error_response("asset_id is required.", status_code=400)

    item, status_code, message = WatchlistService.add_asset(
        int(get_jwt_identity()),
        int(asset_id),
    )
    if item is None:
        return error_response(message, status_code=status_code)

    return success_response("Asset added to watchlist successfully.", item, 201)


@watchlist_bp.delete("/<int:asset_id>")
@jwt_required()
def remove_from_watchlist(asset_id: int):
    removed, status_code, message = WatchlistService.remove_asset(
        int(get_jwt_identity()),
        asset_id,
    )
    if not removed:
        return error_response(message, status_code=status_code)

    return success_response("Asset removed from watchlist successfully.")