## 2024-09-17 - Avoid Enum.values() in Jetpack Compose
**Learning:** In Kotlin codebases, using `Enum.values()` creates a new array instance on every invocation. When used within Jetpack Compose rendering loops (like `items()` or `forEach`), this causes significant GC churn and re-allocations during recomposition, which negatively affects performance.
**Action:** Always prefer `Enum.entries` over `Enum.values()` in Kotlin codebases, especially within Compose rendering loops, to utilize a cached, immutable list and prevent unnecessary array allocations.
