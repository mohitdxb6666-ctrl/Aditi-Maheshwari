## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.
## 2024-11-23 - Avoid O(N) operations inside LazyColumn/LazyRow items()
**Learning:** Performing O(N) operations (like `list.filter { ... }`) inside the `items()` block of a `LazyColumn` or `LazyRow` causes unnecessary computation during every recomposition, slowing down list scrolling and rendering performance.
**Action:** Pre-calculate and memoize these computations outside the rendering loop using `remember` (e.g., `val groupedData = remember(data) { data.groupBy { it.key } }`) and perform fast O(1) lookups inside the `items()` block.
