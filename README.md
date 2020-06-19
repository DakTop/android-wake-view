# android-wake-view
展示线性以及网格布局的自定义view，不支持滚动，适用于展示少量集合数据的view，以及重叠式卡片列表。

本库中包含使用RecycleView实现的各种各样的功能

其实这个自定义控件的功能完全可以由RecycleView代替，那么为什么还要创建这个东西呢？首先我们要明白一个思想：就是RecycleView或者ListView是在有限的窗口内用于循环展示大量的数据集控件。 而对于在屏幕上足够的空间内展示少量的数据集（用不到滚动出屏幕的那些展示内容）我们应当使用LinearLayout或者GridLayout来完成。 其实此控件就是基于LinearLayout和GridLayout进行了进一步的封装，使我们更方便的增加，删除，修改它们内部的View。 使用此控件代替了RecycleView或者ListView后可以有效的避免由于两个滑动布局嵌套而产生的滑动事件冲突。


引入：
1、'https://jitpack.io'
2、com.github.DakTop:android-wake-view:v1.0.3
