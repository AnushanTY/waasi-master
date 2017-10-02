package waasi.waasi;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by Chrustkiran on 26/09/2017.
 */

public class OAdapter extends FirebaseRecyclerAdapter<Offer,OHolder>  {

        DatabaseReference mData;
        Offer offer;
       private Context context;
    public OAdapter(Class<Offer> modelClass, int modelLayout, Class<OHolder> viewHolderClass, DatabaseReference ref,Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
        this.mData = ref;

    }

    @Override
    protected void populateViewHolder(final OHolder viewHolder, final Offer model, int position) {
        this.offer = model;
        final Offer new_offer =getItem(position);

        final DatabaseReference key = getRef(position).child("like");
        viewHolder.mDesc.setText(model.getDesc());
        viewHolder.mName.setText(model.getName());
        Picasso.with(context).load(model.getImage()).into(viewHolder.mImageView);
        //viewHolder.mLike.
        viewHolder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CompanyActivity.class);
                intent.putExtra("company_name",new_offer.getName());
                context.startActivity(intent);
            }
        });
        viewHolder.liketxt.setText(model.getLike().toString());
        viewHolder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, new_offer.getName()+"\n"+new_offer.getDesc());
                context.startActivity(Intent.createChooser(intent, "Share"));
            }
        });

        viewHolder.mLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    key.setValue(new_offer.getLike() + 1);




            }
        });

    }}
