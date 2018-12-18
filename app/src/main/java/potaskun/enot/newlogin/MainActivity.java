package potaskun.enot.newlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public EditText login;
    public EditText pass;
    private ProgressDialog dialog;
    private InputStream is;
    SecondActivity url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button1);
        login = findViewById(R.id.editText1);
        pass  = findViewById(R.id.editText2);
        Global.LOGIN = login.getText().toString();
        Global.PASS = pass.getText().toString();


        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                new RequestTask().execute("https://math123.ru/testjson1.php");
            }
        });
        Intent intent = getIntent();
        TextView tv = findViewById(R.id.error);
        tv.setText(intent.getStringExtra("error"));
    }

    class RequestTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... param) {
            try{
                //создаем запрос на сервер
                DefaultHttpClient hc = new DefaultHttpClient();
                ResponseHandler<String> res = new BasicResponseHandler();
                //он у нас будет посылать post запрос
                HttpPost postMethod = new HttpPost(param[0]);
                //будем передавать два параметра
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                //передаем параметры из наших текстбоксов
                //лоигн
                nameValuePairs.add(new BasicNameValuePair("login", login.getText().toString()));
                //пароль
                nameValuePairs.add(new BasicNameValuePair("pass", pass.getText().toString()));
                //собераем их вместе и посылаем на сервер
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //получаем ответ от сервера
                String response = hc.execute(postMethod, res);
                //посылаем на вторую активность полученные параметры
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                //то что куда мы будем передавать и что, putExtra(куда, что);
                intent.putExtra(SecondActivity.JsonURL, response);
                startActivity(intent);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            dialog.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Загружаюсь...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }
    }
}
