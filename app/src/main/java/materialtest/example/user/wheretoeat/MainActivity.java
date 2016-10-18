package materialtest.example.user.wheretoeat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    TextView tv_wheretoeat;
    String[] anArray;
    private static boolean start = true;
    Handler handler = new Handler();
    private Database myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_wheretoeat = (TextView) findViewById(R.id.tv_wheretoeat);
        myDb = new Database(this);
        myDb.open();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.add, menu);
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

        if (id == R.id.add_places) {
            Intent intent = new Intent(this, add_places.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void resume(View view) {
        //create array
        fillarray();
        if(anArray.length != 0) {
            if (start) {
//timer will start
                handler.postDelayed(updateTimer, 0);
                start = false;
            } else {
//timer will pause
                handler.removeCallbacks(updateTimer);
                start = true;
            }
        }else{
            Toast.makeText(getApplicationContext(), "Please Enter Places to Eat",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public Runnable updateTimer = new Runnable() {

        public void run() {
            int random = (int) (Math.random() * anArray.length);
            String food = anArray[random];
            tv_wheretoeat.setText(food);
            handler.postDelayed(this, 0);
        }

    };

    private void fillarray() {
        Cursor c = myDb.getAllRows();
        anArray = new String[c.getCount()];
        int i = 0;

        if (c.moveToFirst()) {

            do {
                String uname = c.getString(c.getColumnIndex("name"));
                anArray[i] = uname;
                i++;
            }
            while (c.moveToNext());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDb.close();
    }
}

