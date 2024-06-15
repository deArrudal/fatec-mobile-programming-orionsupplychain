package com.example.orionsupplychain.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orionsupplychain.R;
import com.example.orionsupplychain.controller.GoodsController;
import com.example.orionsupplychain.controller.SupplyOrderController;
import com.example.orionsupplychain.model.Customer;
import com.example.orionsupplychain.model.Item;
import com.example.orionsupplychain.model.SupplyOrder;
import com.example.orionsupplychain.persistence.GoodsDAO;
import com.example.orionsupplychain.persistence.SupplyOrderDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {
    private View view;
    private EditText editTextOrdersSupplyOrderId;
    private TextView textViewOrdersCustomerOutput;
    private TextView textViewOrdersSupplyOrderOutput;
    private SupplyOrderController supplyOrderController;


    public OrdersFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_orders, container, false);

        editTextOrdersSupplyOrderId = view.findViewById(R.id.editTextOrdersSupplyOrderId);
        textViewOrdersCustomerOutput = view.findViewById(R.id.textViewOrdersCustomerOutput);
        textViewOrdersCustomerOutput.setMovementMethod(new ScrollingMovementMethod());
        textViewOrdersSupplyOrderOutput = view.findViewById(R.id.textViewOrdersSupplyOrderOutput);
        textViewOrdersSupplyOrderOutput.setMovementMethod(new ScrollingMovementMethod());

        supplyOrderController = new SupplyOrderController(new SupplyOrderDAO(this.getContext()));

        view.findViewById(R.id.buttonOrdersCustomerIdSearch).setOnClickListener(
                searchOrders -> searchOrders());
        view.findViewById(R.id.buttonOrdersSupplyOrderIdSearch).setOnClickListener(
                searchSupplyOrder -> searchSupplyOrder());
        view.findViewById(R.id.buttonOrdersSupplyOrderIdRemove).setOnClickListener(
                removeSupplyOrder -> removeSupplyOrder());

        return view;
    }

    private void searchOrders() {
        try {
            List<SupplyOrder> supplyOrders = supplyOrderController.listEntry();

            StringBuilder stringBuffer = new StringBuilder();

            for (SupplyOrder supplyOrder : supplyOrders) {
                stringBuffer.append(supplyOrder.toString()).append("\n");
            }

            textViewOrdersCustomerOutput.setText(stringBuffer.toString());

        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void searchSupplyOrder() {
        SupplyOrder supplyOrder;

        try {
            if (editTextOrdersSupplyOrderId.length() == 0) {
                throw new IllegalArgumentException("Invalid input");
            }

            supplyOrder = supplyOrderController.searchEntry(new SupplyOrder(
                    Integer.parseInt(editTextOrdersSupplyOrderId.getText().toString()),
                    null, LocalDate.now()));

            if (supplyOrder != null) {
                StringBuilder stringBuffer = new StringBuilder();

                stringBuffer.append(String.format("Order: %s\n",
                        String.valueOf(supplyOrder.getSupplyOrderId())));
                stringBuffer.append(String.format("Customer: %s\n",
                        supplyOrder.getSupplyOrderCustomer().getCustomerName()));
                stringBuffer.append(String.format("Date: %s\n",
                        supplyOrder.getSupplyOrderDate().toString()));
                stringBuffer.append(String.format("Estimate Delivery: %s\n",
                        supplyOrder.getSupplyOrderDeliveryDate().toString()));

                List<Item> items = supplyOrder.getSupplyOrderItems();
                for (Item item : items) {
                    stringBuffer.append(item.toString()).append("\n");
                }

                textViewOrdersSupplyOrderOutput.setText(stringBuffer.toString());

            } else {
                Toast.makeText(
                        view.getContext(), "Order not found", Toast.LENGTH_LONG).show();

            }
        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void removeSupplyOrder() {
        try {
            if (editTextOrdersSupplyOrderId.length() == 0) {
                throw new IllegalArgumentException("Invalid input");
            }

            supplyOrderController.removeEntry(new SupplyOrder(
                    Integer.parseInt(editTextOrdersSupplyOrderId.getText().toString()),
                    null, LocalDate.now()));

            Toast.makeText(
                    view.getContext(), "Order removed successfully", Toast.LENGTH_LONG).show();

        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearOrderValues();
    }

    private void clearOrderValues() {
        editTextOrdersSupplyOrderId.setText("");
    }
}