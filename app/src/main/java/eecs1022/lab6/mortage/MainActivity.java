package eecs1022.lab6.mortage;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Principal;
import java.util.Locale;

import ca.roumani.i2c.MPro;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, SensorEventListener {
    private TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.tts = new TextToSpeech(this,this);
    }
    public void analyze(View view){
        try {
            MPro mp = new MPro();
            mp.setPrinciple(((EditText) findViewById(R.id.pbox)).getText().toString());
            mp.setAmortization(((EditText) findViewById(R.id.abox)).getText().toString());
            mp.setInterest(((EditText) findViewById(R.id.ibox)).getText().toString());
            String s;
            String monthlyPayment = "Monthly Payment = " + mp.computePayment("%,.2f");
            s = monthlyPayment + "\n\n";
            s += "By making this payment monthly for " + ((EditText) findViewById(R.id.abox)).getText().toString() + " years, the mortgage will be paid full. But if you " +
                    "terminate the mortgage on its nth anniversary, the balance still owing depends on n as shown below:";
            s += "\n\n\n\n";
            s+= String.format("%8c", 'n') + String.format("%16s", "balance") + "\n\n";
            String balance0 = String.format("%8d", 0) + mp.outstandingAfter(0, "%,16.0f");
            s = s + balance0 + "\n\n";
            String balance1 = String.format("%8d", 1) + mp.outstandingAfter(1, "%,16.0f");
            s += balance1 + "\n\n";
            String balance2 = String.format("%8d", 2) + mp.outstandingAfter(2, "%,16.0f");
            s += balance2 + "\n\n";
            String balance3 = String.format("%8d", 3) + mp.outstandingAfter(3, "%,16.0f");
            s += balance3 + "\n\n";
            String balance4 = String.format("%8d", 4) + mp.outstandingAfter(4, "%,16.0f");
            s += balance4 + "\n\n";
            String balance5 = String.format("%8d", 5) + mp.outstandingAfter(5, "%,16.0f");
            s += balance5 + "\n\n";
            String balance10 = String.format("%8d", 10) + mp.outstandingAfter(10, "%,16.0f");
            s += balance10 + "\n\n";
            String balance15 = String.format("%8d", 15) + mp.outstandingAfter(15, "%,16.0f");
            s += balance15 + "\n\n";
            String balance20 = String.format("%8d", 20) + mp.outstandingAfter(20, "%,16.0f");
            s += balance20 + "\n\n";
            ((TextView) findViewById(R.id.output)).setText(s);
            tts.speak(monthlyPayment,TextToSpeech.QUEUE_FLUSH,null);
        }
        catch(Exception e){
            Toast label = Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT);
            label.show();
        }
    }

    @Override
    public void onInit(int status) {
       this.tts.setLanguage(Locale.US);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double ax = event.values[0];
        double ay = event.values[1];
        double az = event.values[2];
        double a = Math.sqrt(ax*ax + ay*ay + az*az);
        if(a > 20){
            ((EditText)findViewById(R.id.pbox)).setText("");
            ((EditText)findViewById(R.id.abox)).setText("");
            ((EditText)findViewById(R.id.ibox)).setText("");
            ((TextView)findViewById(R.id.output)).setText("");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}