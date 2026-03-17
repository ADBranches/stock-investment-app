from flask import Blueprint
from flask_jwt_extended import get_jwt_identity, jwt_required

from app.services.notification_service import NotificationService
from app.utils.response import error_response, success_response

notifications_bp = Blueprint("notifications", __name__)


@notifications_bp.get("")
@jwt_required()
def get_notifications():
    notifications = NotificationService.list_notifications(int(get_jwt_identity()))
    return success_response(
        "Notifications retrieved successfully.",
        {"items": [item.to_dict() for item in notifications]},
    )


@notifications_bp.patch("/<int:notification_id>/read")
@jwt_required()
def mark_notification_read(notification_id: int):
    notification = NotificationService.mark_as_read(
        int(get_jwt_identity()),
        notification_id,
    )
    if not notification:
        return error_response("Notification not found.", status_code=404)

    return success_response("Notification marked as read.", notification.to_dict())