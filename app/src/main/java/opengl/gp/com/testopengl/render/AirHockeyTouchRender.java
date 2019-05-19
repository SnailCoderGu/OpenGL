package opengl.gp.com.testopengl.render;

import android.content.Context;
import android.media.Image;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengl.gp.com.testopengl.R;
import opengl.gp.com.testopengl.android.program.ColorShaderProgram2;
import opengl.gp.com.testopengl.android.program.TextureShaderProgram;
import opengl.gp.com.testopengl.objects.Mallet;
import opengl.gp.com.testopengl.objects.Puck;
import opengl.gp.com.testopengl.objects.Table;
import opengl.gp.com.testopengl.util.Geometry;
import opengl.gp.com.testopengl.util.MatrixHelper;
import opengl.gp.com.testopengl.util.TextureHelper;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;
import static opengl.gp.com.testopengl.util.Geometry.intersectionPoint;
import static opengl.gp.com.testopengl.util.Geometry.intersects;


public class AirHockeyTouchRender implements GLSurfaceView.Renderer {
    private final float leftBound = -0.5f;
    private final float rightBound = 0.5f;
    private final float farBound = -0.8f;
    private final float nearBound = 0.8f;

    private final float[] viewMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectMatrix = new float[16];

    private Puck puck;

    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram2 colorShaderProgram;

    private int texture;

    private Context mContext;
    private Table table;
    private Mallet mallet;

    private boolean malletPressed = false;
    private Geometry.Point blueMalletPosition;
    private final float[] invertedViewProjectMatrix = new float[16];

    private Geometry.Point previousBlueMalletPosition;

    private Geometry.Point puckPosition;
    private Geometry.Vector puckVector;

    public AirHockeyTouchRender(Context context) {
        mContext = context;
    }
    float s = 4f;
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        glClearColor(0.0f,0.0f,0.0f,0.0f);
        table = new Table();
        
        mallet = new Mallet(0.08f,0.15f,32);
        puck = new Puck(0.06f,0.02f,32);

        textureShaderProgram = new TextureShaderProgram(mContext);
        colorShaderProgram = new ColorShaderProgram2(mContext);

        texture = TextureHelper.loadTexture(mContext,R.drawable.air_hockey_surface);

        blueMalletPosition = new Geometry.Point(0f,mallet.height/2f,0.4f);

