package chalmers.eda397g1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Ziwei on 11-Apr-17.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
