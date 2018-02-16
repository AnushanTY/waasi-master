package waasi.waasi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Chrustkiran on 26/09/2017.
 */

public class OAdapter extends FirebaseRecyclerAdapter<Offer,OHolder>  {

        int CODE;
        DatabaseReference mData;
        String dateSelected;
        DatabaseReference databaseReference;
        Offer offer;
        RecyclerView recyclerView;
        private String company_name;
       private Context context;
      private ArrayList<String> followingList;
       int modelLayout;



    public OAdapter(Class<Offer> modelClass, int modelLayout, Class<OHolder> viewHolderClass, DatabaseReference ref, Context context, int CODE, RecyclerView recyclerView) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;

        this.mData = ref;
        this.modelLayout = modelLayout;
        this.CODE = CODE;
        this.recyclerView = recyclerView;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        followingList = new ArrayList<>();

    }



    public void setDateSelected(String dateSelected) {
        this.dateSelected = dateSelected;
    }

    @Override
    protected void populateViewHolder(final OHolder viewHolder, final Offer model, final int position) {

        if(CODE==0){ this.offer = model;
            checkInterested();
        final Offer new_offer =getItem(position);

            Log.d("ref",getRef(position).getKey().toString());

        final DatabaseReference key = getRef(position).child("like");

        setLikeButton(viewHolder.mLike,getRef(position).getKey().toString());
        setSavedButton(viewHolder.bookmark,getRef(position).getKey());
        viewHolder.mDesc.setText(model.getDesc());
        viewHolder.mName.setText(model.getName());
        Picasso.with(context).load(model.getImage()).into(viewHolder.mImageView);
        viewHolder.startDate.setText(model.getStartDate());
        viewHolder.endDate.setText(model.getEndDate());
        //viewHolder.mLike.
        viewHolder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Company.class);
                intent.putExtra("title",new_offer.getName());
                context.startActivity(intent);
            }
        });
        if(MainActivity.followingList_Main.contains(viewHolder.mName.getText().toString())){
            viewHolder.interest.setClickable(false);
            viewHolder.interest.setAlpha((float) 0.2);
        }
        else{
            viewHolder.interest.setClickable(true);
            viewHolder.interest.setAlpha((float) 1);
        }
        viewHolder.liketxt.setText(model.getLike().toString());
        viewHolder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, new_offer.getName()+"\n"+new_offer.getDesc());*/
                Picasso.with(context).load(model.getImage()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_TEXT, new_offer.getName()+"\n"+new_offer.getDesc()+"\n"+"Available: from "+new_offer.getStartDate()+" to "+new_offer.getEndDate());
                        intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                        context.startActivity(Intent.createChooser(intent, "Share"));
                    }

                    @Override
                    public void onBitmapFailed(Drawable drawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable drawable) {

                    }
                });

            }
        });
        viewHolder.interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interesting(viewHolder.interest,viewHolder.mName);
            }
        });

       viewHolder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(MainActivity.savedList.contains(getRef(position).getKey().toString())){


                    while (MainActivity.savedList.contains(getRef(position).getKey())){
                        MainActivity.savedList.remove(getRef(position).getKey().toString());
                    }

                    FirebaseDatabase.getInstance().getReference().child("saved").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).removeValue();
                    viewHolder.bookmark.setImageResource(R.drawable.ic_unbookmark);

                }
                else {

                    MainActivity.savedList.add(getRef(position).getKey().toString());
                    FirebaseDatabase.getInstance().getReference().child("saved").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).setValue(getRef(position).getKey().toString());
                    viewHolder.bookmark.setImageResource(R.drawable.ic_bookmark_black_24dp);
                }



            }
        });


        viewHolder.mLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        if(MainActivity.likedList.contains(getRef(position).getKey().toString())){


                            while (MainActivity.likedList.contains(getRef(position).getKey())){
                                MainActivity.likedList.remove(getRef(position).getKey().toString());
                            }

                            key.setValue(new_offer.getLike() - 1);
                            FirebaseDatabase.getInstance().getReference().child("like").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).removeValue();


                        }
                        else {
                            key.setValue(new_offer.getLike() + 1);
                            MainActivity.likedList.add(getRef(position).getKey().toString());
                            FirebaseDatabase.getInstance().getReference().child("like").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).setValue(getRef(position).getKey().toString());


                        }





            }
        });}
        if(CODE == 1){
            today(viewHolder,model,position);
            if(MainActivity.followingList_Main.contains(viewHolder.mNameToday.getText().toString())){
                viewHolder.interestToday.setClickable(false);
                viewHolder.interestToday.setAlpha((float) 0.2);
            }
            else{
                viewHolder.interestToday.setClickable(true);
                viewHolder.interestToday.setAlpha((float) 1);
            }
        }
        if(CODE==2){
            calendar(viewHolder,model,position);
            if(MainActivity.followingList_Main.contains(viewHolder.mNameCalendar.getText().toString())){
                viewHolder.interestCalendar.setClickable(false);
                viewHolder.interestCalendar.setAlpha((float) 0.2);
            }
            else{
                viewHolder.interestCalendar.setClickable(true);
                viewHolder.interestCalendar.setAlpha((float) 1);
            }
        }
        if(CODE == 3){
            company(viewHolder,model,position);
            if(MainActivity.followingList_Main.contains(viewHolder.mNameCompany.getText().toString())){
                viewHolder.interestCompany.setClickable(false);
                viewHolder.interestCompany.setAlpha((float) 0.2);
            }
            else{
                viewHolder.interestCompany.setClickable(true);
                viewHolder.interestCompany.setAlpha((float) 1);
            }
        }
        if(CODE == 4){
            bookmark(viewHolder,model,position);
            if(MainActivity.followingList_Main.contains(viewHolder.mNameBookmark.getText().toString())){
                viewHolder.interestBookmark.setClickable(false);
                viewHolder.interestBookmark.setAlpha((float) 0.2);
            }
            else{
                viewHolder.interestBookmark.setClickable(true);
                viewHolder.interestBookmark.setAlpha((float) 1);
            }
        }


    }




     public void setCompany_name(String company_name){
        this.company_name = company_name;
     }


    private void calendar(final OHolder oHolder, final Offer model, final int position){
        String endDateString = model.getEndDate();
        String startDateString = model.getStartDate();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");


        Date todayDate = null;
        try {
            todayDate = formatter.parse(dateSelected);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDate = null;
        try {
            endDate = formatter.parse(endDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date startDate = null;
        try {
            startDate = formatter.parse(startDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final LinearLayout.LayoutParams params;
        if(todayDate.compareTo(endDate)<=0 && startDate.compareTo(todayDate) <=0 ){

            final Offer new_offer =getItem(position);

            final DatabaseReference key = getRef(position).child("like");
            oHolder.mDescCalendar.setText(model.getDesc());
            setLikeButton(oHolder.mLikeCalendar,getRef(position).getKey());
            oHolder.mNameCalendar.setText(model.getName());
            Picasso.with(context).load(model.getImage()).into(oHolder.mImageViewCalendar);
            oHolder.startDateCalendar.setText(model.getStartDate());
            oHolder.endDateCalendar.setText(model.getEndDate());
            //viewHolder.mLike.
            oHolder.mNameCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,Company.class);
                    intent.putExtra("title",new_offer.getName());
                    context.startActivity(intent);
                }
            });
            oHolder.liketxtCalendar.setText(model.getLike().toString());
            oHolder.mShareCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Picasso.with(context).load(model.getImage()).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_TEXT, new_offer.getName()+"\n"+new_offer.getDesc()+"\n"+"Available: from "+new_offer.getStartDate()+" to "+new_offer.getEndDate());
                            intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                            context.startActivity(Intent.createChooser(intent, "Share"));
                        }

                        @Override
                        public void onBitmapFailed(Drawable drawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable drawable) {

                        }
                    });
                }
            });
            oHolder.interestCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interesting(oHolder.interestCalendar,oHolder.mNameCalendar);
                }
            });

            oHolder.bookmarkCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(MainActivity.savedList.contains(getRef(position).getKey().toString())){


                        while (MainActivity.savedList.contains(getRef(position).getKey())){
                            MainActivity.savedList.remove(getRef(position).getKey().toString());
                        }

                        FirebaseDatabase.getInstance().getReference().child("saved").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).removeValue();
                        oHolder.bookmarkCalendar.setImageResource(R.drawable.ic_unbookmark);

                    }
                    else {

                        MainActivity.savedList.add(getRef(position).getKey().toString());
                        FirebaseDatabase.getInstance().getReference().child("saved").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).setValue(getRef(position).getKey().toString());
                        oHolder.bookmarkCalendar.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    }



                }
            });

            oHolder.mLikeCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(oHolder.mLikeCalendar.getTag().equals("liked")){
                        key.setValue(new_offer.getLike() - 1);
                        oHolder.mLikeCalendar.setTag("unliked");
                        oHolder.mLikeCalendar.setImageResource(R.drawable.ic_unfavourite_black);

                        FirebaseDatabase.getInstance().getReference().child("like").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).removeValue();
                        while (MainActivity.likedList.contains(getRef(position).getKey())){
                            MainActivity.likedList.remove(getRef(position).getKey().toString());
                        }

                    }
                    else {
                        key.setValue(new_offer.getLike() + 1);
                        FirebaseDatabase.getInstance().getReference().child("like").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).setValue(getRef(position).getKey().toString());
                        MainActivity.likedList.add(getRef(position).getKey().toString());
                        oHolder.mLikeCalendar.setImageResource(R.drawable.ic_favorite_black_24dp);
                        oHolder.mLikeCalendar.setTag("liked");

                    }




                }
            });
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        }
        else{

           params = new LinearLayout.LayoutParams(0,0);

        }
        oHolder.v.setLayoutParams(params);


    }




