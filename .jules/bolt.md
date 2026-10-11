## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.
## 2024-11-23 - Avoid .filter inside Compose items() rendering loops
**Learning:** Performing O(N) operations like `.filter` inside Compose lazy list `items()` rendering loops causes recalculation for every item during every recomposition, resulting in O(N*M) time complexity. This is particularly problematic for large datasets and frequent recompositions.
**Action:** Pre-calculate and group data (e.g., using `groupBy`) outside the rendering loop inside a `remember` block. Then, use O(1) map lookups inside the `items()` block.
