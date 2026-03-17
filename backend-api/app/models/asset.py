from datetime import datetime

from app.extensions import db


class Asset(db.Model):
    __tablename__ = "assets"

    id = db.Column(db.Integer, primary_key=True)
    symbol = db.Column(db.String(20), unique=True, nullable=False, index=True)
    name = db.Column(db.String(255), nullable=False)
    asset_type = db.Column(db.String(50), nullable=False, default="stock")
    exchange = db.Column(db.String(100), nullable=True)
    currency = db.Column(db.String(10), nullable=False, default="UGX")
    price = db.Column(db.Float, nullable=False, default=0.0)
    change_percent = db.Column(db.Float, nullable=False, default=0.0)
    description = db.Column(db.Text, nullable=True)
    is_active = db.Column(db.Boolean, nullable=False, default=True)
    created_at = db.Column(db.DateTime, nullable=False, default=datetime.utcnow)
    updated_at = db.Column(
        db.DateTime,
        nullable=False,
        default=datetime.utcnow,
        onupdate=datetime.utcnow,
    )

    watchlist_items = db.relationship("WatchlistItem", back_populates="asset", lazy=True)
    holdings = db.relationship("Holding", back_populates="asset", lazy=True)
    trades = db.relationship("Trade", back_populates="asset", lazy=True)

    def to_dict(self) -> dict:
        return {
            "id": self.id,
            "symbol": self.symbol,
            "name": self.name,
            "asset_type": self.asset_type,
            "exchange": self.exchange,
            "currency": self.currency,
            "price": self.price,
            "change_percent": self.change_percent,
            "description": self.description,
            "is_active": self.is_active,
        }