        puckPosition = new Geometry.Point(0f,puck.height/2f,0f);
        puckVector = new Geometry.Vector(0f,0f,0f);

//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                s = s - 0.01f;
//                if(s <= 0){
//                    s = 4f;
//                }
//                setLookAtM(viewMatrix,0,0,s,2.2f,0f,0f,0f,0f,1f,0f);
//            }
//        },1000,50);


   }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0,0,width,height);
        final float aspectRatio = width > height ? (float)width/(float)height: (float)height/(float)width;

        MatrixHelper.perspectiveM(projectionMatrix,45,(float)width/(float)height,1f,10f);

        setLookAtM(viewMatrix,0,0f,1.2f,2.2f,0f,0f,0f,0f,1f,0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);


        //创建反向矩阵
        invertM(invertedViewProjectMatrix,0,viewProjectionMatrix,0);

        multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);

        positionTableScene();
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(modelViewProjectMatrix,texture);
        table.bindData(textureShaderProgram);
        table.draw();

        positionObjectScene(0f,mallet.height/2f,-0.4f);
        colorShaderProgram.useProgram();
        colorShaderProgram.setUniforms(modelViewProjectMatrix,1f,0f,0f);
        mallet.bindData(colorShaderProgram);
        mallet.draw();

        puckPosition = puckPosition.translate(puckVector);
        if(puckPosition.x < leftBound+puck.radius||puckPosition.x>rightBound-puck.radius){
            puckVector = new Geometry.Vector(-puckVector.x,puckVector.y,puckVector.z);
            puckVector = puckVector.scale(0.99f);
        }
        if(puckPosition.z < farBound+puck.radius||puckPosition.z > nearBound-puck.radius){
            puckVector = new Geometry.Vector(puckVector.x,puckVector.y,-puckVector.z);
            puckVector = puckVector.scale(0.99f);
        }
        puckPosition = new Geometry.Point(
                clamp(puckPosition.x,leftBound+puck.radius,rightBound-puck.radius),
                puckVector.y,
                clamp(puckPosition.z,farBound+puck.radius,nearBound-puck.radius)
        );
        positionObjectScene(puckPosition.x,puckPosition.y,puckPosition.z);
        colorShaderProgram.setUniforms(modelViewProjectMatrix,0.8f,0.8f,1f);
        puck.bindData(colorShaderProgram);
        puck.draw();

        //positionObjectScene(0f,mallet.height/2f,0.4f);
        positionObjectScene(blueMalletPosition.x,blueMalletPosition.y,blueMalletPosition.z);
        colorShaderProgram.setUniforms(modelViewProjectMatrix,0f,0f,1f);
        mallet.bindData(colorShaderProgram);
        mallet.draw();

        puckVector = puckVector.scale(0.99f);

    }
    private void positionTableScene(){

        setIdentityM(modelMatrix,0);
        rotateM(modelMatrix,0,-90f,1,0f,0f);
        multiplyMM(modelViewProjectMatrix,0,viewProjectionMatrix,0,modelMatrix,0);
    }
    private void positionObjectScene(float x,float y,float z){
        setIdentityM(modelMatrix,0);
        translateM(modelMatrix,0,x,y,z);
        multiplyMM(modelViewProjectMatrix,0,viewProjectionMatrix,0,modelMatrix,0);
    }

    public void handleTouchPress(float normalizedX, float normalizedY) {
        Geometry.Ray ray = convertNormalized2DPointToRay(normalizedX,normalizedY);

        Geometry.Sphere mallBoundingSphere = new Geometry.Sphere(new Geometry.Point(blueMalletPosition.x,blueMalletPosition.y,blueMalletPosition.z),mallet.height/2f);

        malletPressed = intersects(mallBoundingSphere,ray);
    }


    public void handleTouchDrag(float normalizedX, float normalizedY) {
        if(malletPressed){
            Geometry.Ray ray = convertNormalized2DPointToRay(normalizedX,normalizedY);

            Geometry.Plane plane = new Geometry.Plane(new Geometry.Point(0,0,0),new Geometry.Vector(0,1,0));

            Geometry.Point touchedPoint = intersectionPoint(ray,plane);

            previousBlueMalletPosition = blueMalletPosition;
            blueMalletPosition = new Geometry.Point(clamp(touchedPoint.x,leftBound+mallet.radius,rightBound-mallet.radius),mallet.height/2f,
                    clamp(touchedPoint.z,0f+mallet.radius,nearBound-mallet.radius));



            float distance = Geometry.vectorBetween(blueMalletPosition,puckPosition).length();
            if(distance < (puck.radius+mallet.radius)){
                puckVector = Geometry.vectorBetween(previousBlueMalletPosition,blueMalletPosition);
            }
        }
    }

    //取中间值
    private float clamp(float value,float min,float max){
        return Math.min(max,Math.max(value,min));
    }

    private Geometry.Ray convertNormalized2DPointToRay(float normalizedX,float normalizedY)
    {

        final float[] nearPointNdc = {normalizedX,normalizedY,-1,1};
        final float[] farPointNdc = {normalizedX,normalizedY,1,1};

        final float[] nearPointWorld = new float[4];
        final float[] farPointWorld = new float[4];

        multiplyMV(nearPointWorld,0,invertedViewProjectMatrix,0,nearPointNdc,0);
        multiplyMV(farPointWorld,0,invertedViewProjectMatrix,0,farPointNdc,0);

        divideByW(nearPointWorld);
        divideByW(farPointWorld);

        Geometry.Point nearPointRay = new Geometry.Point(nearPointWorld[0],nearPointWorld[1],nearPointWorld[2]);

        Geometry.Point farPointRay = new Geometry.Point(farPointWorld[0],farPointWorld[1],farPointWorld[2]);

        return new Geometry.Ray(nearPointRay,Geometry.Ray.vectorBetween(nearPointRay,farPointRay));
    }
    private void divideByW(float[] vector){
        vector[0] /= vector[3];
        vector[1] /= vector[3];
        vector[2] /= vector[3];
    }
}
