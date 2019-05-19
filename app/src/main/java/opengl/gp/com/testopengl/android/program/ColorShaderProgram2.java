package opengl.gp.com.testopengl.android.program;

import android.content.Context;

import opengl.gp.com.testopengl.R;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class ColorShaderProgram2 extends ShaderProgram {

    // Uniform locations
    private final int uMatrixLocation;
    private final int uColorLocation;

    //Attribute locations
    private final int aPositionLocation;


    public ColorShaderProgram2(Context context) {
        super(context, R.raw.simple_vertex_shader4, R.raw.simple_fragment_shader4);

        //Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program,U_MATRIX);
        uColorLocation = glGetUniformLocation(program,U_COLOR);

        // Retrieve attribute locatins for the shader program
        aPositionLocation = glGetAttribLocation(program,A_POSITION);

    }
    public void setUniforms(float[] matrix,float r,float g,float b){
        glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
        glUniform4f(uColorLocation,r,g,b,1f);
    }

    public int getColorAttributeLocation(){
        return uColorLocation;
    }
    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }
}
