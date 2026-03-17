from app.extensions import db
from app.models.trade import Trade


class TradeRepository:
    @staticmethod
    def create_trade(**kwargs):
        trade = Trade(**kwargs)
        db.session.add(trade)
        db.session.commit()
        return trade

    @staticmethod
    def list_for_user(user_id: int):
        return Trade.query.filter_by(user_id=user_id).order_by(Trade.created_at.desc()).all()