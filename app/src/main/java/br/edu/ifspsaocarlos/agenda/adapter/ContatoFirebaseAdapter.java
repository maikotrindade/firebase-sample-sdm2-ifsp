package br.edu.ifspsaocarlos.agenda.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.model.Contact;

public class ContatoFirebaseAdapter extends FirebaseListAdapter<Contact> {
    static final Class<Contact> modelClass = Contact.class;
    static final int modelLayout = R.layout.contact_item;

    public ContatoFirebaseAdapter(Activity activity, Firebase ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    public ContatoFirebaseAdapter(Activity activity, Query ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    @Override
    protected void populateView(View view, Contact contato, int position) {
        ((TextView) view.findViewById(R.id.name)).setText(contato.getNome());
        ((TextView) view.findViewById(R.id.phone)).setText(contato.getFone());
        ((TextView) view.findViewById(R.id.email)).setText(contato.getEmail());
        ((TextView) view.findViewById(R.id.address)).setText(contato.getEndereco());
    }
}
