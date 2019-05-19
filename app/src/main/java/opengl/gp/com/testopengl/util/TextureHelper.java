package opengl.gp.com.testopengl.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

public class TextureHelper {
    public static int loadTexture(Context context, int resourceId){
        final int[] textureObjectIds = new int[1];
        glGenTextures(1,textureObjectIds,0);

        if(textureObjectIds[0]== 0){
            Log.e(TAG,"Could not generate a new OpenGL texture object.");
            return 0;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resourceId,options);
        if(bitmap == null){
            Log.e(TAG,"Resource ID " + resourceId + " could not be cecoded");
            glDeleteTextures(1,textureObjectIds,0);
        }
        glBindTexture(GL_TEXTURE_2D,textureObjectIds[0]);

        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);

        //加载位图数据到OpenGL 里面
        texImage2D(GL_TEXTURE_2D,0,bitmap,0);
        //位图已经被加载到OpengGL里面了，立即释放位图数据
        bitmap.recycle();

        //告诉OpenGL，生成MIP贴图所需要的级别
        glGenerateMipmap(GL_TEXTURE_2D);

        //解除与当前纹理绑定
        glBindTexture(GL_TEXTURE_2D,0);
        return textureObjectIds[0];
    }
}
