## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.
## 2024-11-23 - Avoid O(N) operations in Compose LazyColumn items()
**Learning:** Performing filtering operations like `allChapters.filter { ... }` inside a `LazyColumn` `items()` block causes O(N) complexity during *every* list item rendering and recomposition. This leads to severe UI jank, especially for larger lists.
**Action:** Always pre-calculate and memoize these computations outside the rendering loop. Use `remember` combined with efficient data structures, such as `groupBy` to create a Map, fetching from it inside the loop in O(1) time.
