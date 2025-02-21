# OpenGL ES 基础概念

## 渲染管线流程

OpenGL ES 渲染管线（Graphics Pipeline）是图形渲染的核心流程，主要包含以下几个阶段：

1. 顶点着色器（Vertex Shader）阶段
    - 处理每个顶点数据
    - 进行坐标变换（模型、视图、投影变换）
    - 计算顶点颜色、纹理坐标等
    - 可以自定义顶点属性计算

2. 图元装配（Primitive Assembly）
    - 将顶点组装成图元（点、线、三角形）
    - 进行裁剪（Clipping）
    - 执行面剔除（Face Culling）
    - 视口变换（Viewport Transform）

3. 光栅化（Rasterization）
    - 将图元转换为片段（Fragment）
    - 确定像素覆盖范围
    - 生成片段的属性（颜色、深度、纹理坐标等）
    - 进行插值计算

4. 片段着色器（Fragment Shader）阶段
    - 处理每个片段
    - 计算最终颜色
    - 应用纹理
    - 实现光照效果
    - 可以自定义片段处理逻辑

5. 片段测试与混合（Fragment Tests & Blending）
    - 深度测试（Depth Test）
    - 模板测试（Stencil Test）
    - Alpha 测试
    - 混合（Blending）
    - 写入帧缓冲（Frame Buffer）

渲染管线的特点：

1. 固定流程，顺序不可改变
2. 只能自定义顶点着色器和片段着色器
3. 其他阶段可以通过 OpenGL ES 的 API 配置
4. 并行处理，提高渲染效率

## 坐标系统

OpenGL ES 坐标系统分为五个主要部分：

1. 局部坐标系（Local Space）
    - 原点(0,0,0)为物体的中心点
    - X轴：向右为正
    - Y轴：向上为正
    - Z轴：垂直屏幕向外为正
    - 范围通常在[-1,1]之间

2. 世界坐标系（World Space）
    - 整个3D空间的统一坐标系
    - 通过模型矩阵（Model Matrix）转换
    - 可以进行平移、旋转、缩放操作
    - 决定物体在3D世界中的位置和姿态

3. 观察坐标系（View Space）
    - 以摄像机为原点的坐标系
    - 通过观察矩阵（View Matrix）转换
    - 定义观察位置和方向
    - 决定场景如何被观察者看到

4. 裁剪坐标系（Clip Space）
    - 通过投影矩阵（Projection Matrix）转换
    - 坐标范围：[-1,1]³ 的立方体空间
    - 支持正交投影和透视投影
    - 决定可视范围和显示效果

5. 屏幕坐标系（Screen Space）
    - 二维像素坐标系
    - 原点在左上角(0,0)
    - X轴向右增加
    - Y轴向下增加
    - 范围取决于屏幕分辨率

坐标转换顺序：
局部坐标 → 世界坐标 → 观察坐标 → 裁剪坐标 → 屏幕坐标

每个转换都由对应的变换矩阵完成：

- Model Matrix：局部到世界
- View Matrix：世界到观察
- Projection Matrix：观察到裁剪
- Viewport Transform：裁剪到屏幕

## 图形基本元素（点、线、三角形）

OpenGL ES 的基本图形元素包括点、线、三角形，它们是构建复杂图形的基础：

1. 点（Points）
    - 最简单的图元
    - 通过 GL_POINTS 绘制
    - 可以设置点的大小（glPointSize）
    - 常用于粒子效果

2. 线（Lines）
    - 基本线段：GL_LINES
        * 每两个顶点构成一条线段
        * 顶点：(x1,y1) -> (x2,y2)

    - 连续线段：GL_LINE_STRIP
        * 顶点依次相连
        * 顶点：(x1,y1) -> (x2,y2) -> (x3,y3)

    - 闭合线段：GL_LINE_LOOP
        * 首尾相连形成封闭图形
        * 最后一个点自动连接第一个点

3. 三角形（Triangles）
    - 基本三角形：GL_TRIANGLES
        * 每三个顶点构成一个三角形
        * 是最基本的多边形单元

    - 三角形带：GL_TRIANGLE_STRIP
        * 共享顶点的连续三角形
        * 节省顶点数据

    - 三角形扇：GL_TRIANGLE_FAN
        * 以第一个顶点为中心
        * 形成扇形排列的三角形

绘制顺序：

- 顶点按逆时针顺序定义
- 这样定义的面为正面
- 可以通过 glFrontFace 改变定义

顶点数据格式：

```
点：    (x, y, z)
线：    (x1, y1, z1), (x2, y2, z2)
三角形： (x1, y1, z1), (x2, y2, z2), (x3, y3, z3)
```

这些基本元素是构建复杂3D模型的基础，其中三角形最为常用，因为：

1. 三角形永远是平面
2. 任何多边形都可以分解为三角形
3. 硬件对三角形渲染优化最好

## 顶点着色器 和 片元着色器

让我详细解释一下 OpenGL 中的顶点着色器（Vertex Shader）和片元着色器（Fragment Shader）：

### 顶点着色器（Vertex Shader）

1. **功能定位**：
    - 处理每个顶点的数据
    - 是渲染管线中第一个可编程的阶段

2. **主要任务**：
    - 坐标转换（模型、视图、投影变换）
    - 顶点数据计算（位置、法线、纹理坐标等）
    - 输出 gl_Position（必须，表示顶点的最终位置）

3. **示例代码**：

```glsl
// 顶点着色器示例
attribute vec4 a_Position;  // 输入顶点位置
uniform mat4 u_Matrix;      // 变换矩阵

void main() {
    gl_Position = u_Matrix * a_Position;  // 计算顶点最终位置
}
```

### 片元着色器（Fragment Shader）

1. **功能定位**：
    - 处理每个像素（片元）的数据
    - 在光栅化之后执行

2. **主要任务**：
    - 计算每个像素的最终颜色
    - 处理纹理采样
    - 实现光照效果
    - 输出 gl_FragColor（片元的最终颜色）

3. **示例代码**：

```glsl
// 片元着色器示例
precision mediump float;    // 精度限定符
uniform vec4 u_Color;       // 统一颜色

void main() {
    gl_FragColor = u_Color;  // 设置片元颜色
}
```

### 两者的关系

1. **数据流向**：
    - 顶点着色器 → 图元组装 → 光栅化 → 片元着色器

2. **数据传递**：
    - 可以通过 varying 变量从顶点着色器传递数据到片元着色器
    - 片元着色器会对这些数据进行插值处理

3. **使用示例**：

```glsl
// 顶点着色器
attribute vec4 a_Position;
varying vec4 v_Color;      // 声明 varying 变量

void main() {
    gl_Position = a_Position;
    v_Color = vec4(1.0, 0.0, 0.0, 1.0);  // 传递红色
}

// 片元着色器
precision mediump float;
varying vec4 v_Color;      // 接收 varying 变量

void main() {
    gl_FragColor = v_Color;  // 使用插值后的颜色
}
```