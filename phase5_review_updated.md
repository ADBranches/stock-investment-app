# Phase 5 — Market Discovery and Dashboard (Reviewed Update)

## Timeline

**Weeks 7–8**

## Review outcome

Phase 5 is **not yet populated**.

The reviewed files show that the project has the **navigation shells and part of the data/backend foundation** for Phase 5, but the actual Phase 5 screens, view models, UI state files, reusable market/dashboard components, helper chart components, backend watchlist service, and backend Phase 5 tests are still missing.

So the correct guide is:

- **Phase 4 foundation is in place**
- **Phase 5 should now be populated**
- **Do not jump to Phase 6 yet**

---

## What already exists for Phase 5

### Android foundation already present

These reviewed files already give Phase 5 a starting point:

- `navigation/Routes.kt`
  - already defines `Dashboard`, `Market`, `Watchlist`, and `Portfolio`
- `navigation/BottomNavItem.kt`
  - already defines the main bottom navigation items
- `navigation/AppNavGraph.kt`
  - Phase 5 routes exist, but they still point to **Phase 4 placeholder screens**
- `data/remote/api/AssetApi.kt`
  - asset API interface exists
- `data/remote/api/WatchlistApi.kt`
  - watchlist API interface exists
- `data/repository/AssetRepository.kt`
  - asset repository exists and already maps asset DTOs to domain
- `data/remote/dto/AssetDtos.kt`
  - asset DTO exists
- `data/remote/dto/WatchlistDtos.kt`
  - watchlist DTO exists
- `domain/model/Asset.kt`
  - asset domain model exists
- `domain/model/WatchlistItem.kt`
  - watchlist item domain model exists

### Backend foundation already present

These reviewed backend files already exist:

- `backend-api/app/routes/assets.py`
  - list assets
  - get single asset
- `backend-api/app/routes/watchlist.py`
  - get watchlist
  - add asset to watchlist
  - remove asset from watchlist
- `backend-api/app/services/asset_service.py`
  - asset service exists
- `backend-api/app/repositories/asset_repository.py`
  - asset repository exists
- `backend-api/app/repositories/watchlist_repository.py`
  - watchlist repository exists

---

## Critical findings from the review

### 1) Dashboard, Market, and Watchlist are still placeholders

Your `AppNavGraph.kt` still sends these routes to `Phase4PlaceholderScreen`:

- `Routes.Dashboard`
- `Routes.Market`
- `Routes.Watchlist`

That means the app cannot yet claim Phase 5 is populated.

### 2) Most Android Phase 5 feature files do not exist yet

The terminal output shows `No such file or directory` for the Phase 5 feature files and helper components.

### 3) `WatchlistRepository.kt` on Android is only a stub

Right now it only has the constructor and no implementation methods. That means the watchlist feature cannot work from the Android side yet.

### 4) Android API contracts do not yet match the reviewed backend response shapes

Current Android APIs expect direct lists:

- `AssetApi.getAssets(): List<AssetDto>`
- `WatchlistApi.getWatchlist(): List<WatchlistDto>`

But the reviewed backend routes return wrapped payloads:

- assets route returns `{ "assets": [...] }`
- watchlist route returns `{ "items": [...] }`

So before screens are built, this contract must be aligned.

### 5) `watchlist_service.py` is missing

The route currently calls repositories directly. That can work temporarily, but it breaks the service-layer structure you defined in the phased architecture.

### 6) Phase 5 backend tests are missing

These do not exist yet:

- `backend-api/tests/test_market_assets.py`
- `backend-api/tests/test_watchlist.py`

---

## Updated Phase 5 objective

Build the first usable post-auth experience where the user can:

- land on a real dashboard after KYC
- browse assets from the backend
- search/filter assets
- open a single asset detail screen
- preview and manage watchlist items
- move through a real Phase 5 main navigation flow

---

## Updated Phase 5 deliverables

By the end of Phase 5, the following must be true:

- dashboard is no longer a placeholder
- market screen is no longer a placeholder
- watchlist screen is no longer a placeholder
- assets can be loaded from the backend
- asset detail can be opened from the market list
- watchlist can be loaded from the backend
- user can add and remove watchlist items
- asset search works
- backend assets and watchlist tests pass

