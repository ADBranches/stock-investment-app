from flask import Blueprint, request

from app.services.education_service import EducationService
from app.utils.response import error_response, success_response

education_bp = Blueprint("education", __name__)


@education_bp.get("")
def list_education():
    query = request.args.get("q")
    topic = request.args.get("topic")

    articles = EducationService.list_articles(
        query=query,
        topic=topic,
    )

    return success_response(
        "Education articles retrieved successfully.",
        {"items": [article.to_dict() for article in articles]},
    )


@education_bp.get("/<int:article_id>")
def get_education_article(article_id: int):
    article = EducationService.get_article(article_id)
    if not article:
        return error_response("Education article not found.", status_code=404)

    return success_response(
        "Education article retrieved successfully.",
        article.to_dict(),
    )