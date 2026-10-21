## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.
## 2026-10-21 - Avoid O(N) list filtering inside Compose `items()` block
**Learning:** Performing O(N) operations like `list.filter { ... }` inside a Jetpack Compose `LazyColumn` or `LazyRow` `items()` block causes the operation to be executed on every recomposition. This significantly degrades scrolling performance and increases CPU usage when dealing with lists of reasonable sizes.
**Action:** Pre-calculate and memoize the data grouping outside the rendering loop using `remember` and efficient data structures, such as mapping by key (`list.groupBy { ... }`). Use quick O(1) map lookups inside the `items()` block.
