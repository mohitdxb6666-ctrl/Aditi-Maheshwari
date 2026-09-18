## 2026-09-18 - Jetpack Compose Enums values() vs entries
**Learning:** In Kotlin codebases, using `Enum.values()` inside Jetpack Compose rendering loops (like `items()`) causes unnecessary array allocations on every recomposition. Since `Enum.entries` was introduced in Kotlin 1.9, it provides an unmodifiable list without the allocation penalty.
**Action:** Replace `Enum.values()` with `Enum.entries` globally across the codebase, particularly where it is used within Jetpack Compose elements.
