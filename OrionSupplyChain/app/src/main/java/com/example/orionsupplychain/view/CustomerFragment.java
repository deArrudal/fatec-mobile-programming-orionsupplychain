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
import com.example.orionsupplychain.controller.CustomerController;
import com.example.orionsupplychain.model.Customer;
import com.example.orionsupplychain.persistence.CustomerDAO;

import java.sql.SQLException;
import java.util.List;

public class CustomerFragment extends Fragment {
    private View view;
    private EditText editTextCustomerId;
    private EditText editTextCustomerCNPJ;
    private EditText editTextCustomerName;
    private EditText editTextCustomerAddress;
    private EditText editTextCustomerPhone;
    private TextView textViewCustomerOutput;
    private CustomerController customerController;

    public CustomerFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer, container, false);

        editTextCustomerId = view.findViewById(R.id.editTextCustomerId);
        editTextCustomerCNPJ = view.findViewById(R.id.editTextCustomerCNPJ);
        editTextCustomerName = view.findViewById(R.id.editTextCustomerName);
        editTextCustomerAddress = view.findViewById(R.id.editTextCustomerAddress);
        editTextCustomerPhone = view.findViewById(R.id.editTextCustomerPhone);
        textViewCustomerOutput = view.findViewById(R.id.textViewCustomerOutput);
        textViewCustomerOutput.setMovementMethod(new ScrollingMovementMethod());

        customerController = new CustomerController(new CustomerDAO(view.getContext()));

        view.findViewById(R.id.buttonCustomerSearch).setOnClickListener(search -> searchEntry());
        view.findViewById(R.id.buttonCustomerRegister).setOnClickListener(register -> registerEntry());
        view.findViewById(R.id.buttonCustomerUpdate).setOnClickListener(update -> updateEntry());
        view.findViewById(R.id.buttonCustomerRemove).setOnClickListener(remove -> removeEntry());
        view.findViewById(R.id.buttonCustomerList).setOnClickListener(list -> listEntry());

        return view;
    }

    private void searchEntry() {
        Customer customer;

        try {
            if (editTextCustomerId.length() == 0) {
                throw new IllegalArgumentException("Invalid input");
            }

            customer = customerController.searchEntry(new Customer(
                    Integer.parseInt(editTextCustomerId.getText().toString()),
                    null, null, null, null));

            if (customer.getCustomerName() != null) {
                setCustomerValues(customer);

            } else {
                Toast.makeText(
                        view.getContext(), "Customer not found", Toast.LENGTH_LONG).show();

                clearCustomerValues();
            }
        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void registerEntry() {
        try {
            customerController.registerEntry(getCustomerValues());

            Toast.makeText(
                    view.getContext(), "Customer registered successfully", Toast.LENGTH_LONG).show();

        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearCustomerValues();
    }

    private void updateEntry() {
        try {
            customerController.updateEntry(getCustomerValues());

            Toast.makeText(
                    view.getContext(), "Customer updated successfully", Toast.LENGTH_LONG).show();

        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearCustomerValues();
    }

    private void removeEntry() {
        try {
            if (editTextCustomerId.length() == 0) {
                throw new IllegalArgumentException("Invalid input");
            }

            customerController.removeEntry(new Customer(
                    Integer.parseInt(editTextCustomerId.getText().toString()),
                    null, null, null, null));

            Toast.makeText(
                    view.getContext(), "Customer removed successfully", Toast.LENGTH_LONG).show();

        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearCustomerValues();
    }

    private void listEntry() {
        try {
            List<Customer> customers = customerController.listEntry();

            StringBuilder stringBuffer = new StringBuilder();

            for (Customer customer : customers) {
                stringBuffer.append(customer.toString()).append("\n");
            }

            textViewCustomerOutput.setText(stringBuffer.toString());

        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearCustomerValues();
    }

    private Customer getCustomerValues() throws IllegalArgumentException {
        if (!isCustomerValuesValid()) {
            throw new IllegalArgumentException("Invalid input");
        }

        return new Customer(
                Integer.parseInt(editTextCustomerId.getText().toString()),
                editTextCustomerCNPJ.getText().toString(),
                editTextCustomerName.getText().toString(),
                editTextCustomerAddress.getText().toString(),
                editTextCustomerPhone.getText().toString()
        );
    }

    private boolean isCustomerValuesValid() {
        if (editTextCustomerId.length() == 0) {
            return false;
        }

        if (editTextCustomerCNPJ.length() == 0) {
            return false;
        }

        if (editTextCustomerName.length() == 0) {
            return false;
        }

        if (editTextCustomerAddress.length() == 0) {
            return false;
        }

        if (editTextCustomerPhone.length() == 0) {
            return false;
        }

        return true;
    }

    private void setCustomerValues(Customer customer) {
        editTextCustomerId.setText(String.valueOf(customer.getCustomerId()));
        editTextCustomerCNPJ.setText(customer.getCustomerCNPJ());
        editTextCustomerName.setText(customer.getCustomerName());
        editTextCustomerAddress.setText(customer.getCustomerAddress());
        editTextCustomerPhone.setText(customer.getCustomerPhone());
    }

    private void clearCustomerValues() {
        editTextCustomerId.setText("");
        editTextCustomerCNPJ.setText("");
        editTextCustomerName.setText("");
        editTextCustomerAddress.setText("");
        editTextCustomerPhone.setText("");
    }
}