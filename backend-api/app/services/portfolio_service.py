from app.repositories.portfolio_repository import PortfolioRepository
from app.repositories.trade_repository import TradeRepository


class PortfolioService:
    @staticmethod
    def get_portfolio_summary(user_id: int):
        portfolio = PortfolioRepository.get_primary_for_user(user_id)
        if not portfolio:
            return None

        holdings = [holding.to_dict() for holding in portfolio.holdings]
        total_holdings_value = sum(item["market_value"] for item in holdings)
        total_value = portfolio.cash_balance + total_holdings_value
        trades = [trade.to_dict() for trade in TradeRepository.list_for_user(user_id)]

        return {
            "portfolio": portfolio.to_dict(),
            "total_value": total_value,
            "holdings_count": len(holdings),
            "holdings": holdings,
            "trades": trades,
        }