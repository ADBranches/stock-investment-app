import os
import tempfile

import pytest

from app import create_app
from app.extensions import db


def _build_app():
    """
    Supports either:
    - create_app()
    - create_app("testing")
    """
    try:
        app = create_app("testing")
    except TypeError:
        app = create_app()

    return app


@pytest.fixture()
def app():
    db_fd, db_path = tempfile.mkstemp()

    app = _build_app()
    app.config.update(
        TESTING=True,
        SQLALCHEMY_DATABASE_URI=f"sqlite:///{db_path}",
        JWT_SECRET_KEY="test-secret-key",
    )

    with app.app_context():
        db.create_all()
        yield app
        db.session.remove()
        db.drop_all()

    os.close(db_fd)
    os.unlink(db_path)


@pytest.fixture()
def client(app):
    return app.test_client()