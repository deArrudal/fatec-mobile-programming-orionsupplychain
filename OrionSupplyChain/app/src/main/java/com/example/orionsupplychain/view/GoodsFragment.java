package com.example.orionsupplychain.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orionsupplychain.R;
import com.example.orionsupplychain.controller.GoodsController;
import com.example.orionsupplychain.model.Goods;
import com.example.orionsupplychain.persistence.GoodsDAO;

import java.sql.SQLException;
import java.util.List;

public class GoodsFragment extends Fragment {
    private View view;
    private EditText editTextGoodsId;
    private EditText editTextGoodsName;
    private EditText editTextGoodsPrice;
    private EditText editTextGoodsCategory;
    private CheckBox checkBoxGoodsIsBuild;
    private TextView textViewGoodsOutput;
    private GoodsController goodsController;

    public GoodsFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_goods, container, false);

        editTextGoodsId = view.findViewById(R.id.editTextGoodsId);
        editTextGoodsName = view.findViewById(R.id.editTextGoodsName);
        editTextGoodsPrice = view.findViewById(R.id.editTextGoodsPrice);
        editTextGoodsCategory = view.findViewById(R.id.editTextGoodsCategory);
        checkBoxGoodsIsBuild = view.findViewById(R.id.checkBoxGoodsIsBuild);
        textViewGoodsOutput = view.findViewById(R.id.textViewGoodsOutput);
        textViewGoodsOutput.setMovementMethod(new ScrollingMovementMethod());

        goodsController = new GoodsController(new GoodsDAO(view.getContext()));

        view.findViewById(R.id.buttonGoodsSearch).setOnClickListener(search -> searchEntry());
        view.findViewById(R.id.buttonGoodsRegister).setOnClickListener(register -> registerEntry());
        view.findViewById(R.id.buttonGoodsUpdate).setOnClickListener(update -> updateEntry());
        view.findViewById(R.id.buttonGoodsRemove).setOnClickListener(remove -> removeEntry());
        view.findViewById(R.id.buttonGoodsList).setOnClickListener(list -> listEntry());

        return view;
    }

    private void searchEntry() {
        Goods goods;

        try {
            if (editTextGoodsId.length() == 0) {
                throw new IllegalArgumentException("Invalid input");
            }

            goods = goodsController.searchEntry(new Goods(
                    Integer.parseInt(editTextGoodsId.getText().toString()),
                    null, 0, null, false));

            if (goods.getProductName() != null) {
                setGoodsValues(goods);

            } else {
                Toast.makeText(
                        view.getContext(), "Goods not found", Toast.LENGTH_LONG).show();

                clearGoodsValues();
            }
        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void registerEntry() {
        try {
            goodsController.registerEntry(getGoodsValues());

            Toast.makeText(
                    view.getContext(), "Goods registered successfully", Toast.LENGTH_LONG).show();

        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearGoodsValues();
    }

    private void updateEntry() {
        try {
            goodsController.updateEntry(getGoodsValues());

            Toast.makeText(
                    view.getContext(), "Goods updated successfully", Toast.LENGTH_LONG).show();

        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearGoodsValues();
    }

    private void removeEntry() {
        try {
            if (editTextGoodsId.length() == 0) {
                throw new IllegalArgumentException("Invalid input");
            }

            goodsController.removeEntry(new Goods(
                    Integer.parseInt(editTextGoodsId.getText().toString()),
                    null, 0, null, false));

            Toast.makeText(
                    view.getContext(), "Goods removed successfully", Toast.LENGTH_LONG).show();

        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearGoodsValues();
    }

    private void listEntry() {
        try {
            List<Goods> goodsList = goodsController.listEntry();

            StringBuilder stringBuffer = new StringBuilder();

            for (Goods goods : goodsList) {
                stringBuffer.append(goods.toString()).append("\n");
            }

            textViewGoodsOutput.setText(stringBuffer.toString());

        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearGoodsValues();
    }

    private Goods getGoodsValues() throws IllegalArgumentException {
        if (!isGoodsValuesValid()) {
            throw new IllegalArgumentException("Invalid input");
        }

        return new Goods(
                Integer.parseInt(editTextGoodsId.getText().toString()),
                editTextGoodsName.getText().toString(),
                Double.parseDouble(editTextGoodsPrice.getText().toString()),
                editTextGoodsCategory.getText().toString(),
                checkBoxGoodsIsBuild.isChecked()
        );
    }

    private boolean isGoodsValuesValid() {
        if (editTextGoodsId.length() == 0) {
            return false;
        }

        if (editTextGoodsName.length() == 0) {
            return false;
        }

        if (editTextGoodsPrice.length() == 0) {
            return false;
        }

        if (editTextGoodsCategory.length() == 0) {
            return false;
        }

        return true;
    }

    private void setGoodsValues(Goods goods) {
        editTextGoodsId.setText(String.valueOf(goods.getProductId()));
        editTextGoodsName.setText(goods.getProductName());
        editTextGoodsPrice.setText(String.valueOf(goods.getProductPrice()));
        editTextGoodsCategory.setText(goods.getGoodsCategory());
        checkBoxGoodsIsBuild.setChecked(goods.getGoodsIsBuild());
    }

    private void clearGoodsValues() {
        editTextGoodsId.setText("");
        editTextGoodsName.setText("");
        editTextGoodsPrice.setText("");
        editTextGoodsCategory.setText("");
        checkBoxGoodsIsBuild.setChecked(false);
    }
}