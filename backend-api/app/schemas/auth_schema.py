from marshmallow import Schema, ValidationError, fields, validate, validates_schema


class RegisterSchema(Schema):
    email = fields.Email(required=True)
    password = fields.String(required=True, validate=validate.Length(min=8))
    full_name = fields.String(required=False, allow_none=True)


class LoginSchema(Schema):
    email = fields.Email(required=True)
    password = fields.String(required=True)


class AuthResponseSchema(Schema):
    access_token = fields.String(required=True)
    user = fields.Dict(required=True)

    @validates_schema
    def validate_payload(self, data, **kwargs):
        if not data.get("access_token"):
            raise ValidationError("access_token is required")