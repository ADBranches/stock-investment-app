from app.routes.auth import auth_bp
from app.routes.users import users_bp
from app.routes.assets import assets_bp
from app.routes.watchlist import watchlist_bp
from app.routes.portfolio import portfolio_bp
from app.routes.trades import trades_bp
from app.routes.kyc import kyc_bp
from app.routes.notifications import notifications_bp

__all__ = [
    "auth_bp",
    "users_bp",
    "assets_bp",
    "watchlist_bp",
    "portfolio_bp",
    "trades_bp",
    "kyc_bp",
    "notifications_bp",
]