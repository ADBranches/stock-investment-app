from flask import Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required

from app.schemas.user_schema import UpdateUserSchema
from app.services.user_service import UserService
from app.utils.response import error_response, success_response
from app.utils.validators import load_schema_or_errors

users_bp = Blueprint("users", __name__)
update_user_schema = UpdateUserSchema()


@users_bp.get("/me")
@jwt_required()
def me():
    user = UserService.get_user_profile(int(get_jwt_identity()))
    if not user:
        return error_response("User not found.", status_code=404)
    return success_response("User profile retrieved successfully.", user.to_dict())


@users_bp.put("/profile")
@jwt_required()
def update_profile():
    payload = request.get_json(silent=True) or {}
    data, errors = load_schema_or_errors(update_user_schema, payload)
    if errors:
        return error_response("Validation failed.", errors, 400)

    user = UserService.update_user_profile(int(get_jwt_identity()), data)
    if not user:
        return error_response("User not found.", status_code=404)

    return success_response("Profile updated successfully.", user.to_dict())