from app.extensions import db
from app.models.kyc import KYCSubmission
from app.utils.enums import KYCStatus


class KYCService:
    @staticmethod
    def submit_kyc(user_id: int, data: dict):
        submission = KYCSubmission.query.filter_by(user_id=user_id).first()
        if submission:
            for key, value in data.items():
                setattr(submission, key, value)
            submission.status = KYCStatus.PENDING.value
        else:
            submission = KYCSubmission(
                user_id=user_id,
                status=KYCStatus.PENDING.value,
                **data,
            )
            db.session.add(submission)

        db.session.commit()
        return submission

    @staticmethod
    def get_kyc_status(user_id: int):
        return KYCSubmission.query.filter_by(user_id=user_id).first()