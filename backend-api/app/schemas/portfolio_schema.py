from marshmallow import Schema, fields


class PortfolioSchema(Schema):
    id = fields.Int()
    user_id = fields.Int()
    name = fields.String()
    cash_balance = fields.Float()
    created_at = fields.DateTime()


class PortfolioSummarySchema(Schema):
    portfolio = fields.Dict(required=True)
    total_value = fields.Float(required=True)
    holdings_count = fields.Int(required=True)
    holdings = fields.List(fields.Dict(), required=True)
    trades = fields.List(fields.Dict(), required=True)