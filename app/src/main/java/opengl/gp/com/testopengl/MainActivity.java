package opengl.gp.com.testopengl;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TestAirHockey3();
    }

    private void TestFirstOpenenGl(){
        startActivity(new Intent(MainActivity.this,FirstOpenGLProjectActivity.class));
    }

    private void TestAirHockey1(){
        startActivity(new Intent(MainActivity.this,AirHockey1.class));
    }
    private void TestAirHockey2(){
        startActivity(new Intent(MainActivity.this,AirHockey2.class));
    }
    private void TestAirHockey3(){
        startActivity(new Intent(MainActivity.this,AirHockey3.class));
    }
}
