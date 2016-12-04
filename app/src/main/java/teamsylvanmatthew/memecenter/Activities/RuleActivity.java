package teamsylvanmatthew.memecenter.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import teamsylvanmatthew.memecenter.Database.MemeCenterDataSource;
import teamsylvanmatthew.memecenter.Models.Filter;
import teamsylvanmatthew.memecenter.Models.Rule;
import teamsylvanmatthew.memecenter.R;

public class RuleActivity extends AppCompatActivity {
    private MemeCenterDataSource dataSource;
    private EditText filterEditText;
    private EditText filterNameEditText;
    private SharedPreferences sharedPreferences;
    private Set<String> filterList;
    private Button saveButton;
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
        filterEditText = (EditText) findViewById(R.id.filterListEditText);

        saveButton = (Button) findViewById(R.id.add_save_rule);
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
            populateFields();
        }




    }

    public boolean saveChanges() {
        String newName = filterNameEditText.getText().toString();

        if (newName.equals("")) {
            return false;
        }
        //TODO: check if newName already exists

        if (id != -1 && name != null) {
            /* update */
            long currentId = dataSource.getFilterId(name);
            //TODO: remove this delete rules and to be more accurate.
            dataSource.deleteRules(currentId);
            dataSource.updateFilter(name, newName);


            String lines = filterEditText.getText().toString();
            ArrayList<String> listOfText = new ArrayList<String>(Arrays.asList(lines.split("\n")));

            Rule newRule;
            for( String line : listOfText ) {
                newRule = new Rule(currentId, line);
                dataSource.addRule(newRule);
            }
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
            return true;

        } else {
            /* insert */
            Filter newFilter = new Filter(newName);
            long id = dataSource.addFilter(newFilter);

            String lines = filterEditText.getText().toString();
            ArrayList<String> listOfText = new ArrayList<String>(Arrays.asList(lines.split("\n")));


            Rule newRule;
            for (String line : listOfText) {
                newRule = new Rule(id, line);
                dataSource.addRule(newRule);
            }

            Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public void populateFields() {
        setTitle("Rules for \"" + name + "\"");
        saveButton.setText("Save Changes");
        filterNameEditText.setText(name);

        TextView create = (TextView) findViewById(R.id.textView1);
        create.setText("Edit Filter");

        if (id != -1) {
            ArrayList<String> rules = dataSource.getRules(id);
            filterEditText.setText(android.text.TextUtils.join("\n", rules));
        } else {
            filterEditText.setText("");
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
