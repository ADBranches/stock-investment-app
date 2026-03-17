from marshmallow import Schema, fields, validate


class TradeCreateSchema(Schema):
    asset_id = fields.Int(required=True)
    trade_type = fields.String(required=True, validate=validate.OneOf(["buy", "sell"]))
    quantity = fields.Float(required=True, validate=validate.Range(min=0.0001))


class TradeSchema(Schema):
    id = fields.Int(dump_only=True)
    user_id = fields.Int(dump_only=True)
    portfolio_id = fields.Int(dump_only=True)
    asset_id = fields.Int(required=True)
    trade_type = fields.String(required=True)
    quantity = fields.Float(required=True)
    price = fields.Float(dump_only=True)
    status = fields.String(dump_only=True)
    created_at = fields.DateTime(dump_only=True)
    asset = fields.Dict(dump_only=True)