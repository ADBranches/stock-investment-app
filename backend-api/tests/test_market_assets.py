import uuid

from app.repositories.asset_repository import AssetRepository


def _seed_asset(symbol: str, name: str, price: float, change_percent: float, is_active: bool = True):
    return AssetRepository.create_asset(
        symbol=symbol,
        name=name,
        price=price,
        change_percent=change_percent,
        is_active=is_active,
    )


def test_list_market_assets(client):
    suffix = uuid.uuid4().hex[:6].upper()
    _seed_asset(f"MK{suffix}", f"Market Asset {suffix}", 125.50, 2.4, True)

    response = client.get("/api/v1/assets")
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assets = data.get("assets") or []
    assert len(assets) >= 1


def test_search_market_assets(client):
    suffix = uuid.uuid4().hex[:6].upper()
    symbol = f"SR{suffix}"
    _seed_asset(symbol, f"Search Asset {suffix}", 98.10, -1.1, True)

    response = client.get(f"/api/v1/assets?q={symbol}")
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assets = data.get("assets") or []
    assert any(asset.get("symbol") == symbol for asset in assets), assets


def test_get_single_market_asset(client):
    suffix = uuid.uuid4().hex[:6].upper()
    asset = _seed_asset(f"DT{suffix}", f"Detail Asset {suffix}", 77.00, 0.8, True)

    response = client.get(f"/api/v1/assets/{asset.id}")
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("id") == asset.id
    assert data.get("symbol") == asset.symbol