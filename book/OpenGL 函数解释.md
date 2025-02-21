# `glVertexAttribPointer` 函数的作用和用法：

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