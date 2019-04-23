package opengl.gp.com.testopengl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TestAirHockey1();
    }

    private void TestFirstOpenenGl(){
        startActivity(new Intent(MainActivity.this,FirstOpenGLProjectActivity.class));
    }

    private void TestAirHockey1(){
        startActivity(new Intent(MainActivity.this,AirHockey1.class));
    }
}
