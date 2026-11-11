package com.example.quan_ly_kho_hang;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.quan_ly_kho_hang.database.DatabaseHelper;
import com.example.quan_ly_kho_hang.model.Product;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView btnDetailBackTop;
    private TextView tvDetailName;
    private TextView tvDetailCategory;
    private TextView tvDetailPrice;
    private TextView tvDetailQuantity;
    private TextView tvDetailWarning;
    private TextView tvDetailTotalValue;
    private TextView tvDetailDescription;

    private MaterialButton btnImport;
    private MaterialButton btnSell;
    private MaterialButton btnBackBottom;
    private MaterialButton btnDelete;

    private DatabaseHelper dbHelper;
    private int productId = -1;
    private Product currentProduct;
    private final DecimalFormat formatter = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        dbHelper = new DatabaseHelper(this);

        productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (productId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin sản phẩm!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnDetailBackTop = findViewById(R.id.btnDetailBackTop);
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailCategory = findViewById(R.id.tvDetailCategory);
        tvDetailPrice = findViewById(R.id.tvDetailPrice);
        tvDetailQuantity = findViewById(R.id.tvDetailQuantity);
        tvDetailWarning = findViewById(R.id.tvDetailWarning);
        tvDetailTotalValue = findViewById(R.id.tvDetailTotalValue);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);

        btnImport = findViewById(R.id.btnImport);
        btnSell = findViewById(R.id.btnSell);
        btnBackBottom = findViewById(R.id.btnBackBottom);
        btnDelete = findViewById(R.id.btnDelete);

        loadProductDetail();

        View.OnClickListener backListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        btnDetailBackTop.setOnClickListener(backListener);
        btnBackBottom.setOnClickListener(backListener);

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentProduct != null) {
                    int newQty = currentProduct.getQuantity() + 1;
                    dbHelper.updateQuantity(productId, newQty);
                    loadProductDetail();
                    Toast.makeText(ProductDetailActivity.this, "Đã nhập thêm 1 sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentProduct != null) {
                    if (currentProduct.getQuantity() == 0) {
                        Toast.makeText(ProductDetailActivity.this, "Hết hàng, không thể bán thêm!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int newQty = currentProduct.getQuantity() - 1;
                    dbHelper.updateQuantity(productId, newQty);
                    loadProductDetail();
                    Toast.makeText(ProductDetailActivity.this, "Đã bán 1 sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });
    }

    private void deleteProduct() {
        int rows = dbHelper.deleteProduct(productId);
        if (rows > 0) {
            Toast.makeText(ProductDetailActivity.this, "Đã xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(ProductDetailActivity.this, "Không thể xóa sản phẩm!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProductDetail() {
        currentProduct = dbHelper.getProductById(productId);

        if (currentProduct == null) {
            Toast.makeText(this, "Sản phẩm không tồn tại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvDetailName.setText(currentProduct.getName());
        tvDetailCategory.setText(currentProduct.getCategory());

        String formattedPrice = formatter.format(currentProduct.getPrice()).replace(',', '.') + " đ";
        tvDetailPrice.setText(formattedPrice);

        tvDetailQuantity.setText(String.valueOf(currentProduct.getQuantity()));
        if (currentProduct.getQuantity() < 5) {
            tvDetailWarning.setVisibility(View.VISIBLE);
            tvDetailQuantity.setTextColor(ContextCompat.getColor(this, R.color.accent_red));
        } else {
            tvDetailWarning.setVisibility(View.GONE);
            tvDetailQuantity.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        }

        long totalStockValue = (long) currentProduct.getPrice() * currentProduct.getQuantity();
        String formattedTotalValue = formatter.format(totalStockValue).replace(',', '.') + " đ";
        tvDetailTotalValue.setText(formattedTotalValue);

        if (TextUtils.isEmpty(currentProduct.getDescription())) {
            tvDetailDescription.setText("(Chưa có mô tả cho sản phẩm này)");
        } else {
            tvDetailDescription.setText(currentProduct.getDescription());
        }
    }
}
