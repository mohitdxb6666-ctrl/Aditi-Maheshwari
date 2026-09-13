## 2024-05-24 - [Enum.values() performance]
**Learning:** `Enum.values()` creates a new array every time it is called, which can lead to performance overhead, especially inside a `@Composable` block or a list. It also leads to extra memory allocations.
**Action:** Replace `Enum.values()` with `Enum.entries` which returns a pre-allocated immutable list.
