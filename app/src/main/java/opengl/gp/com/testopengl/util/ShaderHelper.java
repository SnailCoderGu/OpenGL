package opengl.gp.com.testopengl.util;

/**
 * 1.编译着色器
 * 2. 代码链接着色器
 */

import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static int compileVertextShader(String shaderCode){
        return compileShader(GL_VERTEX_SHADER,shaderCode);
    }
    public static int compileFragmentShader(String shaderCode){
       return compileShader(GL_FRAGMENT_SHADER,shaderCode);
    }
    private static int compileShader(int type,String shaderCode){
        final int shaderObjectId = glCreateShader(type);
        if(shaderObjectId == 0){
            Log.e(TAG,"Could not create new shader.");
            return 0;
        }
        glShaderSource(shaderObjectId,shaderCode);
        glCompileShader(shaderObjectId);
        //检查是否成功编译了这个着色器
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId,GL_COMPILE_STATUS,compileStatus,0);
        //获得着色器日志信息
        //Log.e(TAG,"Results of compiling shader "+"\n"+shaderCode+"\n"+glGetShaderInfoLog(shaderObjectId));

        //判断编译结果
        if(compileStatus[0] ==  0){
            //如果失败，删除加载的着色器
            glDeleteShader(shaderObjectId);
            Log.e(TAG,"Compile of shader failed.");
            return 0;
        }
        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId,int fragmentShaderId){
        int programObjectId = glCreateProgram();
        if(programObjectId == 0){
            Log.e(TAG,"Could not create new program.");
            return 0;
        }
        //附上着色器
        glAttachShader(programObjectId,vertexShaderId);
        glAttachShader(programObjectId,fragmentShaderId);
        //链接程序，把这些着色器联合起来
        glLinkProgram(programObjectId);

        //检查是否成功链接了这个着色器
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId,GL_LINK_STATUS,linkStatus,0);
        //获得着色器日志信息
        Log.e(TAG,"Results of link "+"\n"+vertexShaderId+"-"+fragmentShaderId+":\n "+programObjectId +":"+glGetProgramInfoLog(programObjectId));
        //判断链接结果
        if(linkStatus[0] == 0){
            glDeleteProgram(programObjectId);
            Log.e(TAG,"Linking of proram failed.");
            return 0;
        }
        return programObjectId;
    }
    //检测着色器程序是否不可用，或者低效率的
    public static boolean validateProgram(int programObjectId){
        glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        glGetShaderiv(programObjectId,GL_VALIDATE_STATUS,validateStatus,0);
        Log.e(TAG,"Results of validating program"+"\n"+validateStatus[0]+":\n"+glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }


}
