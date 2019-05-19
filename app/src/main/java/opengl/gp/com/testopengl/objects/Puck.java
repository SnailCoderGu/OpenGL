package opengl.gp.com.testopengl.objects;

import java.util.List;

import opengl.gp.com.testopengl.android.program.ColorShaderProgram;
import opengl.gp.com.testopengl.android.program.ColorShaderProgram2;
import opengl.gp.com.testopengl.data.VertexArray;
import opengl.gp.com.testopengl.util.Geometry;

public class Puck {
    private static final int POSITION_COMPONENT_COUNT = 3;
    public final float radius,height;

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Puck(float radius,float height,int numPointsAroundPuck){
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createPuck(new Geometry.Cylinder(
                new Geometry.Point(0,0,0f),radius,height),numPointsAroundPuck);

        this.radius = radius;
        this.height = height;

        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    public void bindData(ColorShaderProgram2 colorShaderProgram){
        vertexArray.setVertexAttribPointer(0,colorShaderProgram.getPositionAttributeLocation(),POSITION_COMPONENT_COUNT,0);
    }
    public void draw(){
        for(ObjectBuilder.DrawCommand drawCommand:drawList){
            drawCommand.draw();
        }
    }


}
