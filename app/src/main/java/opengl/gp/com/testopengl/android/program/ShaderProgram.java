package opengl.gp.com.testopengl.android.program;

import android.content.Context;

import opengl.gp.com.testopengl.util.ShaderHelper;
import opengl.gp.com.testopengl.util.TextResourceReader;

import static android.opengl.GLES20.glUseProgram;

/**
 * 使用指定的着色器程序构建一个OpengGL着色器程序。
 * useProgram() 告诉OpengGL 接下来的渲染要使用这个程序
 */
public class ShaderProgram {
    // Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_COLOR = "u_Color";

    // Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    //Shader program
    protected final int program;
    protected ShaderProgram(Context context,int vertexShaderResourceId,int fragmentShaderResourceId){
        //Compile the shaders and link the program
        program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context,vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context,fragmentShaderResourceId));
    }

    public void useProgram(){
        // Set the current OpengGL shader program to this program
        glUseProgram(program);
    }
}
