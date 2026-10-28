## 2024-05-15 - Compose LazyColumn items() filtering
**Learning:** In Jetpack Compose, executing `filter` operations on large collections (like `allChapters.filter`) inside a `LazyColumn` `items()` rendering block will run on every recomposition as items enter the view, causing unnecessary O(N) recalculations and CPU overhead.
**Action:** Always extract O(N) calculations (like list filtering) outside the rendering loop. Pre-calculate values using `remember` (e.g. `groupBy` to create a Map) and perform O(1) lookups inside the `items()` block instead.
