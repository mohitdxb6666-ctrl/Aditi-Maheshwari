## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.
## 2026-10-26 - Prevent O(N) operations in Compose LazyColumn items()
**Learning:** Performing list filtering (e.g., `list.filter { ... }`) inside Jetpack Compose `items()` rendering loops causes an O(N) operation to run for every item on every recomposition. This blocks the main thread and can lead to dropped frames, especially during fast scrolls or when dealing with large lists.
**Action:** Pre-calculate and memoize these aggregations outside the rendering loop. Use `remember(key) { list.groupBy { ... } }` to turn O(M*N) complexity into an O(1) map lookup per item inside the loop.
