# Phase 5 Audit-First Input Pack

## Rule for this phase
We will **not** bulk-replace files blindly.

We will do Phase 5 in this order:

1. inspect files that already exist and may need **targeted edits**
2. identify files that are truly **missing** and should be fully created
3. only then return the revised Phase 5 markdown pack

This matches your preference to avoid repeating Phase 4 work and to avoid overengineering.

---

## Phase 5 scope from your timeline

Phase 5 covers:

- dashboard
- welcome summary
- market highlights
- asset list
- search and filter
- asset details
- watchlist add/remove

Deliverables:

- user can browse assets
- user can search assets
- user can view asset details
- user can add assets to watchlist
- dashboard is functional

---

## Files to inspect first

### Android files likely to need targeted edits
These may already exist from earlier phases:

```bash
app/src/main/java/com/mutebi/stockinvestmentapp/navigation/Routes.kt
app/src/main/java/com/mutebi/stockinvestmentapp/navigation/AppNavGraph.kt
app/src/main/java/com/mutebi/stockinvestmentapp/navigation/BottomNavItem.kt
app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/AssetApi.kt
app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/WatchlistApi.kt
app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/AssetRepository.kt
app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/WatchlistRepository.kt
app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/AssetDtos.kt
app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/WatchlistDtos.kt
app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/Asset.kt
app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/WatchlistItem.kt
```

### Android files likely to be new for Phase 5
These are the main feature files we will populate if missing:

```bash
app/src/main/java/com/mutebi/stockinvestmentapp/features/home/dashboard/DashboardScreen.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/home/dashboard/DashboardViewModel.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/home/dashboard/DashboardUiState.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/home/components/PortfolioSummaryCard.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/home/components/MarketHighlightsCard.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/home/components/EducationPromptCard.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/home/components/WatchlistPreviewCard.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/market/list/MarketListScreen.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/market/list/MarketListViewModel.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/market/list/MarketListUiState.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/market/details/AssetDetailScreen.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/market/details/AssetDetailViewModel.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/market/details/AssetDetailUiState.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/market/components/AssetCard.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/market/components/AssetPriceRow.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/market/components/MarketSearchBar.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/market/components/FilterChipGroup.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/watchlist/WatchlistScreen.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/watchlist/WatchlistViewModel.kt
app/src/main/java/com/mutebi/stockinvestmentapp/features/watchlist/WatchlistUiState.kt
app/src/main/java/com/mutebi/stockinvestmentapp/core/ui/components/MiniLineChart.kt
app/src/main/java/com/mutebi/stockinvestmentapp/core/ui/components/PercentageBadge.kt
```

### Backend files likely to need targeted edits
These may already exist from Phase 3:

```bash
../backend-api/app/routes/assets.py
../backend-api/app/routes/watchlist.py
../backend-api/app/services/asset_service.py
../backend-api/app/services/watchlist_service.py
../backend-api/app/repositories/asset_repository.py
../backend-api/app/repositories/watchlist_repository.py
../backend-api/tests/test_market_assets.py
../backend-api/tests/test_watchlist.py
```

---

## Exact audit commands to run

### 1) Android existing/update-candidate files
Run this from inside `mobile-android/`:

```bash
for f in app/src/main/java/com/mutebi/stockinvestmentapp/navigation/Routes.kt app/src/main/java/com/mutebi/stockinvestmentapp/navigation/AppNavGraph.kt app/src/main/java/com/mutebi/stockinvestmentapp/navigation/BottomNavItem.kt app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/AssetApi.kt app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/WatchlistApi.kt app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/AssetRepository.kt app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/WatchlistRepository.kt app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/AssetDtos.kt app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/WatchlistDtos.kt app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/Asset.kt app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/WatchlistItem.kt
do
  echo
  echo "==================== $f ===================="
  cat "$f"
done
```

### 2) Android Phase 5 feature/new-surface files
Run this from inside `mobile-android/`:

```bash
for f in app/src/main/java/com/mutebi/stockinvestmentapp/features/home/dashboard/DashboardScreen.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/home/dashboard/DashboardViewModel.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/home/dashboard/DashboardUiState.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/home/components/PortfolioSummaryCard.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/home/components/MarketHighlightsCard.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/home/components/EducationPromptCard.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/home/components/WatchlistPreviewCard.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/market/list/MarketListScreen.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/market/list/MarketListViewModel.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/market/list/MarketListUiState.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/market/details/AssetDetailScreen.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/market/details/AssetDetailViewModel.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/market/details/AssetDetailUiState.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/market/components/AssetCard.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/market/components/AssetPriceRow.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/market/components/MarketSearchBar.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/market/components/FilterChipGroup.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/watchlist/WatchlistScreen.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/watchlist/WatchlistViewModel.kt app/src/main/java/com/mutebi/stockinvestmentapp/features/watchlist/WatchlistUiState.kt app/src/main/java/com/mutebi/stockinvestmentapp/core/ui/components/MiniLineChart.kt app/src/main/java/com/mutebi/stockinvestmentapp/core/ui/components/PercentageBadge.kt
do
  echo
  echo "==================== $f ===================="
  cat "$f"
done
```

### 3) Backend Phase 5 files
Run this from inside `mobile-android/`:

```bash
for f in ../backend-api/app/routes/assets.py ../backend-api/app/routes/watchlist.py ../backend-api/app/services/asset_service.py ../backend-api/app/services/watchlist_service.py ../backend-api/app/repositories/asset_repository.py ../backend-api/app/repositories/watchlist_repository.py ../backend-api/tests/test_market_assets.py ../backend-api/tests/test_watchlist.py
do
  echo
  echo "==================== $f ===================="
  cat "$f"
done
```

---

## What I will do after you paste those results

I will return a **revised Phase 5 markdown pack** split into:

- **keep as-is**
- **targeted edits only**
- **new files to create fully**

That way we do not repeat Phase 4 work and we only touch what truly belongs to Phase 5.