---

## Updated Phase 5 file plan

### A. Files that already exist but must be updated

#### Android navigation

```text
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/navigation/
├── AppNavGraph.kt
├── Routes.kt
└── BottomNavItem.kt
```

**Update intention**

- replace placeholder destinations with real Phase 5 screens
- keep `Routes.kt` as is unless you add asset detail arguments
- keep `BottomNavItem.kt` unless you want a different icon for portfolio later

#### Android networking/data layer

```text
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/
├── AssetApi.kt
└── WatchlistApi.kt

mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/
├── AssetRepository.kt
└── WatchlistRepository.kt

mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/
├── AssetDtos.kt
└── WatchlistDtos.kt

mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/
├── Asset.kt
└── WatchlistItem.kt
```

**Update intention**

- align Android DTO/API contracts with backend response envelopes
- finish `WatchlistRepository.kt`
- add add/remove watchlist operations
- add detail-fetch support if needed
- add DTO-to-domain mappers if not already present for watchlist

#### Backend files to keep and refine

```text
backend-api/app/routes/
├── assets.py
└── watchlist.py

backend-api/app/services/
└── asset_service.py

backend-api/app/repositories/
├── asset_repository.py
└── watchlist_repository.py
```

**Update intention**

- keep these as the base for Phase 5
- optionally move watchlist route logic into a new service layer
- ensure response shapes are final and stable before Android screens are connected

---

### B. Android files that must now be created for Phase 5

```text
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/
├── home/
│   ├── dashboard/
│   │   ├── DashboardScreen.kt
│   │   ├── DashboardViewModel.kt
│   │   └── DashboardUiState.kt
│   └── components/
│       ├── PortfolioSummaryCard.kt
│       ├── MarketHighlightsCard.kt
│       ├── EducationPromptCard.kt
│       └── WatchlistPreviewCard.kt
├── market/
│   ├── list/
│   │   ├── MarketListScreen.kt
│   │   ├── MarketListViewModel.kt
│   │   └── MarketListUiState.kt
│   ├── details/
│   │   ├── AssetDetailScreen.kt
│   │   ├── AssetDetailViewModel.kt
│   │   └── AssetDetailUiState.kt
│   └── components/
│       ├── AssetCard.kt
│       ├── AssetPriceRow.kt
│       ├── MarketSearchBar.kt
│       └── FilterChipGroup.kt
└── watchlist/
    ├── WatchlistScreen.kt
    ├── WatchlistViewModel.kt
    └── WatchlistUiState.kt
```

### C. Android helper files that must now be created

```text
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/core/ui/components/
├── MiniLineChart.kt
└── PercentageBadge.kt
```

### D. Backend files that must now be created for Phase 5

```text
backend-api/app/services/
└── watchlist_service.py

backend-api/tests/
├── test_market_assets.py
└── test_watchlist.py
```

---

## Required Phase 5 contract correction

Before UI implementation goes far, correct the Android/backend response contract.

### Current issue

Android currently expects:

```text
AssetApi -> List<AssetDto>
WatchlistApi -> List<WatchlistDto>
```

Backend currently returns wrapped success payloads such as:

```text
assets -> { data: { assets: [...] } }   or equivalent wrapped success payload
watchlist -> { data: { items: [...] } } or equivalent wrapped success payload
```

### Recommended fix

Use response envelope DTOs on Android.

Example direction:

```text
AssetListResponseDto
└── data
    └── assets: List<AssetDto>

WatchlistResponseDto
└── data
    └── items: List<WatchlistDto>
```

Then update repositories to extract the nested lists cleanly.

---

## Exclusive build order for Phase 5

### Step 1 — Fix the data contract first

Work on:

- `AssetApi.kt`
- `WatchlistApi.kt`
- `AssetDtos.kt`
- `WatchlistDtos.kt`
- `AssetRepository.kt`
- `WatchlistRepository.kt`
- mapper files if needed

**Done when**

- Android can successfully deserialize asset list responses
- Android can successfully deserialize watchlist responses
- add/remove watchlist methods exist in repository

