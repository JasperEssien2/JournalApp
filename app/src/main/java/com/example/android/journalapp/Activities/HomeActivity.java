package com.example.android.journalapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.journalapp.Adapters.ContentHomeViewPagerAdapter;
import com.example.android.journalapp.App;
import com.example.android.journalapp.POJOs.UserDetails;
import com.example.android.journalapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    public boolean itemSelected;
    public Menu menu;
    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    //    private ArrayList<String> mJournalIds;
//    private String mDeleteFrom;
    private EditOptions mEditOptions;
    private CircleImageView mNavProfilePics;
    private TextView mNavUsername, mNavEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        setSupportActionBar(mToolbar);
        setUpViewPager();
        App.getsUsersDetailNode()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                        if (userDetails != null) {
                            if(userDetails.getmEmailAddress() != null
                                    && userDetails.getmUsername() != null) {
                                mNavEmail.setText(userDetails.getmEmailAddress());
                                mNavUsername.setText(userDetails.getmUsername());
                                if(!TextUtils.isEmpty(userDetails.getmProfilePicUrl())
                                        && userDetails.getmProfilePicUrl() != null) {
                                    Picasso
                                            .get()
                                            .load(userDetails.getmProfilePicUrl())
                                            .error(R.color.colorLightGray)
                                            .into(mNavProfilePics);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        if (!itemSelected) {
            getMenuInflater().inflate(R.menu.home, menu);
            return true;
        } else {
            getMenuInflater().inflate(R.menu.item_selecteed_edit_actions, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout) {
//            DatabaseHelper.logOut();
            App.getFirebaseAuth().signOut();
            App.setsFirebaseUser(null);
            startActivity(new Intent(this, SignInActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK));
            return true;
        }
//        JournalListPresenter.EditOptions editOptions = (JournalListPresenter.EditOptions) this;
        if (id == R.id.action_delete) {
            mEditOptions.onMultipleDeleteButton(true);
            return true;
        }

        if (id == R.id.action_share) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share_view_friends) {
//            startActivity(new Intent(this, ViewFriendProfileActivity.class)
//                    .putExtra(Constants.EXTRA_VIEW_ACTIVITY, Constants.EXTRA_VIEW_FRIENDS));
        } else if (id == R.id.nav_my_profile) {
            startActivity(new Intent(this, ViewProfileActivity.class));
        }

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mFab = (FloatingActionButton) findViewById(R.id.fab);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mViewPager = (ViewPager) findViewById(R.id.view_pager_content_home);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_content_home);
        mNavProfilePics = (CircleImageView) mNavigationView.getHeaderView(0).findViewById(R.id.circle_image_user_profile);
        mNavEmail = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.text_user_profile_email);
        mNavUsername = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.text_user_profile_name);
    }

    private void setUpViewPager() {
        ContentHomeViewPagerAdapter contentHomeViewPagerAdapter
                = new ContentHomeViewPagerAdapter(this, getSupportFragmentManager(),
                mTabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(contentHomeViewPagerAdapter);
    }

    public void setEditOptions(EditOptions editOptions){
        mEditOptions = editOptions;
    }

    public interface EditOptions{
        void onMultipleDeleteButton(boolean isClicked);

        void shareButtonClicked(boolean shareButton);
    }
}
