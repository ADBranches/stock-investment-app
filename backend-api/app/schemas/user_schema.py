from marshmallow import Schema, fields


class UserSchema(Schema):
    id = fields.Int(dump_only=True)
    email = fields.Email(required=True)
    full_name = fields.String(allow_none=True)
    phone_number = fields.String(allow_none=True)
    country = fields.String(allow_none=True)
    role = fields.String(dump_only=True)
    profile_completed = fields.Bool(dump_only=True)
    is_active = fields.Bool(dump_only=True)
    created_at = fields.DateTime(dump_only=True)


class UpdateUserSchema(Schema):
    full_name = fields.String(required=False)
    phone_number = fields.String(required=False)
    country = fields.String(required=False)