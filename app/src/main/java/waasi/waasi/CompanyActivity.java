package waasi.waasi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CompanyActivity extends AppCompatActivity {
    private TextView company_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setTitleColor(R.color.offer);
        setContentView(R.layout.com);
        company_name = (TextView)findViewById(R.id.comp);
        String company = getIntent().getStringExtra("company_name");
        company_name.setText(company);


    }
}
