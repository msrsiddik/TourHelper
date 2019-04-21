package msr.zerone.tourhelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import msr.zerone.tourhelper.cameraandgallery.FullImageViewFragment;
import msr.zerone.tourhelper.cameraandgallery.GalleryFragment;
import msr.zerone.tourhelper.eventfragment.EventHomeFragment;
import msr.zerone.tourhelper.userfragment.LoginFragment;
import msr.zerone.tourhelper.userfragment.RegistrationFragment;
import msr.zerone.tourhelper.weather.WeatherHomeFragment;

public class MainActivity extends AppCompatActivity implements FragmentInter, NavigationView.OnNavigationItemSelectedListener {
    private FragmentManager manager;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    static final int REQUEST_TAKE_PHOTO = 1;
    public static String currentPhotoPath;

    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getSupportFragmentManager();
        if (WeatherHomeFragment.reStartWeather){
            manager.beginTransaction().replace(R.id.fragmentContainer, new WeatherHomeFragment()).commit();

        }else {
            manager.beginTransaction().add(R.id.fragmentContainer, new LoginFragment()).commit();
        }
        setupToolbarMenu();
        setupNavigationDrawerMenu();


    }

    private void setupToolbarMenu() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Tour Helper");
        mToolbar.inflateMenu(R.menu.main_toolbar_menu);
    }


    private void setupNavigationDrawerMenu() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        closeDrawer();

        switch (menuItem.getItemId()){
            case R.id.weather:
                manager.beginTransaction().replace(R.id.fragmentContainer, new WeatherHomeFragment()).commit();
                break;
            case R.id.home:
                manager.beginTransaction().replace(R.id.fragmentContainer, new LoginFragment()).commit();
                break;
            case R.id.camera:
                dispatchTakePictureIntent();
                break;
            case R.id.gallery:
                manager.beginTransaction().replace(R.id.fragmentContainer, new GalleryFragment()).commit();
                break;
            case R.id.login:
                manager.beginTransaction().replace(R.id.fragmentContainer, new LoginFragment()).commit();
                break;
            case R.id.eventHome:
                manager.beginTransaction().replace(R.id.fragmentContainer, new EventHomeFragment()).commit();
                break;

        }
        return true;
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
        else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_toolbar_menu, menu);
//        return true;
//    }


    @Override
    protected void onStart() {
        super.onStart();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem loginItem = menu.findItem(R.id.loginToolbarItem);
        MenuItem logoutItem = menu.findItem(R.id.logOutToolbarItem);
        if(fUser.isEmailVerified()){
            loginItem.setVisible(false);
            logoutItem.setVisible(true);
        }else{
            loginItem.setVisible(true);
            logoutItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.loginToolbarItem:
                Toast.makeText(this, "login", Toast.LENGTH_SHORT).show();
//                userPreference.setLoginStatus(true);
                break;
            case R.id.logOutToolbarItem:
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
//                userPreference.setLoginStatus(false);
                break;
        }
        return super.onOptionsItemSelected(item);
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

    }



    @Override
    public void gotoRegistrationFragment() {
        manager.beginTransaction().addToBackStack("RegistrationFragment").replace(R.id.fragmentContainer, new RegistrationFragment()).commit();
    }

    @Override
    public void gotoFullImageViewFragment() {
        manager.beginTransaction().addToBackStack("FullImageViewFragment").replace(R.id.fragmentContainer, new FullImageViewFragment()).commit();
    }
}
