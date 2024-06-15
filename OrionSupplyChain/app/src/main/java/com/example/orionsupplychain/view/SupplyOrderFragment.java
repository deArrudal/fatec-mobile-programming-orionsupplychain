package com.example.orionsupplychain.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orionsupplychain.R;
import com.example.orionsupplychain.controller.CustomerController;
import com.example.orionsupplychain.controller.FoodController;
import com.example.orionsupplychain.controller.GoodsController;
import com.example.orionsupplychain.controller.SupplyOrderController;
import com.example.orionsupplychain.model.Customer;
import com.example.orionsupplychain.model.Food;
import com.example.orionsupplychain.model.Goods;
import com.example.orionsupplychain.model.Item;
import com.example.orionsupplychain.model.Product;
import com.example.orionsupplychain.model.SupplyOrder;
import com.example.orionsupplychain.persistence.CustomerDAO;
import com.example.orionsupplychain.persistence.FoodDAO;
import com.example.orionsupplychain.persistence.GoodsDAO;
import com.example.orionsupplychain.persistence.SupplyOrderDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SupplyOrderFragment extends Fragment {
    private View view;
    private EditText editTextSupplyOrderId;
    private Spinner spinnerSupplyOrderCustomer;
    private Spinner spinnerSupplyOrderProduct;
    private EditText editTextSupplyOrderQuantity;
    private TextView textViewSupplyOrderTotalOutput;
    private CustomerController customerController;
    private FoodController foodController;
    private GoodsController goodsController;
    private SupplyOrderController supplyOrderController;
    private List<Customer> customers;
    private List<Product> products;
    private SupplyOrder supplyOrder;

    public SupplyOrderFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_supply_order, container, false);

        editTextSupplyOrderId = view.findViewById(R.id.editTextSupplyOrderId);
        spinnerSupplyOrderCustomer = view.findViewById(R.id.spinnerSupplyOrderCustomer);
        spinnerSupplyOrderProduct = view.findViewById(R.id.spinnerSupplyOrderProduct);
        editTextSupplyOrderQuantity = view.findViewById(R.id.editTextSupplyOrderQuantity);
        textViewSupplyOrderTotalOutput = view.findViewById(R.id.textViewSupplyOrderTotalOutput);
        textViewSupplyOrderTotalOutput.setMovementMethod(new ScrollingMovementMethod());

        customerController = new CustomerController(new CustomerDAO(this.getContext()));
        foodController = new FoodController(new FoodDAO(this.getContext()));
        goodsController = new GoodsController(new GoodsDAO(this.getContext()));
        supplyOrderController = new SupplyOrderController(new SupplyOrderDAO(this.getContext()));

        loadSpinnerCustomer();
        loadSpinnerProduct();

        view.findViewById(R.id.buttonSupplyOrderNewOrder).setOnClickListener(createSupplyOrder -> createSupplyOrder());
        view.findViewById(R.id.buttonSupplyOrderAdd).setOnClickListener(addItem -> addItem());
        view.findViewById(R.id.buttonSupplyOrderClear).setOnClickListener(clearSupplyOrder -> clearSupplyOrder());
        view.findViewById(R.id.buttonSupplyOrderFinish).setOnClickListener(register -> registerEntry());

        return view;
    }

    private void loadSpinnerCustomer() {
        Customer customer0 = new Customer(0, null, "Select customer", null, null);
        List<Customer> customerSelector = new ArrayList<>();

        try {
            customers = customerController.listEntry();

            customerSelector.add(0, customer0);
            customerSelector.addAll(customers);

            ArrayAdapter<Customer> arrayAdapter = new ArrayAdapter<>(
                    view.getContext(), android.R.layout.simple_spinner_item, customerSelector);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSupplyOrderCustomer.setAdapter(arrayAdapter);

        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadSpinnerProduct() {
        Food food0 = new Food(0, "Select product", 0, null);
        List<Product> productSelector = new ArrayList<>();

        try {
            products = new ArrayList<>();
            products.addAll(foodController.listEntry());
            products.addAll(goodsController.listEntry());

            productSelector.add(food0);
            productSelector.addAll(products);

            ArrayAdapter<Product> arrayAdapter = new ArrayAdapter<>(
                    view.getContext(), android.R.layout.simple_spinner_item, productSelector);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSupplyOrderProduct.setAdapter(arrayAdapter);

        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void createSupplyOrder() {
        if ((editTextSupplyOrderId.length() == 0) ||
                (spinnerSupplyOrderCustomer.getSelectedItemPosition() == 0)) {
            Toast.makeText(view.getContext(), "Invalid input", Toast.LENGTH_LONG).show();
            return;
        }

        supplyOrder = new SupplyOrder(
                Integer.parseInt(editTextSupplyOrderId.getText().toString()),
                (Customer) spinnerSupplyOrderCustomer.getSelectedItem(),
                LocalDate.now()
        );

        Toast.makeText(view.getContext(), "New order created", Toast.LENGTH_LONG).show();

        disableFields();
    }

    private void addItem() {
        if (supplyOrder == null) {
            Toast.makeText(view.getContext(), "No order in progress", Toast.LENGTH_LONG).show();
            return;
        }

        if ((spinnerSupplyOrderProduct.getSelectedItemPosition() == 0) ||
                (editTextSupplyOrderQuantity.length() == 0)) {
            Toast.makeText(view.getContext(), "Invalid input", Toast.LENGTH_LONG).show();
            return;
        }

        supplyOrder.addItem(new Item(
                supplyOrder.getSupplyOrderId(),
                getProductValues(),
                Integer.parseInt(editTextSupplyOrderQuantity.getText().toString()))
        );

        Toast.makeText(view.getContext(), "Item added", Toast.LENGTH_LONG).show();

        textViewSupplyOrderTotalOutput.setText(String.format("$ %s", supplyOrder.getSupplyOrderTotal()));

        clearSupplyOrderValues(false);
    }

    private void clearSupplyOrder() {
        if (supplyOrder == null) {
            Toast.makeText(view.getContext(), "No order in progress", Toast.LENGTH_LONG).show();
            return;
        }

        supplyOrder = null;

        enableFields();

        clearSupplyOrderValues(true);
    }

    private void registerEntry() {
        if (supplyOrder == null) {
            Toast.makeText(view.getContext(), "No order in progress", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            supplyOrderController.registerEntry(supplyOrder);

            Toast.makeText(view.getContext(), "Order send to supplier", Toast.LENGTH_LONG).show();

        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearSupplyOrder();
    }

    private Product getProductValues() {
        if (spinnerSupplyOrderProduct.getSelectedItem() instanceof Food) {
            return (Food) spinnerSupplyOrderProduct.getSelectedItem();
        } else {
            return (Goods) spinnerSupplyOrderProduct.getSelectedItem();
        }
    }

    private void clearSupplyOrderValues(boolean allFields) {
        if (allFields) {
            editTextSupplyOrderId.setText("");
            spinnerSupplyOrderCustomer.setSelection(0);
            textViewSupplyOrderTotalOutput.setText("");
        }

        spinnerSupplyOrderProduct.setSelection(0);
        editTextSupplyOrderQuantity.setText("");
    }

    private void disableFields() {
        editTextSupplyOrderId.setEnabled(false);
        spinnerSupplyOrderCustomer.setEnabled(false);
    }

    private void enableFields() {
        editTextSupplyOrderId.setEnabled(true);
        spinnerSupplyOrderCustomer.setEnabled(true);
    }
}