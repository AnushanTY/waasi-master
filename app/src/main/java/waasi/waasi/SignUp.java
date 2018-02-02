package waasi.waasi;

/**
 * Created by Anushan on 10/8/2017.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private EditText Regname , regEmail, dataofbirth, currentDis, phoneNum;
    private Button  btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Regname=(EditText)findViewById(R.id.regName);
        regEmail=(EditText)findViewById(R.id.Regemail);
        dataofbirth=(EditText)findViewById(R.id.DataOfBirth);
        currentDis=(EditText)findViewById(R.id.CurrentDis);
        phoneNum=(EditText)findViewById(R.id.phoneNum);

        auth = FirebaseAuth.getInstance();


        btnSignUp = (Button) findViewById(R.id.Reg);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = Regname.getText().toString().trim();
                String email = regEmail.getText().toString().trim();
                String date = dataofbirth.getText().toString();
                String dis = currentDis.getText().toString().trim();
                String pho = phoneNum.getText().toString();



                if (TextUtils.isEmpty(email) ) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(date)) {
                    Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(dis)) {
                    Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pho)) {
                    Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                RegUser regUser= new RegUser(name,email,date,dis,pho);

                mDatabase.child("Users").child(auth.getCurrentUser().getUid()).setValue(regUser,
                        new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Data Insert Error!", Toast.LENGTH_SHORT).show();

                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Intent ne =new Intent(SignUp.this,MainActivity.class);
                                    startActivity(ne);

                                }
                            }
                        });

//                Intent i = new Intent (SignUp.this,MainAct.class);
//                Bundle b =new Bundle();
//                b.putString("email",email);
//                b.putString("Password",password);
//                i.putExtras(b);
//                startActivity(i);
//                finish();
//                //create user

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

}