### Step 2 — Build Market list feature

Create:

- `MarketListScreen.kt`
- `MarketListViewModel.kt`
- `MarketListUiState.kt`
- `AssetCard.kt`
- `MarketSearchBar.kt`
- `FilterChipGroup.kt`
- `PercentageBadge.kt`

**Done when**

- asset list loads
- loading state works
- empty state works
- error state works
- search input filters visibly

### Step 3 — Build Asset detail feature

Create:

- `AssetDetailScreen.kt`
- `AssetDetailViewModel.kt`
- `AssetDetailUiState.kt`
- `AssetPriceRow.kt`
- `MiniLineChart.kt`

**Done when**

- tapping an asset opens details
- symbol, name, price, and change appear clearly
- add to watchlist action is visible

### Step 4 — Build Watchlist feature

Create:

- `WatchlistScreen.kt`
- `WatchlistViewModel.kt`
- `WatchlistUiState.kt`

Finish:

- `WatchlistRepository.kt`

**Done when**

- watchlist loads from backend
- remove action works
- add action from detail screen reflects here

### Step 5 — Build Dashboard feature

Create:

- `DashboardScreen.kt`
- `DashboardViewModel.kt`
- `DashboardUiState.kt`
- `PortfolioSummaryCard.kt`
- `MarketHighlightsCard.kt`
- `EducationPromptCard.kt`
- `WatchlistPreviewCard.kt`

**Done when**

- user lands on a real dashboard after KYC
- dashboard shows at least a welcome section
- dashboard shows market highlights from asset data
- dashboard previews watchlist items
- dashboard can navigate to Market and Watchlist

### Step 6 — Replace AppNavGraph placeholders

Update:

- `AppNavGraph.kt`

**Done when**

- `Routes.Dashboard` opens `DashboardScreen`
- `Routes.Market` opens `MarketListScreen`
- `Routes.Watchlist` opens `WatchlistScreen`
- `Routes.Portfolio` can remain placeholder because it belongs to Phase 6

### Step 7 — Complete backend service/test layer

Create:

- `backend-api/app/services/watchlist_service.py`
- `backend-api/tests/test_market_assets.py`
- `backend-api/tests/test_watchlist.py`

**Done when**

- asset list test passes
- asset detail test passes
- watchlist get/add/remove tests pass

---

## Suggested responsibilities inside Phase 5

### Android

- market list and search
- asset detail
- watchlist UI
- dashboard UI
- nav graph replacement
- DTO/repository contract alignment

### Backend

- stabilize assets responses
- stabilize watchlist responses
- add `watchlist_service.py`
- add Phase 5 backend tests

---

## Phase 5 completion gate

You may say **Phase 5 is complete** only when all of the following are true:

- all missing Android Phase 5 files listed above now exist
- `AppNavGraph.kt` no longer uses placeholders for dashboard, market, and watchlist
- `WatchlistRepository.kt` has working methods
- Android DTO contracts match backend responses
- `watchlist_service.py` exists
- `test_market_assets.py` exists and passes
- `test_watchlist.py` exists and passes
- user can browse assets and manage watchlist from the running app

---

## Recommended testing checklist for Phase 5

### Android checks

- login -> profile setup -> KYC success -> dashboard opens
- dashboard -> market navigation works
- dashboard -> watchlist navigation works
- market list loads assets
- market search filters assets
- market detail opens from selected asset
- add to watchlist works
- watchlist loads saved items
- remove from watchlist works
- empty/error/loading states render correctly

### Backend checks

- `GET /assets`
- `GET /assets/<id>`
- `GET /watchlist`
- `POST /watchlist`
- `DELETE /watchlist/<asset_id>`

---

## Final recommendation

Proceed to Phase 5 now, but do it in this order:

1. fix API/DTO response alignment
2. finish Android repositories
3. build Market list
4. build Asset detail
5. build Watchlist
6. build Dashboard
7. replace nav placeholders
8. add backend Phase 5 service and tests

That is the cleanest path that respects your existing timeline and avoids populating later-phase files before Phase 5 is truly complete.
