from app.repositories.user_repository import UserRepository
from app.utils.security import check_password, hash_password


class SecurityService:
    @staticmethod
    def change_password(user_id: int, current_password: str, new_password: str):
        user = UserRepository.find_by_id(user_id)
        if not user:
            return False, "User not found."

        if not check_password(user.password_hash, current_password):
            return False, "Current password is incorrect."

        if len(new_password) < 8:
            return False, "New password must be at least 8 characters."

        UserRepository.update_user(
            user,
            password_hash=hash_password(new_password),
        )
        return True, "Password changed successfully."

    @staticmethod
    def revoke_other_sessions(user_id: int) -> dict:
        return {
            "user_id": user_id,
            "revoked_sessions": 0,
            "note": "JWT session revocation is simulated in this MVP phase."
        }