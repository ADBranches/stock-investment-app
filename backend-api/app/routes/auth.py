from flask import Blueprint, request

from app.schemas.auth_schema import LoginSchema, RegisterSchema
from app.services.auth_service import AuthService
from app.utils.response import error_response, success_response
from app.utils.validators import load_schema_or_errors

auth_bp = Blueprint("auth", __name__)

register_schema = RegisterSchema()
login_schema = LoginSchema()


@auth_bp.post("/register")
def register():
    payload = request.get_json(silent=True) or {}
    data, errors = load_schema_or_errors(register_schema, payload)
    if errors:
        return error_response("Validation failed.", errors, 400)

    result, message = AuthService.register_user(
        email=data["email"],
        password=data["password"],
        full_name=data.get("full_name"),
    )
    if message:
        return error_response(message, status_code=409)

    return success_response("Account created successfully.", result, 201)


@auth_bp.post("/login")
def login():
    payload = request.get_json(silent=True) or {}
    data, errors = load_schema_or_errors(login_schema, payload)
    if errors:
        return error_response("Validation failed.", errors, 400)

    result, message = AuthService.login_user(
        email=data["email"],
        password=data["password"],
    )
    if message:
        return error_response(message, status_code=401)

    return success_response("Login successful.", result, 200)