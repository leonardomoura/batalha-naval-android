package univali.m3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class main_activity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(main_activity.this, MenuActivity.class);
		startActivity(intent);
    }
}