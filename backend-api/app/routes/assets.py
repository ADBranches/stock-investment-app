from flask import Blueprint, request

from app.schemas.asset_schema import AssetQuerySchema
from app.services.asset_service import AssetService
from app.utils.response import error_response, success_response
from app.utils.validators import load_schema_or_errors

assets_bp = Blueprint("assets", __name__)
asset_query_schema = AssetQuerySchema()


@assets_bp.get("")
def list_assets():
    active_raw = request.args.get("active")
    query_params = {
        "q": request.args.get("q"),
        "active": None if active_raw is None else active_raw.lower() == "true",
    }

    data, errors = load_schema_or_errors(asset_query_schema, query_params)
    if errors:
        return error_response("Invalid query parameters.", errors, 400)

    assets = AssetService.list_assets(
        search=data.get("q"),
        active=data.get("active"),
    )
    return success_response(
        "Assets retrieved successfully.",
        {"assets": [asset.to_dict() for asset in assets]},
    )


@assets_bp.get("/<int:asset_id>")
def get_asset(asset_id: int):
    asset = AssetService.get_asset(asset_id)
    if not asset:
        return error_response("Asset not found.", status_code=404)

    return success_response("Asset retrieved successfully.", asset.to_dict())