package waasi.waasi;

import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.R.attr.fragment;

/**
 * Created by Chrustkiran on 17/09/2017.
 */

public class OfferFragment extends Fragment {
    private RecyclerView rv;
    public DataSnapshot dataSnapshot;
    private LinearLayoutManager layoutManager;
    private DatabaseReference mData;
    private DatabaseReference mDatabaseRef;
    private ArrayList<Offer> mDataset;
    public OfferFragment offerFragment;
    private OAdapter oAdapter;
    ViewGroup container;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offer_layout,container);
        this.container = container;
       rv = (RecyclerView)container.findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(getContext());



        rv.setHasFixedSize(true);
        mDataset = new ArrayList<>();
        rv.setLayoutManager(layoutManager);
        mData = FirebaseDatabase.getInstance().getReference().child("offes");
        oAdapter = new OAdapter(Offer.class,R.layout.offer_list,OHolder.class,mData,getContext());
        rv.setAdapter(oAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }



}
