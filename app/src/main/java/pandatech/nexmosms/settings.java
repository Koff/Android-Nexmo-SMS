package pandatech.nexmosms;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class settings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView api_key_settings = (TextView) findViewById(R.id.api_key_settings);
        TextView api_secret_settings = (TextView) findViewById(R.id.api_secret_settings);

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);

        api_key_settings.setText(settings.getString("api_key", ""));
        api_secret_settings.setText(settings.getString("api_secret", ""));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void save_settings(View view) {

        final TextView key_settings = (TextView) findViewById(R.id.api_key_settings);
        final TextView secret_settings = (TextView) findViewById(R.id.api_secret_settings);

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("api_key", key_settings.getText().toString());
        editor.putString("api_secret", secret_settings.getText().toString());
        editor.commit();
    }
}
