## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.
## 2024-11-23 - Memoize Expensive Collection Operations Outside Compose Lists
**Learning:** Performing `O(N)` collection filtering or mapping operations (e.g., `list.filter { ... }`) inside Compose list rendering blocks like `items()` forces expensive re-computation on every list item recomposition.
**Action:** Use `remember` combined with efficient data structures like `groupBy` or `associateWith` outside the `LazyColumn`/`LazyRow` to pre-calculate the required data maps once, turning the `O(N)` per-item operation into an `O(1)` map lookup.
