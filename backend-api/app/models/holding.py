from datetime import datetime

from app.extensions import db


class Holding(db.Model):
    __tablename__ = "holdings"
    __table_args__ = (
        db.UniqueConstraint("portfolio_id", "asset_id", name="uq_portfolio_asset_holding"),
    )

    id = db.Column(db.Integer, primary_key=True)
    portfolio_id = db.Column(db.Integer, db.ForeignKey("portfolios.id"), nullable=False)
    asset_id = db.Column(db.Integer, db.ForeignKey("assets.id"), nullable=False)
    quantity = db.Column(db.Float, nullable=False, default=0.0)
    average_price = db.Column(db.Float, nullable=False, default=0.0)
    created_at = db.Column(db.DateTime, nullable=False, default=datetime.utcnow)
    updated_at = db.Column(
        db.DateTime,
        nullable=False,
        default=datetime.utcnow,
        onupdate=datetime.utcnow,
    )

    portfolio = db.relationship("Portfolio", back_populates="holdings")
    asset = db.relationship("Asset", back_populates="holdings")

    def market_value(self) -> float:
        return float(self.quantity * (self.asset.price if self.asset else 0.0))

    def to_dict(self) -> dict:
        return {
            "id": self.id,
            "portfolio_id": self.portfolio_id,
            "quantity": self.quantity,
            "average_price": self.average_price,
            "market_value": self.market_value(),
            "asset": self.asset.to_dict() if self.asset else None,
        }