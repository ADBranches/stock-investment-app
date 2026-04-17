from sqlalchemy import or_

from app.models.education import EducationArticle


class EducationService:
    @staticmethod
    def list_articles(query: str | None = None, topic: str | None = None):
        db_query = EducationArticle.query

        if topic:
            db_query = db_query.filter(EducationArticle.topic.ilike(topic))

        if query:
            db_query = db_query.filter(
                or_(
                    EducationArticle.title.ilike(f"%{query}%"),
                    EducationArticle.summary.ilike(f"%{query}%"),
                    EducationArticle.content.ilike(f"%{query}%"),
                )
            )

        return db_query.order_by(EducationArticle.created_at.desc()).all()

    @staticmethod
    def get_article(article_id: int):
        return EducationArticle.query.get(article_id)