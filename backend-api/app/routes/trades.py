from flask import Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required

from app.schemas.trade_schema import TradeCreateSchema
from app.services.notification_service import NotificationService
from app.services.trade_service import TradeService
from app.utils.enums import NotificationType
from app.utils.response import error_response, success_response
from app.utils.validators import load_schema_or_errors

trades_bp = Blueprint("trades", __name__)
trade_create_schema = TradeCreateSchema()


@trades_bp.post("")
@jwt_required()
def create_trade():
    payload = request.get_json(silent=True) or {}
    data, errors = load_schema_or_errors(trade_create_schema, payload)
    if errors:
        return error_response("Validation failed.", errors, 400)

    trade, message = TradeService.create_trade(
        user_id=int(get_jwt_identity()),
        asset_id=data["asset_id"],
        trade_type=data["trade_type"],
        quantity=data["quantity"],
    )
    if message:
        return error_response(message, status_code=400)

    NotificationService.create_notification(
        user_id=int(get_jwt_identity()),
        title="Trade completed",
        body=f"Your {trade.trade_type} order for {trade.quantity} unit(s) of {trade.asset.symbol} was completed.",
        notification_type=NotificationType.TRADE.value,
    )

    return success_response("Trade submitted successfully.", trade.to_dict(), 201)