from datetime import datetime

from app.extensions import db


class User(db.Model):
    __tablename__ = "users"

    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(255), unique=True, nullable=False, index=True)
    password_hash = db.Column(db.String(255), nullable=False)
    full_name = db.Column(db.String(255), nullable=True)
    phone_number = db.Column(db.String(50), nullable=True)
    country = db.Column(db.String(100), nullable=True)
    role = db.Column(db.String(50), nullable=False, default="user")
    profile_completed = db.Column(db.Boolean, nullable=False, default=False)
    is_active = db.Column(db.Boolean, nullable=False, default=True)
    created_at = db.Column(db.DateTime, nullable=False, default=datetime.utcnow)
    updated_at = db.Column(
        db.DateTime,
        nullable=False,
        default=datetime.utcnow,
        onupdate=datetime.utcnow,
    )

    portfolios = db.relationship("Portfolio", back_populates="user", lazy=True)
    watchlist_items = db.relationship("WatchlistItem", back_populates="user", lazy=True)
    trades = db.relationship("Trade", back_populates="user", lazy=True)
    notifications = db.relationship("Notification", back_populates="user", lazy=True)
    kyc_submission = db.relationship(
        "KYCSubmission",
        back_populates="user",
        uselist=False,
        lazy=True,
    )

    def to_dict(self) -> dict:
        return {
            "id": self.id,
            "email": self.email,
            "full_name": self.full_name,
            "phone_number": self.phone_number,
            "country": self.country,
            "role": self.role,
            "profile_completed": self.profile_completed,
            "is_active": self.is_active,
            "created_at": self.created_at.isoformat() if self.created_at else None,
        }