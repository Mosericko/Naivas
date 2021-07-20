package com.mdevs.naivas.productmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mdevs.naivas.R;
import com.mdevs.naivas.productmanager.fragments.AddProductsFragment;
import com.mdevs.naivas.productmanager.fragments.ArchivedProducts;
import com.mdevs.naivas.productmanager.fragments.ProductFragment;
import com.mdevs.naivas.productmanager.fragments.ProfileFragment;

public class ProductActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        bottomNavigationView=findViewById(R.id.prod_bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.products_container,new ProductFragment()).commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){

                case R.id.prodDash:
                    selectedFragment= new ProductFragment();
                    break;

                case R.id.addProd:
                    selectedFragment= new AddProductsFragment();
                    break;

                case R.id.archived:
                    selectedFragment= new ArchivedProducts();
                    break;

                case R.id.profile:
                    selectedFragment= new ProfileFragment();
                    break;


            }
            assert selectedFragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.products_container, selectedFragment).commit();
            return true;
        }
    };
}