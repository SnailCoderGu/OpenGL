package opengl.gp.com.testopengl.android.program;

import android.content.Context;

import opengl.gp.com.testopengl.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class TextureShaderProgram extends ShaderProgram{
    // Uniform locations
    private final int uMatrixLocation;
    private final int uTextureUnitLoation;

    //Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;

    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader , R.raw.texture_fragmeng_shader);


        //Retrieve uniform locations for the shader program.
        uTextureUnitLoation = glGetUniformLocation(program,U_TEXTURE_UNIT);
        uMatrixLocation = glGetUniformLocation(program,U_MATRIX);

        // Retrieve attribute locatins for the shader program
        aPositionLocation = glGetAttribLocation(program,A_POSITION);
        aTextureCoordinatesLocation = glGetAttribLocation(program,A_TEXTURE_COORDINATES);
    }

    public void setUniforms(float[] matrix, int textureId){
        //Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);

        //Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0);

        // Bind the texture to this unit
        glBindTexture(GL_TEXTURE_2D,textureId);

        //Tell the texture uniform sample to use this texture in the shader by tell it to read from texture unit 0
        glUniform1f(uTextureUnitLoation,0);

    }
    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }
    public int getTextureCoordinatesAttributeLocation(){
        return aTextureCoordinatesLocation;
    }
}
