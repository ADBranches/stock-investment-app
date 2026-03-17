from flask import Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required

from app.schemas.kyc_schema import KYCSubmissionSchema
from app.services.kyc_service import KYCService
from app.services.notification_service import NotificationService
from app.utils.enums import NotificationType
from app.utils.response import error_response, success_response
from app.utils.validators import load_schema_or_errors

kyc_bp = Blueprint("kyc", __name__)
kyc_submission_schema = KYCSubmissionSchema()


@kyc_bp.post("/submit")
@jwt_required()
def submit_kyc():
    payload = request.get_json(silent=True) or {}
    data, errors = load_schema_or_errors(kyc_submission_schema, payload)
    if errors:
        return error_response("Validation failed.", errors, 400)

    submission = KYCService.submit_kyc(int(get_jwt_identity()), data)

    NotificationService.create_notification(
        user_id=int(get_jwt_identity()),
        title="KYC submitted",
        body="Your KYC details have been submitted and are pending review.",
        notification_type=NotificationType.KYC.value,
    )

    return success_response("KYC submitted successfully.", submission.to_dict(), 201)


@kyc_bp.get("/status")
@jwt_required()
def get_kyc_status():
    submission = KYCService.get_kyc_status(int(get_jwt_identity()))
    if not submission:
        return error_response("KYC record not found.", status_code=404)

    return success_response("KYC status retrieved successfully.", submission.to_dict())