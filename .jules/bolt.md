## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.
## 2024-11-23 - Avoid O(N) list filtering inside Compose rendering loops
**Learning:** Performing operations like `allChapters.filter { it.subjectCode == subject.code }` directly inside a `LazyColumn` or `LazyRow` `items()` rendering loop, or during regular recomposition, causes O(N) operations to run repeatedly. This creates unnecessary overhead and memory allocations.
**Action:** Always pre-calculate and memoize these groupings or filters before the `LazyColumn` using `remember` and efficient data structures (e.g. `val chaptersBySubject = remember(allChapters) { allChapters.groupBy { it.subjectCode } }`), then perform O(1) lookups during render.
