## ⚡ Bolt: [performance improvement] Pre-calculate chapter groupings in HomeScreen

### 💡 What
Extracted the O(N) list filtering operation (`allChapters.filter { it.subjectCode == subject.code }`) out of the `LazyColumn`'s `items()` rendering loop in `HomeScreen.kt`. The chapters are now grouped by subject code into a Map once, memoized with `remember(allChapters)`, and accessed via O(1) map lookups during rendering.

### 🎯 Why
In Jetpack Compose, executing O(N) operations inside `LazyColumn`'s rendering block is a performance anti-pattern. Because `items()` can be called frequently during scrolling and recomposition, repeatedly filtering the entire `allChapters` list causes unnecessary CPU usage and memory churn.

### 📊 Impact
- Reduces rendering time and memory allocations during recompositions on the Home screen.
- Changing from O(N * S) to O(N + S) complexity (where N is number of chapters and S is number of subjects) during each composition of the subject list.
- Noticeably smoother scrolling and rendering, especially as the number of total chapters grows.

### 🔬 Measurement
1. Profile memory allocations and CPU usage during fast scrolling on the Home screen in Android Studio Profiler.
2. Note the reduction in execution time in the UI thread while recomposing `HomeScreen`.
