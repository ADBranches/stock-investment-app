from app.repositories.user_repository import UserRepository


class UserService:
    @staticmethod
    def get_user_profile(user_id: int):
        return UserRepository.find_by_id(user_id)

    @staticmethod
    def update_user_profile(user_id: int, data: dict):
        user = UserRepository.find_by_id(user_id)
        if not user:
            return None
        return UserRepository.update_user(user, **data)