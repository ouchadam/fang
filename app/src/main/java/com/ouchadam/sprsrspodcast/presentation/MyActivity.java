package com.ouchadam.sprsrspodcast.presentation;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.ouchadam.sprsrspodcast.R;
import com.ouchadam.sprsrspodcast.debug.DebugActivity;

public class MyActivity extends DrawerActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.debug, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ab_debug)  {
            startActivity(new Intent(this, DebugActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
