## 2024-11-23 - Avoid Enum.values() in Compose rendering loops
**Learning:** `Enum.values()` in Kotlin allocates a new array each time it's called. When used inside a Jetpack Compose rendering loop (like `items(SubjectType.values().toList())` or `NavigationTab.values().forEach`), it causes unnecessary memory allocation on every recomposition.
**Action:** Use `Enum.entries` instead, which returns a pre-allocated unmodifiable list, avoiding memory churn during Compose recompositions.## 2024-11-23 - Avoid Destructive Test Fixes
**Learning:** Do not 'fix' broken tests by altering their assertions or deleting test files entirely unless specifically instructed to do so, especially when they represent core functionality regressions.
**Action:** If tests fail, investigate the root cause or leave them as-is if unrelated, but do not delete test files to make CI pass.