private void today(final OHolder oHolder, final Offer model , final int position)  {
    String endDateString = model.getEndDate();
    String startDateString = model.getStartDate();
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    Date today = new Date();
    setLikeButton(oHolder.mLikeToday,getRef(position).getKey());
    Date todayDate = null;
    try {
        todayDate = formatter.parse(formatter.format(today));
    } catch (ParseException e) {
        e.printStackTrace();
    }
    Date endDate = null;
    try {
        endDate = formatter.parse(endDateString);
    } catch (ParseException e) {
        e.printStackTrace();
    }
    Date startDate = null;
    try {
        startDate = formatter.parse(startDateString);
    } catch (ParseException e) {
        e.printStackTrace();
    }
    final LinearLayout.LayoutParams params;
    if(todayDate.compareTo(endDate)<=0 && startDate.compareTo(todayDate) <=0 ){

        final Offer new_offer =getItem(position);

        final DatabaseReference key = getRef(position).child("like");
        oHolder.mDescToday.setText(model.getDesc());
        oHolder.mNameToday.setText(model.getName());
        Picasso.with(context).load(model.getImage()).into(oHolder.mImageViewToday);
        oHolder.startDateToday.setText(model.getStartDate());
        oHolder.endDateToday.setText(model.getEndDate());
        //viewHolder.mLike.
        oHolder.mNameToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Company.class);
                intent.putExtra("title",new_offer.getName());
                context.startActivity(intent);
            }
        });
        oHolder.liketxtToday.setText(model.getLike().toString());
        oHolder.mShareToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(context).load(model.getImage()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_TEXT, new_offer.getName()+"\n"+new_offer.getDesc()+"\n"+"Available: from "+new_offer.getStartDate()+" to "+new_offer.getEndDate());
                        intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                        context.startActivity(Intent.createChooser(intent, "Share"));
                    }

                    @Override
                    public void onBitmapFailed(Drawable drawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable drawable) {

                    }
                });











            }
        });
        oHolder.interestToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interesting(oHolder.interestToday,oHolder.mNameToday);

            }
        });
        oHolder.bookmarkToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(MainActivity.savedList.contains(getRef(position).getKey().toString())){


                    while (MainActivity.savedList.contains(getRef(position).getKey())){
                        MainActivity.savedList.remove(getRef(position).getKey().toString());
                    }

                    FirebaseDatabase.getInstance().getReference().child("saved").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).removeValue();
                    oHolder.bookmarkToday.setImageResource(R.drawable.ic_unbookmark);

                }
                else {

                    MainActivity.savedList.add(getRef(position).getKey().toString());
                    FirebaseDatabase.getInstance().getReference().child("saved").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).setValue(getRef(position).getKey().toString());
                    oHolder.bookmarkToday.setImageResource(R.drawable.ic_bookmark_black_24dp);
                }



            }
        });

        oHolder.mLikeToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(oHolder.mLikeToday.getTag().equals("liked")){
                    key.setValue(new_offer.getLike() - 1);
                    oHolder.mLikeToday.setTag("unliked");
                    oHolder.mLikeToday.setImageResource(R.drawable.ic_unfavourite_black);

                    FirebaseDatabase.getInstance().getReference().child("like").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).removeValue();
                    while (MainActivity.likedList.contains(getRef(position).getKey())){
                        MainActivity.likedList.remove(getRef(position).getKey().toString());
                    }

                }
                else {
                    key.setValue(new_offer.getLike() + 1);
                    FirebaseDatabase.getInstance().getReference().child("like").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).setValue(getRef(position).getKey().toString());
                    MainActivity.likedList.add(getRef(position).getKey().toString());
                    oHolder.mLikeToday.setImageResource(R.drawable.ic_favorite_black_24dp);
                    oHolder.mLikeToday.setTag("liked");

                }




            }
        });
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
    }
    else{

        params = new LinearLayout.LayoutParams(0,0);

    }
    oHolder.v.setLayoutParams(params);


}

