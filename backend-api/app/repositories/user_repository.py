from app.extensions import db
from app.models.portfolio import Portfolio
from app.models.user import User


class UserRepository:
    @staticmethod
    def find_by_id(user_id: int):
        return User.query.get(user_id)

    @staticmethod
    def find_by_email(email: str):
        return User.query.filter_by(email=email.lower()).first()

    @staticmethod
    def create_user(**kwargs):
        user = User(**kwargs)
        db.session.add(user)
        db.session.flush()

        portfolio = Portfolio(
            user_id=user.id,
            name="Main Portfolio",
            cash_balance=100000.0,
        )
        db.session.add(portfolio)
        db.session.commit()
        return user

    @staticmethod
    def update_user(user: User, **kwargs):
        for key, value in kwargs.items():
            setattr(user, key, value)
        user.profile_completed = True
        db.session.commit()
        return user