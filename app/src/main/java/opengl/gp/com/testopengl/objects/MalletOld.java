package opengl.gp.com.testopengl.objects;

import opengl.gp.com.testopengl.android.program.ColorShaderProgram;
import opengl.gp.com.testopengl.data.VertexArray;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static opengl.gp.com.testopengl.android.Constants.BYTES_PER_FLOAT;

public class MalletOld {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT)* BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA= {
            //order of coordinates: X,Y,R,G,B
            0,-0.4f,0f,0f,1f,
            0,0.4f,1f,0f,0f
    };
    private final VertexArray vertexArray;

    public MalletOld(){
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(ColorShaderProgram colorShaderProgram ){
        vertexArray.setVertexAttribPointer(0,colorShaderProgram.getPositionAttributeLocation(),POSITION_COMPONENT_COUNT,STRIDE);
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT,colorShaderProgram.getColorAttributeLocation(),POSITION_COMPONENT_COUNT,STRIDE);
    }

    public void draw(){
        glDrawArrays(GL_POINTS,0,2);
    }



}
