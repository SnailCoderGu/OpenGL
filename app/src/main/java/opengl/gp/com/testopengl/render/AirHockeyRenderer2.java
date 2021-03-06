package opengl.gp.com.testopengl.render;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengl.gp.com.testopengl.R;
import opengl.gp.com.testopengl.util.ShaderHelper;
import opengl.gp.com.testopengl.util.TextResourceReader;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static javax.microedition.khronos.opengles.GL10.GL_POINTS;
import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLE_FAN;


public class AirHockeyRenderer2 implements GLSurfaceView.Renderer {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;

    private int program;

    private static final String A_POSIONT= "a_Position";
    private int aPositionLocation;

    private static final String A_COLOR = "a_Color";
    private int aColorLocation;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT+COLOR_COMPONENT_COUNT)*BYTES_PER_FLOAT;

    private Context mContext;

    public AirHockeyRenderer2(Context context) {
        mContext = context;
        float[] tableVerticesWithTriangles = {
                //Triagle Fan
                0,0,1f,1f,1f,
                -0.5f,-0.5f,0.7f,0.7f,0.7f,
                0.5f,-0.5f,0.7f,0.7f,0.7f,
                0.5f,0.5f,0.7f,0.7f,0.7f,
                -0.5f,0.5f,0.7f,0.7f,0.7f,
                -0.5f,-0.5f,0.7f,0.7f,0.7f,

                // Line 1
                -0.5f,0f,1f,0f,0f,
                0.5f,0f,1f,0f,0f,

                // Mallets
                0f,-0.25f,0f,0f,1f,
                0f,0.25f,1f,0f,0f
        };

        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f,0.0f,0.0f,0.0f);
        //加载着色器代码
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(mContext,R.raw.simple_vertex_shader2);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(mContext,R.raw.simple_fragment_shader2);
        //编译着色器代码
        int vertexShader = ShaderHelper.compileVertextShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        //链接着色器程序
        program = ShaderHelper.linkProgram(vertexShader,fragmentShader);

        ShaderHelper.validateProgram(program);

        //使用程序
        glUseProgram(program);

        aColorLocation = glGetAttribLocation(program,A_COLOR);
        aPositionLocation = glGetAttribLocation(program,A_POSIONT);

        //程序和具体的数据的指定
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GL_FLOAT,false,STRIDE,vertexData);
        //告诉OpenGL 把定点数据与做色漆中的a_Position关联起来
        glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation,COLOR_COMPONENT_COUNT,GL_FLOAT,false,STRIDE,vertexData);
        //告诉OpenGL 把定点数据与做色漆中的a_Color 关联起来
        glEnableVertexAttribArray(aColorLocation);



    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        //绘制三角扇形

        glDrawArrays(GL_TRIANGLE_FAN,0,6);
        //绘制分割线

        glDrawArrays(GL_LINES,6,2);
        //绘制点
        glDrawArrays(GL_POINTS,8,1);
        glDrawArrays(GL_POINTS,9,1);
    }
}
