package opengl.gp.com.testopengl.objects;

import opengl.gp.com.testopengl.android.program.TextureShaderProgram;
import opengl.gp.com.testopengl.data.VertexArray;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLE_FAN;
import static opengl.gp.com.testopengl.android.Constants.BYTES_PER_FLOAT;

public class Table {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_CORRDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT+TEXTURE_CORRDINATES_COMPONENT_COUNT)*BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            // Order of coordinates: X,Y,S,T

            // Triangle Fan
            0f,0f,0.5f,0.5f,
            -0.5f,-0.8f,0f,0.9f,
            0.5f,-0.8f,1f,0.9f,
            0.5f,0.8f,1f,0.1f,
            -0.5f,0.8f,0f,0.1f,
            -0.5f,-0.8f,0f,0.9f

    };

    private final VertexArray vertexArray;

    public Table(){
        vertexArray = new VertexArray(VERTEX_DATA);
    }
    public void bindData(TextureShaderProgram textureProgram){
        vertexArray.setVertexAttribPointer(0,textureProgram.getPositionAttributeLocation(),POSITION_COMPONENT_COUNT,STRIDE);
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT,textureProgram.getTextureCoordinatesAttributeLocation(),POSITION_COMPONENT_COUNT,STRIDE);
    }
    public void draw(){
        glDrawArrays(GL_TRIANGLE_FAN,0,6);
    }

}
