package be.unamur.projetLabo.activities;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;

public class BaseActivity extends AppCompatActivity {
    protected MenuItem action_bar_progress;
    private boolean progressShown = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.action_profile:
                startActivity(new Intent(BaseActivity.this, ProfileActivity.class));
                return true;
            case R.id.action_logout:
                ProjetLabo.user = null;
                Intent intent = new Intent(BaseActivity.this, ConnexionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                BaseActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empty, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        action_bar_progress = menu.findItem(R.id.action_bar_progress);
        if(action_bar_progress != null){
            action_bar_progress.setVisible(this.progressShown);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public void showProgressBar(){
        if(action_bar_progress != null){
            action_bar_progress.setVisible(true);
        }
        this.progressShown = true;
    }
    public void hideProgressBar(){
        if(action_bar_progress != null){
            action_bar_progress.setVisible(false);
        }
        this.progressShown = false;
    }
}
