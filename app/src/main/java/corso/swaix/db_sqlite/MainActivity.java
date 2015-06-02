package corso.swaix.db_sqlite;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import corso.swaix.db_sqlite.models.User;
import corso.swaix.db_sqlite.utils.UsersDataSource;


public class MainActivity extends ListActivity {
    private UsersDataSource datasource;
    private EditText nome;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new UsersDataSource(this);
        datasource.open();

        List<User> values = datasource.getAllUsers();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

        nome = (EditText) findViewById(R.id.edit_text_nome);
    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<User> adapter = (ArrayAdapter<User>) getListAdapter();
        User comment = null;
        switch (view.getId()) {
            case R.id.add:
                if (TextUtils.isEmpty(nome.getText())) {
                    Toast.makeText(this, "nome vuoto", Toast.LENGTH_LONG).show();
                } else {

                    // save the new comment to the database
                    comment = datasource.createUser(nome.getText().toString());
                    nome.setText("");
                    adapter.add(comment);
                }
                break;
            case R.id.delete:
                if (getListAdapter().getCount() > 0) {
                    comment = (User) getListAdapter().getItem(0);
                    datasource.deleteUser(comment);
                    adapter.remove(comment);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

} 
