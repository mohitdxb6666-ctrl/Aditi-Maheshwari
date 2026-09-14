## 2024-09-14 - Kotlin Enum .values() vs .entries
**Learning:** In Kotlin codebases, using `Enum.values()` creates a new array every time it is called, which can cause unnecessary memory allocations and garbage collection overhead, especially in Compose `items()` calls where it runs frequently. `Enum.entries` provides an immutable list and does not allocate a new object on each access.
**Action:** Replace `Enum.values().toList()` and `Enum.values()` with `Enum.entries` where possible to avoid redundant array allocations.
