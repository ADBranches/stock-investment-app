from flask import Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required

from app.services.settings_service import SettingsService
from app.utils.response import success_response

settings_bp = Blueprint("settings", __name__)


@settings_bp.get("")
@jwt_required()
def get_settings():
    user_id = int(get_jwt_identity())
    data = SettingsService.get_settings(user_id)
    return success_response("Settings retrieved successfully.", data)


@settings_bp.patch("")
@jwt_required()
def update_settings():
    user_id = int(get_jwt_identity())
    payload = request.get_json(silent=True) or {}
    data = SettingsService.update_settings(user_id, payload)
    return success_response("Settings updated successfully.", data)