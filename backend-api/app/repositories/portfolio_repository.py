from app.models.portfolio import Portfolio


class PortfolioRepository:
    @staticmethod
    def get_primary_for_user(user_id: int):
        return Portfolio.query.filter_by(user_id=user_id).order_by(Portfolio.id.asc()).first()

    @staticmethod
    def list_for_user(user_id: int):
        return Portfolio.query.filter_by(user_id=user_id).order_by(Portfolio.id.asc()).all()