package waasi.waasi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class InviteActivity extends AppCompatActivity {
    private Button btnShare;
    private TextView txtCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        txtCode = (TextView)findViewById(R.id.txtCode);
        btnShare = (Button)findViewById(R.id.shareBtn);
        String code = getIntent().getStringExtra("code");
        txtCode.setText(code);
    }
}
