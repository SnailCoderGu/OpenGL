package opengl.gp.com.testopengl.android.program;

import android.content.Context;

import opengl.gp.com.testopengl.R;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

public class ColorShaderProgram extends ShaderProgram {

    // Uniform locations
    private final int uMatrixLocation;

    //Attribute locations
    private final int aPositionLocation;
    private final int aColorLocation;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.simple_vertex_shader3, R.raw.simple_fragment_shader2);

        //Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program,U_MATRIX);
        // Retrieve attribute locatins for the shader program
        aPositionLocation = glGetAttribLocation(program,A_POSITION);
        aColorLocation = glGetAttribLocation(program,A_COLOR);
    }
    public void setUniforms(float[] matrix){
        glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
    }

    public int getColorAttributeLocation(){
        return aColorLocation;
    }
    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }
}
