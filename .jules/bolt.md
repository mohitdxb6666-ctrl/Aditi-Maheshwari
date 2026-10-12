## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.
## 2024-03-24 - Avoid O(N) operations in Compose rendering loops
**Learning:** Performing O(N) list operations like `filter` directly inside Jetpack Compose rendering loops (such as `items()` in `LazyColumn`) causes significant performance overhead during recomposition, especially for large lists. The recomposition happens frequently and redundant calculations block the UI thread.
**Action:** Always pre-calculate and memoize expensive computations outside the rendering loop. Use `remember` combined with efficient data structures like `groupBy` to transform O(N) lookups into O(1) map access during the render phase.
