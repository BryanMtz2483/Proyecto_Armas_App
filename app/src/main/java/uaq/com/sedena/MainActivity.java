package uaq.com.sedena;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public EditText email, password;

    public Button login;

    public String usuario, contrasena;

    private static final String PREFERENCES_FILE = "UserPrefs";

    private static final String TOKEN_KEY = "token";

    public String url = "http://10.0.2.2:8000/api/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initElements();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void initElements(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.LOGIN);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = String.valueOf(email.getText());
                contrasena = String.valueOf(password.getText());
                //función de ws -> post login
                wsLogin();
            }
        });
    }
    public void wsLogin(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email",usuario);
            jsonObject.put("password",contrasena);
        }catch (JSONException js){js.printStackTrace();}

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response->{
                    try {
                        System.out.println(response.toString());
                        String token = response.getString("access_token");
                        //Almacenar en las variables de entorno de la aplication
                        System.out.println(token);
                    }catch (JSONException je){je.printStackTrace();}
                }, error -> {
            System.out.println("Error"+ error.toString());
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
}