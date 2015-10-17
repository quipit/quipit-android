package it.quip.android.actvitiy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseInstallation;

import it.quip.android.R;

public class QuipitHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quipit_home);
        Parse.initialize(this, "djo3QZBPwqvrO9H7FPUvU0Q6S6j3I6HB1YEZYuh1", "CYgHSQKeDfLFu0i9eT7Ks2e8DPdgwTdOFEX2BLP8");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_quipit_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
