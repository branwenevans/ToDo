package com.branwenevans.todo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ListActivity extends Activity {

    public static final int REQUEST_CODE = 1;

    private TodoAdapter adapter;

    private RecyclerView recyclerView;

    private boolean showCompletedTasks = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TodoAdapter(readFromFile());
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String newLabel = data.getStringExtra(AddActivity.EXTRA_ADDED_ITEM);
            if (!newLabel.isEmpty()) {
                adapter.addItem(new ListItem(newLabel));
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        writeToFile();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hide_done:
                toggleShowCompletedTasks(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addItem(View view) {
        Intent addItemIntent = new Intent(this, AddActivity.class);
        startActivityForResult(addItemIntent, REQUEST_CODE);
    }


    public void taskDone(View view) {
        adapter.taskDone(recyclerView.getChildPosition((View) view.getParent()));
    }


    public void taskDeleted(View view) {
        adapter.taskDeleted(recyclerView.getChildPosition((View) view.getParent()));
    }


    private void toggleShowCompletedTasks(MenuItem item) {
        showCompletedTasks = !showCompletedTasks;
        item.setIcon(showCompletedTasks ? R.drawable.ic_action_visibility_off : R.drawable.ic_action_visibility);
        adapter.toggleShowCompletedTasks();
    }


    private void writeToFile() {
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            for (ListItem item : adapter.getAllItems()) {
                outputStream.write(item.getLabel().getBytes());
                outputStream.write(getString(R.string.doneSeparator).getBytes());
                outputStream.write(item.isDone() ? getString(R.string.done).getBytes() : getString(R.string.todo).getBytes());
                outputStream.write('\n');
            }
            outputStream.close();
        } catch (IOException e) {
            //TODO: handle
        }
    }


    private ArrayList<ListItem> readFromFile() {
        ArrayList<ListItem> lines = new ArrayList<>();
        String line;
        try {
            FileReader reader = new FileReader(getFilePath());
            BufferedReader bufferedReader = new BufferedReader(reader);

            while ((line = bufferedReader.readLine()) != null) {
                int separator = line.lastIndexOf(getString(R.string.doneSeparator));
                String done = line.substring(separator + 1);
                lines.add(new ListItem(line.substring(0, separator), getString(R.string.done).equals(done)));
            }
        } catch (IOException e) {
            //TODO: handle
        }
        return lines;
    }


    private String getFilePath() {
        return getBaseContext().getFilesDir() + "/" + getString(R.string.file_name);
    }

}
