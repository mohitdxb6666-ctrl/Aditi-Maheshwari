
## 2024-05-18 - Avoid Enum.values() in Jetpack Compose
**Learning:** `Enum.values()` creates a new array on every call, which leads to unnecessary memory allocations and garbage collection overhead. This is especially problematic in Jetpack Compose rendering loops (like `items()`) where it can cause stuttering and performance degradation.
**Action:** Always prefer `Enum.entries` in Kotlin (available since Kotlin 1.9), which returns a pre-allocated unmodifiable list, or cache the result of `Enum.values()` if `Enum.entries` is not available.
