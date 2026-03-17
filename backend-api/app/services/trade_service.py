from app.extensions import db
from app.models.holding import Holding
from app.repositories.asset_repository import AssetRepository
from app.repositories.portfolio_repository import PortfolioRepository
from app.repositories.trade_repository import TradeRepository
from app.utils.enums import TradeStatus, TradeType


class TradeService:
    @staticmethod
    def create_trade(user_id: int, asset_id: int, trade_type: str, quantity: float):
        asset = AssetRepository.get_by_id(asset_id)
        if not asset:
            return None, "Asset not found."

        portfolio = PortfolioRepository.get_primary_for_user(user_id)
        if not portfolio:
            return None, "Portfolio not found."

        if quantity <= 0:
            return None, "Quantity must be greater than zero."

        holding = Holding.query.filter_by(
            portfolio_id=portfolio.id,
            asset_id=asset.id,
        ).first()
        trade_value = quantity * asset.price

        if trade_type == TradeType.BUY.value:
            if portfolio.cash_balance < trade_value:
                return None, "Insufficient cash balance."

            portfolio.cash_balance -= trade_value
            if holding:
                total_cost = (holding.quantity * holding.average_price) + trade_value
                holding.quantity += quantity
                holding.average_price = total_cost / holding.quantity
            else:
                holding = Holding(
                    portfolio_id=portfolio.id,
                    asset_id=asset.id,
                    quantity=quantity,
                    average_price=asset.price,
                )
                db.session.add(holding)

        elif trade_type == TradeType.SELL.value:
            if not holding or holding.quantity < quantity:
                return None, "Insufficient holdings to sell."

            holding.quantity -= quantity
            portfolio.cash_balance += trade_value

            if holding.quantity == 0:
                db.session.delete(holding)
        else:
            return None, "Invalid trade type."

        db.session.flush()

        trade = TradeRepository.create_trade(
            user_id=user_id,
            portfolio_id=portfolio.id,
            asset_id=asset.id,
            trade_type=trade_type,
            quantity=quantity,
            price=asset.price,
            status=TradeStatus.COMPLETED.value,
        )
        return trade, None