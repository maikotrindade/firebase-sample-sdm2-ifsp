package br.edu.ifspsaocarlos.agenda.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.model.Contact;
import br.edu.ifspsaocarlos.agenda.util.Constants;

public class DetailActivity extends AppCompatActivity {
    private Contact mContact;
    String FirebaseID;
    Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Firebase.setAndroidContext(this);

        myFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().hasExtra("FirebaseID")) {

            FirebaseID = getIntent().getStringExtra("FirebaseID");
            Firebase refContato = myFirebaseRef.child(FirebaseID);


            ValueEventListener refContatoListener = refContato.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    mContact = snapshot.getValue(Contact.class);

                    if (mContact != null) {
                        EditText nameText = (EditText) findViewById(R.id.editText1);
                        nameText.setText(mContact.getNome());
                        EditText foneText = (EditText) findViewById(R.id.editText2);
                        foneText.setText(mContact.getFone());
                        EditText emailText = (EditText) findViewById(R.id.editText3);
                        emailText.setText(mContact.getEmail());
                        EditText addressText = (EditText) findViewById(R.id.editText4);
                        addressText.setText(mContact.getEndereco());
                        EditText dataNascText = (EditText) findViewById(R.id.editText5);
                        dataNascText.setText(mContact.getDataNascimento());
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e("LOG", firebaseError.getMessage());
                }

            });


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        if (FirebaseID == null) {
            MenuItem item = menu.findItem(R.id.deleteContact);
            item.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveContact:
                salvar();
                return true;
            case R.id.deleteContact:
                myFirebaseRef.child(FirebaseID).removeValue();
                Toast.makeText(getApplicationContext(), "Contato removido", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void salvar() {
        String name = ((EditText) findViewById(R.id.editText1)).getText().toString();
        String fone = ((EditText) findViewById(R.id.editText2)).getText().toString();
        String email = ((EditText) findViewById(R.id.editText3)).getText().toString();
        String address = ((EditText) findViewById(R.id.editText4)).getText().toString();
        String birthday = ((EditText) findViewById(R.id.editText5)).getText().toString();

        if (mContact == null) {
            mContact = new Contact();
            mContact.setNome(name);
            mContact.setFone(fone);
            mContact.setEmail(email);
            mContact.setEndereco(address);
            mContact.setDataNascimento(birthday);

            myFirebaseRef.push().setValue(mContact);
            Toast.makeText(this, "Incluído com sucesso", Toast.LENGTH_SHORT).show();
        } else {

            mContact.setNome(name);
            mContact.setFone(fone);
            mContact.setEmail(email);
            mContact.setEndereco(address);
            mContact.setDataNascimento(birthday);

            myFirebaseRef.child(FirebaseID).setValue(mContact);

            Toast.makeText(this, "Alterado com sucesso", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

}
