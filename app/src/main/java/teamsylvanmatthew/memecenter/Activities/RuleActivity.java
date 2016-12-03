package teamsylvanmatthew.memecenter.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import teamsylvanmatthew.memecenter.R;

public class RuleActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    EditText filterEditText;
    private SharedPreferences sharedPreferences;
    private Set<String> filterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);


        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFilterList();

                //TODO: Redirect user from Filter screen to where ever Sylvan wants
            }
        });


        populateEditTextField();


    }

    public void saveFilterList() {
        sharedPreferences = getSharedPreferences("memecenter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //parser
        filterEditText = (EditText) findViewById(R.id.filterListEditText);

        String filterLinesOfText = filterEditText.getText().toString();
        System.out.println("The Strings :" + filterLinesOfText);
        String delimiter = "\n";

        List<String> listOfText = new ArrayList<String>(Arrays.asList(filterLinesOfText.split(delimiter)));


        //saving preferences
        filterList = new HashSet<String>(listOfText);
        editor.putStringSet("filterList", filterList);
        editor.commit();


        //Toast feedback
        Context context = getApplicationContext();
        CharSequence text = "List of filters saved";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void populateEditTextField() {

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
