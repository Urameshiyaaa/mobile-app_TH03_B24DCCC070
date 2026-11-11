package com.example.quan_ly_kho_hang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quan_ly_kho_hang.adapter.ProductAdapter;
import com.example.quan_ly_kho_hang.database.DatabaseHelper;
import com.example.quan_ly_kho_hang.model.Product;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvTotalProducts;
    private TextView tvTotalValue;
    private TextView tvLowStock;
    private RecyclerView rvProducts;
    private TextView tvEmptyList;
    private MaterialButton btnAddProduct;

    private DatabaseHelper dbHelper;
    private ProductAdapter adapter;
    private List<Product> productList;
    private final DecimalFormat formatter = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvLowStock = findViewById(R.id.tvLowStock);
        rvProducts = findViewById(R.id.rvProducts);
        tvEmptyList = findViewById(R.id.tvEmptyList);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("PRODUCT_ID", product.getId());
                startActivity(intent);
            }
        });

        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setAdapter(adapter);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        productList = dbHelper.getAllProducts();
        adapter.updateData(productList);

        if (productList.isEmpty()) {
            tvEmptyList.setVisibility(View.VISIBLE);
            rvProducts.setVisibility(View.GONE);
        } else {
            tvEmptyList.setVisibility(View.GONE);
            rvProducts.setVisibility(View.VISIBLE);
        }

        tvTotalProducts.setText(String.valueOf(productList.size()));

        long totalValue = dbHelper.getTotalValue();
        String formattedValue = formatter.format(totalValue).replace(',', '.') + " đ";
        tvTotalValue.setText(formattedValue);

        int lowStockCount = dbHelper.countLowStock();
        tvLowStock.setText(String.valueOf(lowStockCount));
    }
}
