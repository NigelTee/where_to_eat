package materialtest.example.user.wheretoeat;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class add_places extends AppCompatActivity {

    private EditText et_insertplace;
    private Database myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_places);

        et_insertplace = (EditText) findViewById(R.id.editText);
        openDB();
        populateListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Methods
    private void openDB() {
        myDb = new Database(this);
        myDb.open();
    }

    private void closeDB() {
        myDb.close();
    }

    //add button
    /*
       add inputs in DB and display
     */
    public void add(View view) {
        String place = et_insertplace.getText().toString();
        myDb.insertRow(place);
        populateListView();
    }

    //reset button
    /*
        clean DB and display
     */
    public void reset(View view) {
        myDb.deleteAll();
        populateListView();

    }

    private void populateListView() {
        Cursor cursor = myDb.getAllRows();
        String[] resolution_array = new String[]{myDb.KEY_NAME};
        int[] days_array = new int[]{R.id.tvName};
        SimpleCursorAdapter my_cursor_adapter;
        my_cursor_adapter = new SimpleCursorAdapter(getBaseContext(), R.layout.place_view, cursor, resolution_array, days_array, 0);
        ListView myList = (ListView) findViewById(R.id.lv_display);
        myList.setAdapter(my_cursor_adapter);

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                myDb.deleteRow(id);
                populateListView();
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }
}
