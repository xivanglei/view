package xl.gcs.com;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    String color = "r";

    Button changeColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
////        frameLayout.addView(new MyView(this));
//        frameLayout.addView(new DrawBitmap(this));
//        final DrawPath drawView = (DrawPath) findViewById(R.id.dv);
//        //清除
//        ImageButton button = (ImageButton) findViewById(R.id.clear);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawView.clear();
//            }
//        });
//        //变色
//        changeColor = (Button) findViewById(R.id.button);
//        changeColor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch(color) {
//                    case "r":
//                        drawView.paint.setColor(Color.YELLOW);
//                        changeColor.setTextColor(Color.YELLOW);
//                        color = "y";
//                        break;
//                    case "y":
//                        drawView.paint.setColor(Color.BLUE);
//                        changeColor.setTextColor(Color.BLUE);
//                        color = "b";
//                        break;
//                    case "b":
//                        drawView.paint.setColor(Color.RED);
//                        changeColor.setTextColor(Color.RED);
//                        color = "r";
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
    }
}
