package pandatech.nexmosms;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, settings.class);
            this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void send_message(View view) {

        final TextView originator = (TextView) findViewById(R.id.originator_id);
        final TextView destination = (TextView) findViewById(R.id.destination_id);
        final TextView content = (TextView) findViewById(R.id.msg_content);
        final TextView response_view = (TextView) findViewById(R.id.response_body);

        String url = "http://rest.nexmo.com/sms/json";
        HashMap<String, String> params = new HashMap<String, String>();
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);

        params.put("api_key", settings.getString("api_key", ""));
        params.put("api_secret", settings.getString("api_secret",""));
        params.put("from", originator.getText().toString());
        params.put("to", destination.getText().toString());
        params.put("text", content.getText().toString());

        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int total_messages = response.getInt("message-count");
                            System.out.println("total number of messages retrieved: " + total_messages);
                            JSONArray messages = response.getJSONArray("messages");
                            String messages_status[] = new String[20];
                            String messages_id[] = new String[20];
                            String messages_to[] = new String[20];
                            String messages_remaining_balance[] = new String[20];
                            String messages_price[] = new String[20];
                            String messages_network[] = new String[20];

                            for (int i = 0; i < messages.length(); i++) {
                                JSONObject current_message = messages.getJSONObject(i);
                                messages_status[i] = current_message.get("status").toString();
                                messages_id[i] = current_message.get("message-id").toString();
                                messages_to[i] = current_message.get("to").toString();
                                messages_remaining_balance[i] = current_message.get("remaining-balance").toString();
                                messages_price[i] = current_message.get("message-price").toString();
                                messages_network[i] = current_message.get("network").toString();
                            }

                            for (int i=0; i<total_messages; i++){
                                System.out.println("Message " + i + " Message Status " + messages_status[i] + " Message Id " + messages_id[i]);
                                response_view.append("Message: " + i + "\n" + "Message Status: " + messages_status[i] + "\n" + "Message Id: " + messages_id[i] + "\n" + "To: " + messages_to[i] + "\n" + "Remaining Balance: " + messages_remaining_balance[i] + "\n" + "To: " + messages_to[i] + "\n" + "Price: " + messages_price[i] + "\n" + "Network: " + messages_network[i] + "\n");
                                // TODO instead of 10, we should use response_view.getWidth()
                                for (int j=0; j<10; j++)
                                {
                                    response_view.append("-");
                                }
                                response_view.append("\n");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

    }
}