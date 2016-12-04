package teamsylvanmatthew.memecenter.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import teamsylvanmatthew.memecenter.Adapters.FilterAdapter;
import teamsylvanmatthew.memecenter.Database.MemeCenterDataSource;
import teamsylvanmatthew.memecenter.Models.Filter;
import teamsylvanmatthew.memecenter.R;

public class FilterActivity extends AppCompatActivity {
    private static final String TAG = "FilterActivity";
    private SharedPreferences sharedPreferences;
    private MemeCenterDataSource dataSource;
    private FilterAdapter filterAdapter;
    private ArrayList<Filter> filters;
    private ListView filterListView;
    private Context mContext;
    private AlertDialog.Builder alertDialogBuilder;
    private EditText userInput;
    private Set<String> listOfChecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mContext = getApplicationContext();

        sharedPreferences = getSharedPreferences("memecenter", Context.MODE_PRIVATE);
        dataSource = new MemeCenterDataSource(this);
        dataSource.open();

        listOfChecked = new HashSet<String>();
        listOfChecked = sharedPreferences.getStringSet("filterList", null);

        filters = dataSource.getAllFilters();
        filterAdapter = new FilterAdapter(this, listOfChecked, filters);

        filterListView = (ListView) findViewById(R.id.filterList);
        filterListView.setAdapter(filterAdapter);

        Button btn_save_sel = (Button) findViewById(R.id.save_selection);
        Button btn_add = (Button) findViewById(R.id.add_filter);

        btn_save_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                listOfChecked = new HashSet<String>(filterAdapter.getSelectedItems());
                editor.putStringSet("filterList", listOfChecked);
                editor.commit();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ruleIntent = new Intent(mContext, RuleActivity.class);
                startActivityForResult(ruleIntent, 1);
            }
        });

        //setSelection();
    }

    private void setSelection() {
        HashSet<String> listOfChecked = new HashSet<String>();
        sharedPreferences.getStringSet("filterList", listOfChecked);

        for (String position : listOfChecked) {
            filterListView.setItemChecked(Integer.parseInt(position), true);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    /*
                    filters.clear();
                    filters = dataSource.getAllFilters();
                    filterAdapter.notifyDataSetChanged();
                    */
                    //findViewById(R.id.activity_filter).invalidate();
                    break;
                default:
                    break;
            }
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
