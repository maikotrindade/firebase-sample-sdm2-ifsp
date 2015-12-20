package br.edu.ifspsaocarlos.agenda.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.adapter.ContatoFirebaseAdapter;
import br.edu.ifspsaocarlos.agenda.util.Constants;

public class BaseActivity extends AppCompatActivity {

    public ListView mListView;
    protected SearchView mSearchView;
    Firebase myFirebaseRef;
    ContatoFirebaseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2,
                                    long arg3) {

                Intent inte = new Intent(getApplicationContext(), DetailActivity.class);

                inte.putExtra("FirebaseID", mAdapter.getRef(arg2).getKey());
                startActivityForResult(inte, 0);
            }
        });


        registerForContextMenu(mListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.searchContact).getActionView();

        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        mSearchView.setIconifiedByDefault(true);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater m = getMenuInflater();
        m.inflate(R.menu.menu_context, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ContatoFirebaseAdapter adapter = (ContatoFirebaseAdapter) mListView.getAdapter();
        String FirebaseID = adapter.getRef(info.position).getKey().toString();


        switch (item.getItemId()) {
            case R.id.delete_item:
                myFirebaseRef.child(FirebaseID).removeValue();
                Toast.makeText(getApplicationContext(), "Removido com sucesso", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    protected void buildListView() {
        mAdapter = new ContatoFirebaseAdapter(this, myFirebaseRef);
        mListView.setAdapter(mAdapter);
    }

    protected void buildSearchListView(String query) {
        Query queryRef = myFirebaseRef.orderByChild("nome").equalTo(query);
        mAdapter = new ContatoFirebaseAdapter(this, queryRef);
        mListView.setAdapter(mAdapter);
    }

}
