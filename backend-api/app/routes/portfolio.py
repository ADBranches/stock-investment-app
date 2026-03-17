from flask import Blueprint
from flask_jwt_extended import get_jwt_identity, jwt_required

from app.services.portfolio_service import PortfolioService
from app.utils.response import error_response, success_response

portfolio_bp = Blueprint("portfolio", __name__)


@portfolio_bp.get("")
@jwt_required()
def get_portfolio_summary():
    summary = PortfolioService.get_portfolio_summary(int(get_jwt_identity()))
    if not summary:
        return error_response("Portfolio not found.", status_code=404)

    return success_response("Portfolio summary retrieved successfully.", summary)


@portfolio_bp.get("/transactions")
@jwt_required()
def get_transactions():
    summary = PortfolioService.get_portfolio_summary(int(get_jwt_identity()))
    if not summary:
        return error_response("Portfolio not found.", status_code=404)

    return success_response(
        "Portfolio transactions retrieved successfully.",
        {"trades": summary["trades"]},
    )