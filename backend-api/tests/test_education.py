from app.extensions import db
from app.models.education import EducationArticle


def test_list_education_articles(client):
    article = EducationArticle(
        title="Responsible Investing Basics",
        summary="A starter lesson for new investors.",
        content="Long-form lesson content goes here.",
        topic="Beginner",
        risk_warning="Markets can rise and fall. Learn before acting.",
        is_beginner_friendly=True,
    )
    db.session.add(article)
    db.session.commit()

    response = client.get("/api/v1/education")
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    items = data.get("items") or []
    assert any(item.get("title") == "Responsible Investing Basics" for item in items)


def test_get_single_education_article(client):
    article = EducationArticle(
        title="Understanding Risk",
        summary="A short article about investment risk.",
        content="Diversification, time horizon, and volatility matter.",
        topic="Risk",
        risk_warning="Past performance does not guarantee future results.",
        is_beginner_friendly=True,
    )
    db.session.add(article)
    db.session.commit()

    response = client.get(f"/api/v1/education/{article.id}")
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("id") == article.id
    assert data.get("title") == "Understanding Risk"