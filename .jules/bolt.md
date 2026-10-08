## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.
## 2024-11-23 - Memoize computations and use Map lookups in Compose loops
**Learning:** Performing O(N) operations like `.filter` or expensive calculations without memoization directly within Jetpack Compose rendering loops (like `LazyColumn` `items`) causes major performance degradation and unnecessary CPU work during recompositions or scrolling.
**Action:** Always wrap top-level filtered lists and aggregations in `remember(key)` blocks. For lists that need to be iterated grouped by a specific property inside `LazyColumn`, pre-calculate and memoize a map using `groupBy` outside the list context to allow O(1) lookups during rendering.
