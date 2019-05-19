package opengl.gp.com.testopengl.objects;

import java.util.List;

import opengl.gp.com.testopengl.android.program.ColorShaderProgram;
import opengl.gp.com.testopengl.android.program.ColorShaderProgram2;
import opengl.gp.com.testopengl.android.program.TextureShaderProgram;
import opengl.gp.com.testopengl.data.VertexArray;
import opengl.gp.com.testopengl.util.Geometry;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLE_FAN;
import static opengl.gp.com.testopengl.android.Constants.BYTES_PER_FLOAT;

public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius;
    public final float height;

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Mallet(float radius,float height,int numPointsAroundMallet){
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createMallet(
                new Geometry.Point(0f,0f,0f),radius,height,numPointsAroundMallet);
        this.radius = radius;
        this.height = height;

        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;

    }

    public void bindData(ColorShaderProgram2 colorShaderProgram ){
       vertexArray.setVertexAttribPointer(0,colorShaderProgram.getPositionAttributeLocation(),POSITION_COMPONENT_COUNT,0);
    }

    public void draw(){
        for(ObjectBuilder.DrawCommand drawCommand:drawList){
            drawCommand.draw();
        }
    }



}
