package teamsylvanmatthew.memecenter.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Set;

import teamsylvanmatthew.memecenter.Database.MemeCenterDataSource;
import teamsylvanmatthew.memecenter.Models.Filter;
import teamsylvanmatthew.memecenter.R;

public class RuleActivity extends AppCompatActivity {
    private MemeCenterDataSource dataSource;
    private EditText filterEditText;
    private EditText filterNameEditText;
    private SharedPreferences sharedPreferences;
    private Set<String> filterList;
    private long id;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);

        Intent intent = getIntent();
        id = intent.getLongExtra("id", -1);
        name = intent.getStringExtra("name");

        dataSource = new MemeCenterDataSource(this);
        dataSource.open();

        filterNameEditText = (EditText) findViewById(R.id.filterNameEditText);
        Button saveButton = (Button) findViewById(R.id.add_save_rule);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = saveChanges();

                if (check) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });



        if (id == -1 || name == null) {
            setTitle("Add New Filter");

            return;
        } else {
            setTitle("Rules for \"" + name + "\"");

            populateFields();

            saveButton.setText("Save Changes");
            filterNameEditText.setText(name);
        }



    }

    public boolean saveChanges() {
        String name = filterNameEditText.getText().toString();

        if (name.equals("")) {
            return false;
        } else {
            Filter newFilter = new Filter(name);
            long id = dataSource.addFilter(newFilter);
/*
            String lines = filterEditText.getText().toString();
            ArrayList<String> listOfText = new ArrayList<String>(Arrays.asList(lines.split("\n")));

            for( String line : listOfText ) {
                dataSource.addRule();
            }
            */
            return true;
        }
    }

    public void populateFields() {

        TextView create = (TextView) findViewById(R.id.textView1);
        create.setText("Edit Filter");

        filterEditText = (EditText) findViewById(R.id.filterListEditText);

        sharedPreferences = getSharedPreferences("memecenter", Context.MODE_PRIVATE);

        filterList = sharedPreferences.getStringSet("filterList", null);
        if (filterList != null) {
            String textToFillEditText = "";
            for (String aString : filterList) {
                textToFillEditText += aString + "\n";
            }

            filterEditText.setText(textToFillEditText);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
