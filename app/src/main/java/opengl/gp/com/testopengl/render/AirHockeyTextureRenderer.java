package opengl.gp.com.testopengl.render;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengl.gp.com.testopengl.R;
import opengl.gp.com.testopengl.android.program.ColorShaderProgram;
import opengl.gp.com.testopengl.android.program.TextureShaderProgram;
import opengl.gp.com.testopengl.objects.Mallet;
import opengl.gp.com.testopengl.objects.MalletOld;
import opengl.gp.com.testopengl.objects.Table;
import opengl.gp.com.testopengl.util.MatrixHelper;
import opengl.gp.com.testopengl.util.ShaderHelper;
import opengl.gp.com.testopengl.util.TextResourceReader;
import opengl.gp.com.testopengl.util.TextureHelper;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static javax.microedition.khronos.opengles.GL10.GL_POINTS;
import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLE_FAN;


public class AirHockeyTextureRenderer implements GLSurfaceView.Renderer {
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private Table table;
    private MalletOld mallet;

    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgram;

    private int texture;


    private Context mContext;


    public AirHockeyTextureRenderer(Context context) {
        mContext = context;


    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        glClearColor(1.0f,0.0f,0.0f,0.0f);
        table = new Table();
        mallet = new MalletOld();

        textureShaderProgram = new TextureShaderProgram(mContext);
        colorShaderProgram = new ColorShaderProgram(mContext);

        texture = TextureHelper.loadTexture(mContext,R.drawable.air_hockey_surface);


   }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0,0,width,height);
        final float aspectRatio = width > height ? (float)width/(float)height: (float)height/(float)width;

        MatrixHelper.perspectiveM(projectionMatrix,45,(float)width/(float)height,1f,10f);

        setIdentityM(modelMatrix,0);//设置单位矩阵，
        translateM(modelMatrix,0,0f,0f,-3.5f); //然后再平移-2
        rotateM(modelMatrix,0,-50f,1f,0f,0f);

        final float[] temp = new float[16];
        multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
        System.arraycopy(temp,0,projectionMatrix,0,temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

//        Draw the table
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(projectionMatrix,texture);
        table.bindData(textureShaderProgram);
        table.draw();

        colorShaderProgram.useProgram();
        colorShaderProgram.setUniforms(projectionMatrix);
        mallet.bindData(colorShaderProgram);
        mallet.draw();
    }
}
