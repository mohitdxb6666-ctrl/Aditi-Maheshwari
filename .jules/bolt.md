## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.
## 2026-11-02 - Avoid O(N) operations inside Compose items() loops
**Learning:** Operations like `.filter` inside Compose `items(SubjectType.entries)` blocks execute O(N) calculations on every single recomposition, leading to poor UI performance and jank during scrolling or updates.
**Action:** Memoize large computations at the top of the Composable using `remember`, and pre-calculate O(1) lookup maps (e.g., using `groupBy`) to replace O(N) list filtering inside rendering loops.
