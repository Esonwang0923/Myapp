package com.example.admin.godzillaandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.example.admin.godzillaandroid.comm.Constants;
import com.example.admin.godzillaandroid.comm.OkHttp;
import com.example.admin.godzillaandroid.dao.People;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class Main2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TypefaceProvider.registerDefaultIconSets();
    }

    public  void Clicking(View view){
        try{
            switch (view.getId()){
                case R.id.sumbButton://此处是对布局中设置的id直接进行判断，
                    // 不需要对控件进行获取（findviewByID）
                    TextView textViewName = (TextView) findViewById(R.id.autoCompleteTextView);
                    EditText editTextPwd = (EditText) findViewById(R.id.editText2);
                    String count = textViewName.getText().toString();
                    String pwd = editTextPwd.getText().toString();
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("count", count);
                    jsonObj.put("password", pwd);

                    OkHttp okHttp = new OkHttp(Constants.UserByCountAndPassword,jsonObj.toString());
                    Call call = okHttp.getCall();
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if(response.isSuccessful()){
                                Intent intent=new Intent(Main2Activity.this,MainMenuActivity.class);
                                //添加传送数据
                                String result = response.body().string();
                                intent.putExtra("person",result);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                    break;
            }
        }catch (Exception E){

        }
    }
}
