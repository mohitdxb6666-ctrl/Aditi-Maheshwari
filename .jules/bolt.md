## 2024-09-15 - Kotlin Enum Array Allocation Optimization in Jetpack Compose
**Learning:** In Jetpack Compose, using `Enum.values()` inside rendering loops like `items()` or `forEach` creates unnecessary array allocations every time the component recomposes. This can cause frequent garbage collection and negatively impact rendering performance.
**Action:** Always prefer `Enum.entries` over `Enum.values()` in Kotlin codebases, especially within Jetpack Compose rendering functions to prevent unnecessary array allocations and avoid triggering unnecessary garbage collection.
