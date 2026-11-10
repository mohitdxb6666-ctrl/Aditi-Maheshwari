## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.
## 2024-11-23 - Avoid O(N) list filtering inside Compose rendering loops
**Learning:** Performing O(N) operations like `list.filter { it.key == value }` inside a Jetpack Compose rendering loop (e.g. `items()`, `forEach`) causes redundant CPU overhead on every recomposition.
**Action:** Pre-calculate and memoize these computations using `remember` and efficient data structures (e.g. grouping by key into a Map via `list.groupBy { it.key }` outside the loop) to look up values in O(1) time and reduce overhead during recompositions.
