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

# `glTexParameterf` 和 `glTexParameteri` 是 OpenGL 中用于设置纹理参数的两个函数，它们的主要区别在于参数值的数据类型：

1. `glTexParameterf`：接受浮点数（float）类型的参数值
   ```c
   void glTexParameterf(GLenum target, GLenum pname, GLfloat param);
   ```

2. `glTexParameteri`：接受整数（integer）类型的参数值
   ```c
   void glTexParameteri(GLenum target, GLenum pname, GLint param);
   ```

在实际使用中：

- 大多数纹理参数（如过滤模式、包装模式等）都是使用预定义的常量值，这些常量是整数，因此通常使用
  `glTexParameteri`
- 只有少数参数需要浮点值，这时才会使用 `glTexParameterf`

例如，设置纹理过滤模式：

```c
// 使用整数常量，所以用 glTexParameteri
glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
```

在 Android 的 OpenGL ES 中，大多数情况下你会使用 `glTexParameteri`，因为大部分纹理参数都是使用整数常量来设置的。

# glDrawElements

`glDrawElements` 是 OpenGL ES 中用于绘制图元（如点、线、三角形等）的一个函数。它允许你使用索引数组来指定顶点的绘制顺序，从而提高绘制效率。

## 函数原型

```c
void glDrawElements(GLenum mode, GLsizei count, GLenum type, const GLvoid *indices);
```

## 参数解释

1. **mode**: 指定绘制的图元类型。常用的值包括：
    - `GL_POINTS`: 绘制点。
    - `GL_LINES`: 绘制线段。
    - `GL_LINE_STRIP`: 绘制线段条带。
    - `GL_LINE_LOOP`: 绘制线段环。
    - `GL_TRIANGLES`: 绘制三角形。
    - `GL_TRIANGLE_STRIP`: 绘制三角形条带。
    - `GL_TRIANGLE_FAN`: 绘制三角形扇形。

2. **count**: 指定要绘制的元素数量。

3. **type**: 指定索引数组中每个索引的类型。常用的值包括：
    - `GL_UNSIGNED_BYTE`: 无符号字节。
    - `GL_UNSIGNED_SHORT`: 无符号短整型。
    - `GL_UNSIGNED_INT`: 无符号整型。

4. **indices**: 指向索引数组的指针。索引数组指定了顶点的绘制顺序。

## 使用示例

以下是一个简单的使用 `glDrawElements` 绘制一个三角形的示例：

```c
// 定义顶点数组
GLfloat vertices[] = {
    0.0f,  0.5f, 0.0f, // 顶点1
   -0.5f, -0.5f, 0.0f, // 顶点2
    0.5f, -0.5f, 0.0f  // 顶点3
};

// 定义索引数组
GLushort indices[] = { 0, 1, 2 };

// 启用顶点数组
glEnableClientState(GL_VERTEX_ARRAY);

// 指定顶点数组
glVertexPointer(3, GL_FLOAT, 0, vertices);

// 使用索引数组绘制三角形
glDrawElements(GL_TRIANGLES, 3, GL_UNSIGNED_SHORT, indices);

// 禁用顶点数组
glDisableClientState(GL_VERTEX_ARRAY);
```

在这个示例中，我们定义了一个包含三个顶点的数组和一个包含绘制顺序的索引数组。然后，我们启用顶点数组并指定顶点数据。最后，我们使用
`glDrawElements` 函数按照索引数组的顺序绘制三角形。

## 总结

`glDrawElements` 是OpenGL
ES中一个高效的绘图函数，通过使用索引数组，可以减少顶点数据的冗余，提高绘图性能。掌握其参数和使用方法，可以帮助你在Android开发中实现复杂的图形绘制。
