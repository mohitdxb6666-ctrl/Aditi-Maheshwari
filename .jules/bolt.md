## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.
## 2024-11-23 - Pre-calculate collections for O(1) rendering access
**Learning:** Avoid expensive O(N) list operations like `filter` inside Jetpack Compose `LazyColumn`/`LazyRow` rendering blocks. In particular, computing something like `allChapters.filter { it.subjectCode == subject.code }` inside `items(SubjectType.entries)` results in O(S * C) time complexity on every recomposition.
**Action:** Pre-calculate grouped data using `remember` and efficient O(1) lookup structures like Maps outside the rendering loop (e.g., `val chaptersBySubject = remember(allChapters) { allChapters.groupBy { it.subjectCode } }`), reducing the loop complexity to O(1).
