package com.example.quan_ly_kho_hang;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.quan_ly_kho_hang.database.DatabaseHelper;
import com.example.quan_ly_kho_hang.model.Product;
import com.google.android.material.button.MaterialButton;

public class AddProductActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etProductName;
    private EditText etProductPrice;
    private EditText etProductQuantity;
    private Spinner spCategory;
    private SwitchCompat switchActive;
    private EditText etProductDescription;
    private MaterialButton btnSaveProduct;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        dbHelper = new DatabaseHelper(this);

        // Ánh xạ View
        btnBack = findViewById(R.id.btnBack);
        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductQuantity = findViewById(R.id.etProductQuantity);
        spCategory = findViewById(R.id.spCategory);
        switchActive = findViewById(R.id.switchActive);
        etProductDescription = findViewById(R.id.etProductDescription);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.category_list,
                android.R.layout.simple_spinner_item
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoryAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
    }

    private void saveProduct() {
        String name = etProductName.getText().toString().trim();
        String priceStr = etProductPrice.getText().toString().trim();
        String quantityStr = etProductQuantity.getText().toString().trim();
        String category = spCategory.getSelectedItem() != null ? spCategory.getSelectedItem().toString() : "Khác";
        int active = switchActive.isChecked() ? 1 : 0;
        String description = etProductDescription.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etProductName.setError("Vui lòng nhập tên sản phẩm");
            etProductName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(priceStr)) {
            etProductPrice.setError("Vui lòng nhập giá bán");
            etProductPrice.requestFocus();
            return;
        }
        int price;
        try {
            price = Integer.parseInt(priceStr);
            if (price <= 0) {
                etProductPrice.setError("Giá bán phải lớn hơn 0");
                etProductPrice.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            etProductPrice.setError("Giá bán không hợp lệ");
            etProductPrice.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(quantityStr)) {
            etProductQuantity.setError("Vui lòng nhập số lượng");
            etProductQuantity.requestFocus();
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity < 0) {
                etProductQuantity.setError("Số lượng không được âm");
                etProductQuantity.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            etProductQuantity.setError("Số lượng không hợp lệ");
            etProductQuantity.requestFocus();
            return;
        }

        Product product = new Product(name, price, quantity, category, active, description);
        long newId = dbHelper.insertProduct(product);

        if (newId != -1) {
            Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Có lỗi xảy ra, không thể thêm sản phẩm!", Toast.LENGTH_SHORT).show();
        }
    }
}
