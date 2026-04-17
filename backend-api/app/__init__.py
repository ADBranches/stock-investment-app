import os
from flask import Flask

from config import config_by_name
from app.extensions import cors, db, jwt, migrate
from app.utils.response import error_response


def create_app(config_name: str | None = None) -> Flask:
    app = Flask(__name__)

    env_name = config_name or os.getenv("FLASK_ENV", "development")
    app.config.from_object(
        config_by_name.get(env_name, config_by_name["development"])
    )

    register_extensions(app)
    register_blueprints(app)
    register_shell_context(app)

    @app.get("/api/v1/health")
    def health_check():
        return {
            "message": "Backend is running",
            "environment": env_name,
            "status": "ok",
        }, 200

    return app


def register_extensions(app: Flask) -> None:
    db.init_app(app)
    migrate.init_app(app, db)
    jwt.init_app(app)

    @jwt.unauthorized_loader
    def handle_missing_jwt(reason):
        return error_response("Authentication required.", status_code=401)

    @jwt.invalid_token_loader
    def handle_invalid_jwt(reason):
        return error_response("Invalid or malformed access token.", status_code=401)

    @jwt.expired_token_loader
    def handle_expired_jwt(jwt_header, jwt_payload):
        return error_response("Session expired. Please log in again.", status_code=401)

    @jwt.revoked_token_loader
    def handle_revoked_jwt(jwt_header, jwt_payload):
        return error_response("This session is no longer valid.", status_code=401)
    cors.init_app(
        app,
        resources={r"/api/*": {"origins": app.config["CORS_ORIGINS"]}},
    )


def register_blueprints(app: Flask) -> None:
    from app.routes.auth import auth_bp
    from app.routes.assets import assets_bp
    from app.routes.users import users_bp
    from app.routes.watchlist import watchlist_bp
    from app.routes.portfolio import portfolio_bp
    from app.routes.trades import trades_bp
    from app.routes.kyc import kyc_bp
    from app.routes.notifications import notifications_bp

    app.register_blueprint(auth_bp, url_prefix="/api/v1/auth")
    app.register_blueprint(users_bp, url_prefix="/api/v1/users")
    app.register_blueprint(assets_bp, url_prefix="/api/v1/assets")
    app.register_blueprint(watchlist_bp, url_prefix="/api/v1/watchlist")
    app.register_blueprint(portfolio_bp, url_prefix="/api/v1/portfolio")
    app.register_blueprint(trades_bp, url_prefix="/api/v1/trades")
    app.register_blueprint(kyc_bp, url_prefix="/api/v1/kyc")
    app.register_blueprint(notifications_bp, url_prefix="/api/v1/notifications")


def register_shell_context(app: Flask) -> None:
    from app import models

    @app.shell_context_processor
    def shell_context():
        return {
            "db": db,
            "models": models,
        }
