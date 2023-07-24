package com.example.foodmenu.RecyclerViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodmenu.Entity.Order;
import com.example.foodmenu.Fragments.CartFragment;
import com.example.foodmenu.Fragments.CustomerOrderFragment;
import com.example.foodmenu.R;
import com.example.foodmenu.Utils.FragmentUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Order> orders;
    private Context context;

    public OrderRecyclerViewAdapter(ArrayList<Order> orders, Context context){
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.order_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.order_id.setText("Order ID : " + orders.get(position).getOrder_id());
        holder.customer_id.setText("Customer Name : " + orders.get(position).getCustomer_id());
    }

    @Override
    public int getItemCount() {
        if(orders==null){
            return 0;
        }
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView order_id, customer_id;
        private ConstraintLayout card_ConstraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            InitViews(itemView);
            Listeners(itemView);
        }

        private void InitViews(View view){
            order_id = view.findViewById(R.id.orderId_TextView);
            customer_id = view.findViewById(R.id.name_TextView);
            card_ConstraintLayout = view.findViewById(R.id.card_ConstraintLayout);
        }

        private void Listeners(View view){
            card_ConstraintLayout.setOnClickListener(v -> {
                FragmentUtils.ReplaceFragment(
                        ((AppCompatActivity)itemView.getContext()).getSupportFragmentManager(),
                        R.id.user_FrameLayout,
                        new CustomerOrderFragment(
                                orders.get(getAdapterPosition()).getOrder_id(), context
                        )
                );
            });
        }
    }
}
