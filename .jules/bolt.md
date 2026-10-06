## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.## 2024-11-23 - Jetpack Compose LazyList Rendering Optimizations
**Learning:** Filtering a large list inside a LazyColumn `items()` block causes an O(N) operation to run continuously as the user scrolls, creating UI jank.
**Action:** Use `remember(key)` and data structures like `groupBy` outside the rendering block to pre-compute maps/lookups (O(1)) instead of dynamically filtering (O(N)) during recomposition.
