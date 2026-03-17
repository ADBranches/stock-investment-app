from marshmallow import ValidationError


def load_schema_or_errors(schema, payload: dict):
    try:
        return schema.load(payload), None
    except ValidationError as exc:
        return None, exc.messages