## 2024-05-24 - [Avoid Enum.values() in Compose Render Loops]
**Learning:** [Using `Enum.values()` inside Jetpack Compose `items()` or other render loops causes a new array allocation on every recomposition. This leads to unnecessary garbage collection overhead and potential UI stutter.]
**Action:** [Always use `Enum.entries` instead of `Enum.values()` in Kotlin. It returns a pre-allocated unmodifiable list, eliminating these micro-allocations.]
