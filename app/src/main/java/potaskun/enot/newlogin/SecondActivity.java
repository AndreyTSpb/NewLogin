package potaskun.enot.newlogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SecondActivity extends AppCompatActivity {

    public HashMap<String, Object> hm;
    public static String JsonURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //принимаем параметр который мы послылали в manActivity
        Bundle extras = getIntent().getExtras();
        //превращаем в тип стринг для парсинга
        assert extras != null;
        String json = extras.getString(JsonURL);
        //передаем в метод парсинга
        if(!JSONURL(json)){
            String error = "Произошла ошибка";
            Intent intent = new Intent(SecondActivity.this, MainActivity.class);
            intent.putExtra("error", error);
            System.out.println("test-error"+error);
            startActivity(intent);
        }
        System.out.println("test-id"+Global.ID_TEACH);
        TextView idTeach   = findViewById(R.id.idTeach);
        idTeach.setText(""+Global.ID_TEACH);
        TextView nameTeach = findViewById(R.id.nameTeach);
        nameTeach.setText(Global.NAME_TEACH);
        TextView hesh = findViewById(R.id.hashKey);
        hesh.setText(Global.HESH_KEY);
    }

    /** @param result */
    public boolean JSONURL(String result){
        try{
            System.out.println("json_test"+ result);
            //создали читателя json объектов и отдали ему строку - result
            JSONObject json  = new JSONObject(result);
            //дальше находим вход в наш json им является ключевое слово data
            JSONArray urls = json.getJSONArray("data");
            System.out.println("test-g"+urls);
            /*Проверяем есть ли данные*/
            System.out.println("test-mass"+urls.getJSONObject(0).getString("error"));
            if(urls.getJSONObject(0).getString("error").equals("FALSE")) {

                Global.ID_TEACH   = urls.getJSONObject(1).getInt("idTeach");
                Global.NAME_TEACH = urls.getJSONObject(1).getString("nameTeach");
                Global.HESH_KEY   = urls.getJSONObject(1).getString("heshKey");

                return true;
            }else{
                System.out.print("test-err"+urls.getJSONObject(1).getString("errorText"));
                String error = urls.getJSONObject(1).getString("errorText");
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                intent.putExtra("error", error);
                System.out.println("test-error"+error);
                startActivity(intent);
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return false;
    }
}
