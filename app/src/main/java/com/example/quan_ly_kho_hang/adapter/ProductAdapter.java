package com.example.quan_ly_kho_hang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quan_ly_kho_hang.R;
import com.example.quan_ly_kho_hang.model.Product;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private List<Product> productList;
    private final OnProductClickListener listener;
    private final DecimalFormat formatter = new DecimalFormat("#,###");

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(Context context, List<Product> productList, OnProductClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    public void updateData(List<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvProductName.setText(product.getName());

        String formattedPrice = formatter.format(product.getPrice()).replace(',', '.') + " đ";
        holder.tvProductPrice.setText("Giá bán: " + formattedPrice);

        holder.tvProductCategory.setText(product.getCategory());

        holder.tvProductQuantity.setText("SL: " + product.getQuantity());

        if (product.getQuantity() < 5) {
            holder.tvProductWarning.setVisibility(View.VISIBLE);
            holder.tvProductQuantity.setTextColor(ContextCompat.getColor(context, R.color.accent_red));
        } else {
            holder.tvProductWarning.setVisibility(View.GONE);
            holder.tvProductQuantity.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductQuantity;
        TextView tvProductCategory;
        TextView tvProductWarning;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvProductCategory = itemView.findViewById(R.id.tvProductCategory);
            tvProductWarning = itemView.findViewById(R.id.tvProductWarning);
        }
    }
}
