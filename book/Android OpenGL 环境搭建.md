# Android OpenGL 环境搭建

## 环境搭建

1. AndroidManifest.xml 配置

```xml:d:\AndroidProjects\HallPlayer\app\src\main\AndroidManifest.xml
<uses-feature android:glEsVersion="0x00030000" android:required="true" />
```

这里声明了应用需要 OpenGL ES 3.0 支持，0x00030000 表示版本 3.0。required="true" 表示这是必需的，不支持的设备将无法安装。

2. GLSurfaceView 是什么：

- 这是 Android 提供的专门用于 OpenGL 渲染的视图容器
- 管理 EGL context
- 提供专门的渲染线程
- 处理渲染周期

3. Renderer 的三个核心方法：

```kotlin:d:\AndroidProjects\HallPlayer\app\src\main\java\com\mooncell\hallplayer\render\MyGLRenderer.kt
class MyGLRenderer : GLSurfaceView.Renderer {
    // 1. Surface 创建时调用，用于初始化
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 设置清屏颜色为黑色
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        // 这里可以：
        // - 初始化着色器
        // - 加载纹理
        // - 创建缓冲区
    }

    // 2. Surface 尺寸改变时调用
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // 设置视口大小
        GLES30.glViewport(0, 0, width, height)
        // 这里可以：
        // - 计算宽高比
        // - 更新投影矩阵
    }

    // 3. 每帧渲染时调用
    override fun onDrawFrame(gl: GL10?) {
        // 清除颜色缓冲
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        // 这里进行实际的绘制操作
    }
}
```

4. 渲染模式说明：

```kotlin:d:\AndroidProjects\HallPlayer\app\src\main\java\com\mooncell\hallplayer\view\MyGLSurfaceView.kt
// RENDERMODE_CONTINUOUSLY: 持续渲染，每帧都调用 onDrawFrame
// RENDERMODE_WHEN_DIRTY: 按需渲染，调用 requestRender() 时才渲染
renderMode = RENDERMODE_WHEN_DIRTY
```

5. 着色器工具类的作用：

```kotlin:d:\AndroidProjects\HallPlayer\app\src\main\java\com\mooncell\hallplayer\utils\ShaderUtils.kt
object ShaderUtils {
    // 加载并编译着色器
    fun loadShader(type: Int, shaderCode: String): Int {
        // 创建着色器
        val shader = GLES30.glCreateShader(type)
        // 加载着色器源码
        GLES30.glShaderSource(shader, shaderCode)
        // 编译着色器
        GLES30.glCompileShader(shader)
        // 检查编译状态
        val compiled = IntArray(1)
        GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] == 0) {
            val error = GLES30.glGetShaderInfoLog(shader)
            GLES30.glDeleteShader(shader)
            throw RuntimeException("Shader compile error: $error")
        }
        return shader
    }

    // 创建着色器程序
    fun createProgram(vertexSource: String, fragmentSource: String): Int {
        // 加载顶点着色器和片段着色器
        val vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexSource)
        val fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentSource)
        
        // 创建程序
        val program = GLES30.glCreateProgram()
        // 附加着色器
        GLES30.glAttachShader(program, vertexShader)
        GLES30.glAttachShader(program, fragmentShader)
        // 链接程序
        GLES30.glLinkProgram(program)
        
        // 检查链接状态
        val linked = IntArray(1)
        GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linked, 0)
        if (linked[0] == 0) {
            val error = GLES30.glGetProgramInfoLog(program)
            GLES30.glDeleteProgram(program)
            throw RuntimeException("Program link error: $error")
        }
        
        // 删除着色器，它们已经链接到程序中
        GLES30.glDeleteShader(vertexShader)
        GLES30.glDeleteShader(fragmentShader)
        
        return program
    }
}
```

```kotlin
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)
```

setEGLConfigChooser 的参数配置 EGL 的颜色和缓冲区格式，依次是：

1. redSize (8): 红色分量的位数
    - 表示红色通道的精度
    - 8位可以表示256种红色深度

2. greenSize (8): 绿色分量的位数
    - 表示绿色通道的精度
    - 8位可以表示256种绿色深度

3. blueSize (8): 蓝色分量的位数
    - 表示蓝色通道的精度
    - 8位可以表示256种蓝色深度

4. alphaSize (8): 透明度分量的位数
    - 表示透明度通道的精度
    - 8位可以表示256级透明度

5. depthSize (16): 深度缓冲区的位数
    - 用于3D渲染中的深度测试
    - 16位提供较好的深度精度
    - 常用值：16或24

6. stencilSize (0): 模板缓冲区的位数
    - 用于实现模板测试
    - 0表示不使用模板缓冲
    - 常用值：0或8

总结：

- RGBA各8位 = 32位真彩色
- 16位深度缓冲 = 适中的深度精度
- 0位模板缓冲 = 不使用模板测试