package waasi.waasi;

import android.content.Intent;
import android.media.MediaPlayer;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private ImageButton unloackBtn;
    private DatabaseReference databaseCodes;
    private DatabaseReference databaseUsers;
    final private String userId = "mGwWWtJUCCTpCOVPlokMeE1Ccdw1";
    private User user;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offer_layout,container);
        this.container = container;
        rv = (RecyclerView)container.findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(getContext());
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseCodes = FirebaseDatabase.getInstance().getReference().child("codes");

        unloackBtn = (ImageButton)view.findViewById(R.id.unlockBtn);
        unloackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    unlockOrInvite();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String dsnapshot = dataSnapshot1.getKey();
                    if (dataSnapshot1.getKey().toString() != userId) {


                        user = dataSnapshot1.getValue(User.class);
                        String x = user.getName();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        rv.setHasFixedSize(true);
        mDataset = new ArrayList<>();
        rv.setLayoutManager(layoutManager);
        mData = FirebaseDatabase.getInstance().getReference().child("offers");
        oAdapter = new OAdapter(Offer.class,R.layout.offer_list,OHolder.class,mData,getContext());
        rv.setAdapter(oAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    private void unlockOrInvite() throws ParseException {
        if(isUnlock()){
            unlockActivity();
        }
      else {
            inviteActivity();
        }

    }

    private void inviteActivity() {
        Intent inviteIntent = new Intent(getContext(),InviteActivity.class);
        inviteIntent.putExtra("code",user.getCode());
        getActivity().startActivity(inviteIntent);

    }

    private void unlockActivity() {
        Intent unlockIntent = new Intent(getContext(),UnlockActivity.class);
        getActivity().startActivity(unlockIntent);
    }

    private boolean isUnlock() throws ParseException {
       boolean isUnlck = false;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        Date date1 = dateFormat.parse(user.getLastdate());


        //int days = 1+(int)( (date1.getTime() - date.getTime()) / (1000 * 60 * 60 * 24));
        int days = 0;
        if(user.getInvited()>=2){
        if(((user.getInvited()*7)/2>days)){
            isUnlck = true;

        }}
       return isUnlck;

    }


}
