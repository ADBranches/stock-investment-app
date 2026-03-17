from marshmallow import Schema, fields, validate


class KYCSubmissionSchema(Schema):
    first_name = fields.String(required=True)
    last_name = fields.String(required=True)
    dob = fields.Date(required=True)
    document_type = fields.String(
        required=True,
        validate=validate.OneOf(["national_id", "passport", "driving_permit"]),
    )
    document_number = fields.String(required=True)


class KYCStatusSchema(Schema):
    status = fields.String(required=True)
    submitted_at = fields.DateTime(allow_none=True)
    reviewed_at = fields.DateTime(allow_none=True)
    review_notes = fields.String(allow_none=True)