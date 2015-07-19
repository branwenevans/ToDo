package com.branwenevans.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class AddActivity extends Activity {

    public static final String EXTRA_ADDED_ITEM = "com.branwenevans.todo.ADDED_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        String message = ((EditText) findViewById(R.id.edit_message)).getText().toString();
        intent.putExtra(EXTRA_ADDED_ITEM, message);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
