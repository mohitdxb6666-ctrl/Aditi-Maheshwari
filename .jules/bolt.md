## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.
## 2024-11-23 - Memoize O(N) list operations outside Compose LazyColumn/LazyRow items blocks
**Learning:** Performing O(N) list operations (like `.filter { ... }`) inside the `items(...)` rendering block of Jetpack Compose `LazyColumn` or `LazyRow` leads to those expensive operations executing repeatedly upon every recomposition and item render.
**Action:** Pre-calculate and memoize the data grouping/filtering (e.g., `remember { list.groupBy { ... } }`) completely *outside* the Lazy container, then perform O(1) map lookups inside the `items` rendering loop.
