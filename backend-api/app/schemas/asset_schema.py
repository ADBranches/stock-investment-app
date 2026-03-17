from marshmallow import Schema, fields


class AssetSchema(Schema):
    id = fields.Int(dump_only=True)
    symbol = fields.String(required=True)
    name = fields.String(required=True)
    asset_type = fields.String(required=True)
    exchange = fields.String(allow_none=True)
    currency = fields.String(required=True)
    price = fields.Float(required=True)
    change_percent = fields.Float(required=True)
    description = fields.String(allow_none=True)
    is_active = fields.Bool(required=True)


class AssetQuerySchema(Schema):
    q = fields.String(required=False, allow_none=True, load_default=None)
    active = fields.Bool(required=False, allow_none=True, load_default=None)