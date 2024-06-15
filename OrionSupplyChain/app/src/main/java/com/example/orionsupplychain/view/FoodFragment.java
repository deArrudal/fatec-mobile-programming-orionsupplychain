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
import com.example.orionsupplychain.controller.FoodController;
import com.example.orionsupplychain.model.Food;
import com.example.orionsupplychain.persistence.FoodDAO;

import java.sql.SQLException;
import java.util.List;

public class FoodFragment extends Fragment {
    private View view;
    private EditText editTextFoodId;
    private EditText editTextFoodName;
    private EditText editTextFoodPrice;
    private EditText editTextFoodProducer;
    private TextView textViewFoodOutput;
    private FoodController foodController;

    public FoodFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_food, container, false);

        editTextFoodId = view.findViewById(R.id.editTextFoodId);
        editTextFoodName = view.findViewById(R.id.editTextFoodName);
        editTextFoodPrice = view.findViewById(R.id.editTextFoodPrice);
        editTextFoodProducer = view.findViewById(R.id.editTextFoodProducer);
        textViewFoodOutput = view.findViewById(R.id.textViewFoodOutput);
        textViewFoodOutput.setMovementMethod(new ScrollingMovementMethod());

        foodController = new FoodController(new FoodDAO(view.getContext()));

        view.findViewById(R.id.buttonFoodSearch).setOnClickListener(search -> searchEntry());
        view.findViewById(R.id.buttonFoodRegister).setOnClickListener(register -> registerEntry());
        view.findViewById(R.id.buttonFoodUpdate).setOnClickListener(update -> updateEntry());
        view.findViewById(R.id.buttonFoodRemove).setOnClickListener(remove -> removeEntry());
        view.findViewById(R.id.buttonFoodList).setOnClickListener(list -> listEntry());

        return view;
    }

    private void searchEntry() {
        Food food;

        try {
            if (editTextFoodId.length() == 0) {
                throw new IllegalArgumentException("Invalid input");
            }

            food = foodController.searchEntry(new Food(
                    Integer.parseInt(editTextFoodId.getText().toString()),
                    null, 0, null));

            if (food.getProductName() != null) {
                setFoodValues(food);

            } else {
                Toast.makeText(
                        view.getContext(), "Food not found", Toast.LENGTH_LONG).show();

                clearFoodValues();
            }
        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void registerEntry() {
        try {
            foodController.registerEntry(getFoodValues());

            Toast.makeText(
                    view.getContext(), "Food registered successfully", Toast.LENGTH_LONG).show();

        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearFoodValues();
    }

    private void updateEntry() {
        try {
            foodController.updateEntry(getFoodValues());

            Toast.makeText(
                    view.getContext(), "Food updated successfully", Toast.LENGTH_LONG).show();

        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearFoodValues();
    }

    private void removeEntry() {
        try {
            if (editTextFoodId.length() == 0) {
                throw new IllegalArgumentException("Invalid input");
            }

            foodController.removeEntry(new Food(
                    Integer.parseInt(editTextFoodId.getText().toString()),
                    null, 0, null));

            Toast.makeText(
                    view.getContext(), "Food removed successfully", Toast.LENGTH_LONG).show();

        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearFoodValues();
    }

    private void listEntry() {
        try {
            List<Food> foods = foodController.listEntry();

            StringBuilder stringBuffer = new StringBuilder();

            for (Food food : foods) {
                stringBuffer.append(food.toString()).append("\n");
            }

            textViewFoodOutput.setText(stringBuffer.toString());

        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearFoodValues();
    }

    private Food getFoodValues() throws IllegalArgumentException {
        if (!isFoodValuesValid()) {
            throw new IllegalArgumentException("Invalid input");
        }

        return new Food(
                Integer.parseInt(editTextFoodId.getText().toString()),
                editTextFoodName.getText().toString(),
                Double.parseDouble(editTextFoodPrice.getText().toString()),
                editTextFoodProducer.getText().toString()
        );
    }

    private boolean isFoodValuesValid() {
        if (editTextFoodId.length() == 0) {
            return false;
        }

        if (editTextFoodName.length() == 0) {
            return false;
        }

        if (editTextFoodPrice.length() == 0) {
            return false;
        }

        if (editTextFoodProducer.length() == 0) {
            return false;
        }

        return true;
    }

    private void setFoodValues(Food Food) {
        editTextFoodId.setText(String.valueOf(Food.getProductId()));
        editTextFoodName.setText(Food.getProductName());
        editTextFoodPrice.setText(String.valueOf(Food.getProductPrice()));
        editTextFoodProducer.setText(Food.getFoodProducer());
    }

    private void clearFoodValues() {
        editTextFoodId.setText("");
        editTextFoodName.setText("");
        editTextFoodPrice.setText("");
        editTextFoodProducer.setText("");
    }
}