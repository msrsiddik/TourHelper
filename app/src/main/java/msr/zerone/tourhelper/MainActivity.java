package msr.zerone.tourhelper;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import msr.zerone.tourhelper.cameraandgallery.FullImageViewFragment;
import msr.zerone.tourhelper.cameraandgallery.GalleryFragment;
import msr.zerone.tourhelper.cameraandgallery.ImageCollection;
import msr.zerone.tourhelper.eventfragment.EventHomeFragment;
import msr.zerone.tourhelper.map.MapFragment;
import msr.zerone.tourhelper.networkinfo.MyReceiver;
import msr.zerone.tourhelper.networkinfo.NetworkUtil;
import msr.zerone.tourhelper.userfragment.DashboardFragment;
import msr.zerone.tourhelper.userfragment.LoginFragment;
import msr.zerone.tourhelper.userfragment.RegistrationFragment;
import msr.zerone.tourhelper.weather.WeatherHomeFragment;

import static msr.zerone.tourhelper.THfirebase.fAuth;
import static msr.zerone.tourhelper.THfirebase.userReference;

public class MainActivity extends AppCompatActivity implements FragmentInter, NavigationView.OnNavigationItemSelectedListener {
    private FragmentManager manager;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView navigation;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static String currentPhotoPath;

    private BroadcastReceiver myReceiver = null;

    private FirebaseUser user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = fAuth.getCurrentUser();
        myReceiver = new MyReceiver();

        manager = getSupportFragmentManager();
        if (user != null && !WeatherHomeFragment.reStartWeather){
            manager.beginTransaction().add(R.id.fragmentContainer, new DashboardFragment()).commit();
        }
        else if (WeatherHomeFragment.reStartWeather){
            manager.beginTransaction().replace(R.id.fragmentContainer, new WeatherHomeFragment()).commit();
        }
        else {
            manager.beginTransaction().add(R.id.fragmentContainer, new LoginFragment()).commit();
        }

        setupToolbarMenu();
        setupNavigationDrawerMenu();

        getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        broadcastIntent();

    }

    public void broadcastIntent() {
        registerReceiver(myReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(myReceiver);
//    }



    private void setupToolbarMenu() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Tour Helper");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.inflateMenu(R.menu.main_toolbar_menu);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.netInfoToolbarItem:
                        broadcastIntent();
                        break;
                    case R.id.cameraToolbarItem:
                        dispatchTakePictureIntent();
                        break;
                    case R.id.loginToolbarItem:
                        manager.beginTransaction().replace(R.id.fragmentContainer, new LoginFragment()).commit();
                        break;
                    case R.id.syncPhotos:
                        ImageCollection collect = new ImageCollection();
                        collect.syncPhoto(MainActivity.this);
                        break;
                    case R.id.uploadPhotos:
                        NetworkUtil.isOffline(MainActivity.this, new ViewGroup(MainActivity.this) {
                            @Override
                            protected void onLayout(boolean changed, int l, int t, int r, int b) {

                            }
                        });

                        ImageCollection collection = new ImageCollection();
                        collection.uploadPhoto(MainActivity.this);
                        break;
                    case R.id.logOutToolbarItem:
                        logout();
                        break;
                }
                return false;
            }
        });
    }


    private void setupNavigationDrawerMenu() {
        navigationView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.dl);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigation = findViewById(R.id.navigationBottom);
        navigation.setBackgroundColor(Color.rgb(233,180,250));
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_dashboard:
                        if (user != null) {
                            gotoDashboardFragment();
                        }else {
                            manager.beginTransaction().replace(R.id.fragmentContainer, new LoginFragment()).commit();
                        }
                        break;
                    case R.id.action_nav:
                        showDrawer();
                        break;
                }
                return false;
            }
        });

        navAndToolbarItemHide();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        closeDrawer();

        switch (menuItem.getItemId()){
            case R.id.weather:
                gotoWeatherFragment();
                break;
            case R.id.home:
                gotoDashboardFragment();
                break;
            case R.id.camera:
                dispatchTakePictureIntent();
                break;
            case R.id.gallery:
                gotoGalleryFragment();
                break;
            case R.id.login:
                manager.beginTransaction().replace(R.id.fragmentContainer, new LoginFragment()).commit();
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.map:
                gotoMapFragment();
                break;
            case R.id.eventHome:
                gotoEventFragment();
                break;

        }
        return true;
    }

    private void navAndToolbarItemHide(){
        Menu nav_menu = navigationView.getMenu();
        Menu toolbar_menu = mToolbar.getMenu();
        if (user != null) {
            nav_menu.findItem(R.id.login).setVisible(false);
            nav_menu.findItem(R.id.logout).setVisible(true);
            nav_menu.findItem(R.id.eventHome).setVisible(true);
            nav_menu.findItem(R.id.home).setVisible(true);
            nav_menu.findItem(R.id.map).setVisible(true);

            toolbar_menu.findItem(R.id.loginToolbarItem).setVisible(false);
            toolbar_menu.findItem(R.id.logOutToolbarItem).setVisible(true);
            toolbar_menu.findItem(R.id.uploadPhotos).setVisible(true);
        }else {
            nav_menu.findItem(R.id.login).setVisible(true);
            nav_menu.findItem(R.id.logout).setVisible(false);

            toolbar_menu.findItem(R.id.loginToolbarItem).setVisible(true);
            toolbar_menu.findItem(R.id.logOutToolbarItem).setVisible(false);
            toolbar_menu.findItem(R.id.uploadPhotos).setVisible(false);
        }
    }

    private void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void showDrawer(){
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            closeDrawer();
        }
        else if (manager.getBackStackEntryCount()>0){
            int fragments = manager.getBackStackEntryCount();
            if (fragments == 1) {
                manager.popBackStack();
            } else if (manager.getBackStackEntryCount() > 1) {
                manager.popBackStack();
            }
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.tour_helper);
            builder.setTitle("Tour Helper");
            builder.setMessage("Do you want to close this app?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    MainActivity.super.onBackPressed();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.show();

        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        user = fAuth.getCurrentUser();
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "msr.zerone.tourhelper",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

        gotoGalleryFragment();

    }

    private void logout() {
        fAuth.signOut();
        FragmentInter inter = MainActivity.this;
        inter.login(false);
        File[] allPhotos = new ImageCollection().matches();
        for (File f : allPhotos){
            f.delete();
        }
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);//Start the same Activity
        finish();
    }


    @Override
    public void gotoRegistrationFragment() {
        manager.beginTransaction().addToBackStack("RegistrationFragment").replace(R.id.fragmentContainer, new RegistrationFragment()).commit();
    }

    @Override
    public void gotoFullImageViewFragment() {
        manager.beginTransaction().addToBackStack("FullImageViewFragment").replace(R.id.fragmentContainer, new FullImageViewFragment()).commit();
    }

    @Override
    public void gotoWeatherFragment() {
        manager.beginTransaction().addToBackStack(null).replace(R.id.fragmentContainer, new WeatherHomeFragment()).commit();
    }

    @Override
    public void gotoGalleryFragment() {
        manager.beginTransaction().addToBackStack(null).replace(R.id.fragmentContainer, new GalleryFragment()).commit();
    }

    @Override
    public void gotoEventFragment() {
        manager.beginTransaction().addToBackStack(null).replace(R.id.fragmentContainer, new EventHomeFragment()).commit();
    }

    @Override
    public void gotoMapFragment() {
        manager.beginTransaction().addToBackStack(null).replace(R.id.fragmentContainer, new MapFragment()).commit();
    }

    @Override
    public void gotoDashboardFragment() {
        manager.beginTransaction().replace(R.id.fragmentContainer, new DashboardFragment()).commit();
    }


    @Override
    public void login(boolean b) {
        Menu nav_menu = navigationView.getMenu();
        Menu toolbar_menu = mToolbar.getMenu();

        user = fAuth.getCurrentUser();
        if (b) {
            nav_menu.findItem(R.id.login).setVisible(false);
            nav_menu.findItem(R.id.logout).setVisible(true);
            nav_menu.findItem(R.id.eventHome).setVisible(true);
            nav_menu.findItem(R.id.home).setVisible(true);
            nav_menu.findItem(R.id.map).setVisible(true);

            toolbar_menu.findItem(R.id.loginToolbarItem).setVisible(false);
            toolbar_menu.findItem(R.id.logOutToolbarItem).setVisible(true);
            toolbar_menu.findItem(R.id.uploadPhotos).setVisible(true);
            toolbar_menu.findItem(R.id.syncPhotos).setVisible(true);

        }else {
            nav_menu.findItem(R.id.login).setVisible(true);
            nav_menu.findItem(R.id.logout).setVisible(false);

            toolbar_menu.findItem(R.id.loginToolbarItem).setVisible(false);
            toolbar_menu.findItem(R.id.logOutToolbarItem).setVisible(true);
            toolbar_menu.findItem(R.id.uploadPhotos).setVisible(true);
            toolbar_menu.findItem(R.id.syncPhotos).setVisible(true);

        }
    }

    @Override
    public void onNetworkConnectionChanged(String status) {
        Menu netIconToolbar = mToolbar.getMenu();
        switch (status){
            case "Wifi enabled":
                netIconToolbar.findItem(R.id.netInfoToolbarItem).setIcon(R.drawable.ic_wifi);
                break;
            case "Mobile data enabled":
                netIconToolbar.findItem(R.id.netInfoToolbarItem).setIcon(R.drawable.ic_signal_cellular);
                break;
            case "No internet is available":
            case "No Internet Connection":
                netIconToolbar.findItem(R.id.netInfoToolbarItem).setIcon(R.drawable.ic_airplanemode);
                break;
        }
    }

}
