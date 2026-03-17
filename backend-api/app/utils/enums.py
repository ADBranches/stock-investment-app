from enum import Enum


class UserRole(str, Enum):
    USER = "user"
    ADMIN = "admin"


class AssetType(str, Enum):
    STOCK = "stock"
    ETF = "etf"
    BOND = "bond"


class TradeType(str, Enum):
    BUY = "buy"
    SELL = "sell"


class TradeStatus(str, Enum):
    PENDING = "pending"
    COMPLETED = "completed"
    FAILED = "failed"


class KYCStatus(str, Enum):
    PENDING = "pending"
    APPROVED = "approved"
    REJECTED = "rejected"


class NotificationType(str, Enum):
    SYSTEM = "system"
    TRADE = "trade"
    KYC = "kyc"