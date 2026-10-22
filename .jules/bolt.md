## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.
## 2026-10-22 - Expensive O(N) operations inside LazyColumn/LazyRow
**Learning:** Performing O(N) list operations like `.filter` or `.map` directly inside a Compose `items()` lambda forces these computations to be evaluated repeatedly on every scroll event or recomposition loop.
**Action:** Hoist the expensive operations outside the rendering loop and compute them exactly once using `remember`, grouping elements into a Map or List. Then inside `items()`, do a quick O(1) lookup.
