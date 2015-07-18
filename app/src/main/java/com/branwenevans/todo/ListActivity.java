package com.branwenevans.todo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class ListActivity extends Activity {

    public static final int REQUEST_CODE = 1;

    private LinearLayoutManager layoutManager;

    private TodoAdapter adapter;

    private RecyclerView recyclerView;

    private ArrayList<ListItem> dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        dataset = readFromFile();
        adapter = new TodoAdapter(dataset);
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String newLabel = data.getStringExtra(AddActivity.EXTRA_ADDED_ITEM);
            if (!newLabel.isEmpty()) {
                dataset.add(new ListItem(newLabel));
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        writeToFile();
    }


    public void addItem(View view) {
        Intent addItemIntent = new Intent(this, AddActivity.class);
        startActivityForResult(addItemIntent, REQUEST_CODE);
    }


    public void taskDone(View view) {
        int position = recyclerView.getChildPosition((View) view.getParent());
        ListItem listItem = dataset.get(position);
        listItem.done();
        adapter.notifyItemChanged(position);
    }

    public void taskDeleted(View view) {
        int position = recyclerView.getChildPosition((View) view.getParent());
        dataset.remove(position);
        adapter.notifyDataSetChanged();
    }

    private void writeToFile() {
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            for (ListItem item : dataset) {
                outputStream.write(item.getLabel().getBytes());
                outputStream.write('\n');
            }
            outputStream.close();
        } catch (IOException e) {
            //TODO: handle
        }
    }

    private ArrayList<ListItem> readFromFile() {
        ArrayList<ListItem> lines = new ArrayList<ListItem>();
        String line;
        try {
            FileReader reader = new FileReader(getFilePath());
            BufferedReader bufferedReader = new BufferedReader(reader);

            while ((line = bufferedReader.readLine()) != null) {
                lines.add(new ListItem(line));
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            //TODO: handle
        }
        return lines;
    }

    private String getFilePath() {
        return getBaseContext().getFilesDir() + "/" + getString(R.string.file_name);
    }

}
