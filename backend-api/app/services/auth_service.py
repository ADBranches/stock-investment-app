from flask_jwt_extended import create_access_token

from app.repositories.user_repository import UserRepository
from app.utils.security import check_password, hash_password


class AuthService:
    @staticmethod
    def register_user(email: str, password: str, full_name: str | None = None):
        existing_user = UserRepository.find_by_email(email)
        if existing_user:
            return None, "An account with that email already exists."

        user = UserRepository.create_user(
            email=email.lower(),
            password_hash=hash_password(password),
            full_name=full_name,
        )
        token = create_access_token(identity=str(user.id))
        return {
            "access_token": token,
            "user": user.to_dict(),
        }, None

    @staticmethod
    def login_user(email: str, password: str):
        user = UserRepository.find_by_email(email.lower())
        if not user or not check_password(user.password_hash, password):
            return None, "Invalid email or password."

        token = create_access_token(identity=str(user.id))
        return {
            "access_token": token,
            "user": user.to_dict(),
        }, None