from flask import Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required

from app.services.security_service import SecurityService
from app.utils.response import error_response, success_response

security_bp = Blueprint("security", __name__)


@security_bp.post("/change-password")
@jwt_required()
def change_password():
    payload = request.get_json(silent=True) or {}
    current_password = (payload.get("current_password") or "").strip()
    new_password = (payload.get("new_password") or "").strip()

    if not current_password or not new_password:
        return error_response(
            "current_password and new_password are required.",
            status_code=400,
        )

    success, message = SecurityService.change_password(
        int(get_jwt_identity()),
        current_password,
        new_password,
    )
    if not success:
        return error_response(message, status_code=400)

    return success_response(message, None, 200)


@security_bp.post("/revoke-other-sessions")
@jwt_required()
def revoke_other_sessions():
    result = SecurityService.revoke_other_sessions(int(get_jwt_identity()))
    return success_response("Other sessions revoked successfully.", result, 200)