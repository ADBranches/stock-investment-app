_USER_SETTINGS_STORE: dict[int, dict] = {}


class SettingsService:
    DEFAULTS = {
        "privacy_accepted": False,
        "analytics_consent": False,
        "marketing_consent": False,
    }

    @staticmethod
    def get_settings(user_id: int) -> dict:
        stored = _USER_SETTINGS_STORE.get(user_id, {})
        return {**SettingsService.DEFAULTS, **stored}

    @staticmethod
    def update_settings(user_id: int, payload: dict) -> dict:
        current = SettingsService.get_settings(user_id)
        allowed_keys = set(SettingsService.DEFAULTS.keys())

        updates = {
            key: bool(value)
            for key, value in payload.items()
            if key in allowed_keys
        }

        current.update(updates)
        _USER_SETTINGS_STORE[user_id] = current
        return current