from app.extensions import db
from app.models.notification import Notification
from app.utils.enums import NotificationType


class NotificationService:
    @staticmethod
    def create_notification(
        user_id: int,
        title: str,
        body: str,
        notification_type: str = NotificationType.SYSTEM.value,
    ):
        notification = Notification(
            user_id=user_id,
            title=title,
            body=body,
            notification_type=notification_type,
        )
        db.session.add(notification)
        db.session.commit()
        return notification

    @staticmethod
    def list_notifications(user_id: int):
        return (
            Notification.query.filter_by(user_id=user_id)
            .order_by(Notification.created_at.desc())
            .all()
        )

    @staticmethod
    def mark_as_read(user_id: int, notification_id: int):
        notification = Notification.query.filter_by(
            id=notification_id,
            user_id=user_id,
        ).first()
        if not notification:
            return None

        notification.is_read = True
        db.session.commit()
        return notification