private void company(final OHolder viewHolder, final Offer model , final int position){
    final LinearLayout.LayoutParams params;
        if(company_name.equals(model.getName())){

            viewHolder.v.setVisibility(View.VISIBLE);
    //this.offer = model;
    final Offer new_offer =getItem(position);


    setLikeButton(viewHolder.mLikeCompany,getRef(position).getKey());

    final DatabaseReference key = getRef(position).child("like");
    viewHolder.mDescCompany.setText(model.getDesc());
    viewHolder.mNameCompany.setText(model.getName());
    Picasso.with(context).load(model.getImage()).into(viewHolder.mImageViewCompany);
    viewHolder.startDateComapny.setText(model.getStartDate());
    viewHolder.endDateCompany.setText(model.getEndDate());
    //viewHolder.mLike.
    viewHolder.mNameCompany.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,Company.class);
            intent.putExtra("title",new_offer.getName());
            context.startActivity(intent);
        }
    });

    viewHolder.liketxtCompany.setText(model.getLike().toString());
    viewHolder.mShareCompany.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Picasso.with(context).load(model.getImage()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_TEXT, new_offer.getName()+"\n"+new_offer.getDesc()+"\n"+"Available: from "+new_offer.getStartDate()+" to "+new_offer.getEndDate());
                    intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                    context.startActivity(Intent.createChooser(intent, "Share"));
                }

                @Override
                public void onBitmapFailed(Drawable drawable) {

                }

                @Override
                public void onPrepareLoad(Drawable drawable) {

                }
            });





        }
    });
    viewHolder.interestCompany.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            interesting(viewHolder.interestCompany,viewHolder.mNameCompany);
        }
    });
            viewHolder.bookmarkCompany.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(MainActivity.savedList.contains(getRef(position).getKey().toString())){


                        while (MainActivity.savedList.contains(getRef(position).getKey())){
                            MainActivity.savedList.remove(getRef(position).getKey().toString());
                        }

                        FirebaseDatabase.getInstance().getReference().child("saved").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).removeValue();
                        viewHolder.bookmarkCompany.setImageResource(R.drawable.ic_unbookmark);

                    }
                    else {

                        MainActivity.savedList.add(getRef(position).getKey().toString());
                        FirebaseDatabase.getInstance().getReference().child("saved").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).setValue(getRef(position).getKey().toString());
                        viewHolder.bookmarkCompany.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    }



                }
            });



    viewHolder.mLikeCompany.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            if(viewHolder.mLikeCompany.getTag().equals("liked")){
                key.setValue(new_offer.getLike() - 1);
                viewHolder.mLikeCompany.setTag("unliked");
                viewHolder.mLikeCompany.setImageResource(R.drawable.ic_unfavourite_black);

                FirebaseDatabase.getInstance().getReference().child("like").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).removeValue();
                while (MainActivity.likedList.contains(getRef(position).getKey())){
                    MainActivity.likedList.remove(getRef(position).getKey().toString());
                }

            }
            else {
                key.setValue(new_offer.getLike() + 1);
                FirebaseDatabase.getInstance().getReference().child("like").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).setValue(getRef(position).getKey().toString());
                MainActivity.likedList.add(getRef(position).getKey().toString());
                viewHolder.mLikeCompany.setImageResource(R.drawable.ic_favorite_black_24dp);
                viewHolder.mLikeCompany.setTag("liked");

            }




        }
    });
    params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
}
    else{

            //viewHolder.v.setLayoutParams(new LinearLayout.LayoutParams(0,0));
            viewHolder.v.setVisibility(View.GONE);
            params = new LinearLayout.LayoutParams(0,0);
        }
        viewHolder.v.setLayoutParams(params);
    }

