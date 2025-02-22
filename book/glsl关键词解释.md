# `#version` 指令表示着色器使用的 GLSL 版本号。

`#version 100` 具体含义：

1. 这是 OpenGL ES 2.0 使用的 GLSL ES 版本
2. 100 表示 GLSL ES 1.00 版本
3. 这是移动设备上最基础和广泛支持的版本

GLSL 主要版本对应关系：

- `#version 100` - OpenGL ES 2.0
- `#version 300 es` - OpenGL ES 3.0
- `#version 310 es` - OpenGL ES 3.1
- `#version 320 es` - OpenGL ES 3.2

注意事项：

1. `#version` 必须是着色器代码的第一行（注释除外）
2. 不同版本支持的特性和语法有所不同
3. 移动端开发建议使用 ES 版本
4. 版本向后兼容，但不向前兼容

OpenGL ES 项目中，使用 `#version 100` 是合适的选择，因为它能确保在大多数 Android 设备上运行。

# uniform 和 attribute 主要区别：

1. **数据更新频率**

- `attribute`: 每个顶点都可以不同，逐顶点变化
- `uniform`: 在一次绘制过程中保持不变，所有顶点共用同一个值

2. **使用位置**

- `attribute`: 只能在顶点着色器中使用
- `uniform`: 可以在顶点着色器和片段着色器中使用

3. **典型用途**

```glsl
// attribute 示例：顶点数据
attribute vec4 a_Position;    // 顶点位置
attribute vec4 a_Color;      // 顶点颜色
attribute vec2 a_TexCoord;   // 纹理坐标

// uniform 示例：全局数据
uniform mat4 u_Matrix;       // 变换矩阵
uniform vec4 u_Color;        // 统一的颜色
uniform float u_Time;        // 时间变量
```

4. **数据传递方式**

```kotlin
// attribute 数据传递
glVertexAttribPointer(location, size, type, normalized, stride, buffer)
glEnableVertexAttribArray(location)

// uniform 数据传递
glUniform4f(location, r, g, b, a)
glUniformMatrix4fv(location, count, transpose, matrix)
```

5. **适用场景**

- `attribute`:
    - 顶点坐标
    - 顶点颜色
    - 法线向量
    - 纹理坐标

- `uniform`:
    - 变换矩阵
    - 光照参数
    - 全局颜色
    - 时间等动画参数