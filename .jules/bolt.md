## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.

## 2024-05-24 - Grouping outside Compose loops
**Learning:** Performing an O(N) list filtration or aggregation inside `items()` loops in Jetpack Compose leads to excessive performance overhead during re-composition, essentially creating O(N * M) behavior on the main thread for rendering the list.
**Action:** Always prefer pre-calculating groupings utilizing `remember` (e.g. `val groupedItems = remember(items) { items.groupBy { it.key } }`) and then use O(1) Map lookups within the `items()` rendering blocks.
