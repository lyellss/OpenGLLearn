# `glVertexAttribPointer`

### 函数签名

```c
void glVertexAttribPointer(
    GLuint index,          // 属性位置
    GLint size,            // 每个顶点属性的组件数量
    GLenum type,           // 数据类型
    GLboolean normalized,  // 是否需要归一化
    GLsizei stride,        // 步长
    const void* pointer    // 数据缓冲区
)
```

### 参数详解

1. **index**（属性位置）
    - 指定要修改的顶点属性的索引
    - 通过 `glGetAttribLocation` 获取
    - 在您的代码中是 `aPositionLocation`

2. **size**（组件数量）
    - 指定每个顶点属性的组件数量
    - 可选值：1、2、3、4
    - 您的代码中用 `POSITION_COMPONENT_COUNT = 2`，表示每个顶点有 x、y 两个分量

3. **type**（数据类型）
    - 指定数据类型
    - 常用值：
        - `GL_FLOAT`：浮点型
        - `GL_BYTE`：字节型
        - `GL_UNSIGNED_BYTE`：无符号字节
        - `GL_SHORT`：短整型

4. **normalized**（是否归一化）
    - 是否将非浮点数据归一化到 [0, 1] 或 [-1, 1] 区间
    - 对于 `GL_FLOAT` 类型，此参数无效

5. **stride**（步长）
    - 连续顶点属性之间的偏移量（字节数）
    - 0 表示数据是紧密排列的
    - 计算方式：所有属性的字节数之和

6. **pointer**（数据缓冲区）
    - 顶点数据缓冲区
    - 在 Java/Kotlin 中使用 `FloatBuffer`

### 在您的代码中的使用

```kotlin
glVertexAttribPointer(
    aPositionLocation,        // 位置属性的索引
    POSITION_COMPONENT_COUNT, // 每个顶点2个分量(x,y)
    GL_FLOAT,                // 数据类型为浮点数
    false,                   // 不需要归一化
    0,                       // 数据是紧密排列的
    vertexData              // 顶点数据缓冲区
)
```

### 实际应用示例

如果顶点还包含颜色属性：

```kotlin
// 顶点数据：位置(x,y) + 颜色(r,g,b)
val vertexData = floatArrayOf(
    // x,  y,    r,    g,    b
    0f, 0f, 1.0f, 0.0f, 0.0f,  // 顶点1
    1f, 0f, 0.0f, 1.0f, 0.0f,  // 顶点2
    0f, 1f, 0.0f, 0.0f, 1.0f   // 顶点3
)

// 位置属性
glVertexAttribPointer(
    positionLocation,
    2,                          // x, y
    GL_FLOAT,
    false,
    5 * BYTES_PER_FLOAT,       // 每个顶点占用5个float
    vertexData
)

// 颜色属性
glVertexAttribPointer(
    colorLocation,
    3,                          // r, g, b
    GL_FLOAT,
    false,
    5 * BYTES_PER_FLOAT,       // 每个顶点占用5个float
    vertexData.position(2)      // 偏移2个float到颜色数据
)
```

### 注意事项

1. 调用此函数前需要启用顶点属性数组：

```kotlin
glEnableVertexAttribArray(attributeLocation)
```

2. 确保数据缓冲区正确设置了 position：

```kotlin
vertexData.position(0)  // 重置位置到起始处
```

3. 步长（stride）的计算要考虑所有属性的大小：
    - 步长 = 所有属性组件数 × 每个组件的字节数

4. 使用完后应该禁用顶点属性数组：

```kotlin
glDisableVertexAttribArray(attributeLocation)
```

这个函数是 OpenGL 中非常重要的一个函数，它建立了顶点着色器中属性变量与顶点数据之间的关联，是渲染管线中不可或缺的一环。

# `glViewport`

`glViewport` 函数用于设置 OpenGL 的视口（Viewport），它定义了渲染的显示区域。

函数签名：

```kotlin
glViewport(x: Int, y: Int, width: Int, height: Int)
```

参数说明：

1. `x`：视口的左下角 x 坐标
2. `y`：视口的左下角 y 坐标
3. `width`：视口的宽度
4. `height`：视口的高度

功能作用：

1. 定义 OpenGL 渲染内容显示在屏幕上的区域
2. 将 NDC（标准化设备坐标：-1 到 1）映射到屏幕坐标
3. 处理屏幕旋转和尺寸变化

在您的代码中：

```kotlin
override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
    glViewport(0, 0, width, height)
}
```

这段代码表示：

- 从屏幕左下角(0,0)开始
- 使用整个屏幕作为渲染区域
- 当屏幕尺寸或方向改变时自动调整视口

注意事项：

1. 坐标原点在屏幕左下角
2. 宽高通常设置为实际屏幕尺寸
3. 可以设置部分屏幕区域作为渲染区域
4. 在 Surface 大小改变时需要重新设置

# `glGetAttribLocation`

是用于获取着色器程序中 attribute 变量位置的方法。

函数签名：

```kotlin
glGetAttribLocation(program: Int, name: String): Int
```

参数说明：

1. `program`：着色器程序ID
2. `name`：attribute变量的名称（必须与着色器中定义的名称完全一致）

返回值：

- 返回 attribute 变量的位置索引
- 如果变量不存在，返回 -1

在您的代码中的使用示例：

```kotlin
// 获取 a_Position 变量的位置
aPositionLocation = glGetAttribLocation(programId, "a_Position")

// 对应的着色器中的声明
// attribute vec4 a_Position;
```

主要用途：

1. 获取顶点属性的位置
2. 用于后续的顶点数据绑定
3. 与 `glVertexAttribPointer` 配合使用

使用建议：

1. 程序初始化时获取位置
2. 检查返回值是否有效
3. 缓存位置值避免重复获取
4. 注意变量名大小写

注意事项：

1. 必须在 `glUseProgram` 之后调用
2. 变量必须是 attribute 类型
3. 变量必须在着色器中被使用