## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.

## 2024-11-23 - Pre-calculate collections for LazyColumn/LazyRow items
**Learning:** Performing $O(N)$ operations like `.filter { it.subjectCode == subject.code }` inside a Jetpack Compose `LazyColumn` or `LazyRow` `items()` rendering block causes expensive operations on every recomposition.
**Action:** Pre-calculate and memoize these computations using `remember` and efficient data structures (e.g., `groupBy { it.subjectCode }` into a Map) outside the rendering loop. Access the map inside the `items()` block.
