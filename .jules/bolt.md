## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.
## 2024-11-23 - Memoize expensive operations outside Compose rendering loops
**Learning:** Performing `O(N)` operations like `list.filter { ... }` inside a Jetpack Compose `items()` block or a standard `forEach` block triggers those operations on every rendered item and during every recomposition, causing unnecessary CPU cycles.
**Action:** Identify expensive loop-bound operations, move them outside the render loop by grouping or transforming the data, and memoize the result using `remember { ... }`. Access the pre-calculated data (e.g., via `O(1)` Map lookup) inside the render block.
