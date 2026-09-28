## 2024-05-14 - Enum allocations in Jetpack Compose
**Learning:** Using `Enum.values()` inside Jetpack Compose rendering loops (like `items()` or `forEach`) causes unnecessary GC pressure because `values()` allocates a new array on every call.
**Action:** Always prefer `Enum.entries` (introduced in Kotlin 1.8.20 and stable in 1.9.0) as it returns a pre-allocated `List` that prevents these unnecessary allocations.