public void interesting(ImageButton imageButton, TextView textView){
        DatabaseReference followingref = databaseReference.child("following").child(MainActivity.PHONE);
        followingref.child(textView.getText().toString()).setValue(textView.getText());
        imageButton.setClickable(false);
        imageButton.setAlpha((float)0.2);
}


public void bookmark(final OHolder viewHolder, final Offer model , final int position){
    final LinearLayout.LayoutParams params;
    if(MainActivity.savedList.contains(getRef(position).getKey())){

        viewHolder.v.setVisibility(View.VISIBLE);
        //this.offer = model;
        final Offer new_offer =getItem(position);


        setLikeButton(viewHolder.mLikeBookmark,getRef(position).getKey());

        final DatabaseReference key = getRef(position).child("like");
        viewHolder.mDescBookmark.setText(model.getDesc());
        viewHolder.mNameBookmark.setText(model.getName());
        Picasso.with(context).load(model.getImage()).into(viewHolder.mImageViewBookmark);
        viewHolder.startDateBookmark.setText(model.getStartDate());
        viewHolder.endDateBookmark.setText(model.getEndDate());
        //viewHolder.mLike.
        viewHolder.mNameBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Company.class);
                intent.putExtra("title",new_offer.getName());
                context.startActivity(intent);

            }
        });

        viewHolder.liketxtBookmark.setText(model.getLike().toString());
        viewHolder.mShareBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(context).load(model.getImage()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_TEXT, new_offer.getName()+"\n"+new_offer.getDesc()+"\n"+"Available: from "+new_offer.getStartDate()+" to "+new_offer.getEndDate());
                        intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                        context.startActivity(Intent.createChooser(intent, "Share"));
                    }

                    @Override
                    public void onBitmapFailed(Drawable drawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable drawable) {

                    }
                });
            }
        });
        viewHolder.interestBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interesting(viewHolder.interestBookmark,viewHolder.mNameBookmark);
            }
        });

        viewHolder.bookmarkBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(MainActivity.savedList.contains(getRef(position).getKey().toString())){


                    while (MainActivity.savedList.contains(getRef(position).getKey())){
                        MainActivity.savedList.remove(getRef(position).getKey().toString());
                    }

                    FirebaseDatabase.getInstance().getReference().child("saved").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).removeValue();
                    viewHolder.bookmarkBookmark.setImageResource(R.drawable.ic_unbookmark);

                }
                else {

                    MainActivity.savedList.add(getRef(position).getKey().toString());
                    FirebaseDatabase.getInstance().getReference().child("saved").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).setValue(getRef(position).getKey().toString());
                    viewHolder.bookmarkBookmark.setImageResource(R.drawable.ic_bookmark_black_24dp);
                }



            }
        });


        viewHolder.mLikeBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(viewHolder.mLikeBookmark.getTag().equals("liked")){
                    key.setValue(new_offer.getLike() - 1);
                    viewHolder.mLikeBookmark.setTag("unliked");
                    viewHolder.mLikeBookmark.setImageResource(R.drawable.ic_unfavourite_black);

                    FirebaseDatabase.getInstance().getReference().child("like").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).removeValue();
                    while (MainActivity.likedList.contains(getRef(position).getKey())){
                        MainActivity.likedList.remove(getRef(position).getKey().toString());
                    }

                }
                else {
                    key.setValue(new_offer.getLike() + 1);
                    FirebaseDatabase.getInstance().getReference().child("like").child(MainActivity.PHONE).child(getRef(position).getKey().toString()).setValue(getRef(position).getKey().toString());
                    MainActivity.likedList.add(getRef(position).getKey().toString());
                    viewHolder.mLikeBookmark.setImageResource(R.drawable.ic_favorite_black_24dp);
                    viewHolder.mLikeBookmark.setTag("liked");

                }




            }
        });
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
    }
    else{

        //viewHolder.v.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        viewHolder.v.setVisibility(View.GONE);
        params = new LinearLayout.LayoutParams(0,0);
    }
    viewHolder.v.setLayoutParams(params);
}

public void checkInterested(){
    DatabaseReference followingref1 = (DatabaseReference) databaseReference.child("following").child(MainActivity.PHONE);
    followingref1.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot companies : dataSnapshot.getChildren()){
                followingList.add(companies.getKey());
                Log.d("Company",companies.getKey());
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}


    public  ArrayList<String> getFollowingList() {
        return followingList;
    }

    public  void setFollowingList(ArrayList<String> followingList) {
        this.followingList = followingList;
    }

public void setLikeButton(ImageButton likeButton,String key){
    if(MainActivity.likedList.contains(key)){
        likeButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        likeButton.setTag("liked");
    }
    else{
        likeButton.setImageResource(R.drawable.ic_unfavourite_black);
        likeButton.setTag("unliked");
    }
}
    public void setSavedButton(ImageButton savedButton,String key){
        if(MainActivity.savedList.contains(key)){
            savedButton.setImageResource(R.drawable.ic_bookmark_black_24dp);
            savedButton.setTag("saved");
        }
        else{
            savedButton.setImageResource(R.drawable.ic_unbookmark);
            savedButton.setTag("unsaved");
        }
    }
    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

}
