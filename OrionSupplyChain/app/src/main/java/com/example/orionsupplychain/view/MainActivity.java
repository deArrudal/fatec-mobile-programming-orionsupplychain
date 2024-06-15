package com.example.orionsupplychain.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.orionsupplychain.R;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            loadFragment(bundle);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutFragment, new InitialPageFragment());
            fragmentTransaction.commit();
        }
    }

    private void loadFragment(Bundle bundle) {
        String itemType = bundle.getString("type");

        assert itemType != null;
        switch (itemType) {
            case "customer":
                fragment = new CustomerFragment();
                break;
            case "food":
                fragment = new FoodFragment();
                break;
            case "goods":
                fragment = new GoodsFragment();
                break;
            case "orders":
                fragment = new OrdersFragment();
                break;
            default:
                fragment = new SupplyOrderFragment();
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutFragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, MainActivity.class);

        if (id == R.id.itemCustomer) {
            bundle.putString("type", "customer");
        } else if (id == R.id.itemFood) {
            bundle.putString("type", "food");
        } else if (id == R.id.itemGoods) {
            bundle.putString("type", "goods");
        } else if (id == R.id.itemOrders) {
            bundle.putString("type", "orders");
        } else if (id == R.id.itemSupplyOrder) {
            bundle.putString("type", "supplyOrder");
        }

        intent.putExtras(bundle);
        this.startActivity(intent);
        this.finish();
        return true;
    }
}