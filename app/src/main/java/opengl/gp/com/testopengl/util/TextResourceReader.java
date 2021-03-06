package opengl.gp.com.testopengl.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 加载着色器代码
 */
public class TextResourceReader {
    public static String readTextFileFromResource(Context context,int resourceId){
        StringBuilder body = new StringBuilder();
        try{
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String nextLine;
            while((nextLine = bufferedReader.readLine()) != null){
                body.append(nextLine);
                body.append('\n');
            }
        }catch (Exception e){

        }
        return body.toString();
    }
}
