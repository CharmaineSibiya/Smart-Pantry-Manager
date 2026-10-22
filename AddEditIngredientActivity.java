package com.smartpantry.manager.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.smartpantry.manager.R;
import com.smartpantry.manager.database.DatabaseHelper;
import com.smartpantry.manager.models.Ingredient;

/**
 * Activity for adding a new ingredient or editing an existing one.
 * Includes input validation and full CRUD (create / update / delete).
 */
public class AddEditIngredientActivity extends AppCompatActivity {

    private TextInputLayout tilName, tilQuantity, tilUnit, tilExpiry;
    private TextInputEditText etName, etQuantity, etUnit, etExpiry;
    private MaterialButton btnSave, btnCancel, btnDelete;

    private DatabaseHelper dbHelper;
    private long ingredientId = -1; // -1 means new ingredient
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);

        tilName = findViewById(R.id.til_name);
        tilQuantity = findViewById(R.id.til_quantity);
        tilUnit = findViewById(R.id.til_unit);
        tilExpiry = findViewById(R.id.til_expiry);
        etName = findViewById(R.id.et_name);
        etQuantity = findViewById(R.id.et_quantity);
        etUnit = findViewById(R.id.et_unit);
        etExpiry = findViewById(R.id.et_expiry);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        btnDelete = findViewById(R.id.btn_delete);

        // Check if we are editing an existing ingredient
        if (getIntent().hasExtra("ingredient_id")) {
            ingredientId = getIntent().getLongExtra("ingredient_id", -1);
            if (ingredientId != -1) {
                isEditMode = true;
                loadIngredient(ingredientId);
                btnDelete.setVisibility(View.VISIBLE);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(R.string.edit_ingredient);
                }
                // Also update the form title TextView if present
                android.widget.TextView tvTitle = findViewById(R.id.tv_form_title);
                if (tvTitle != null) {
                    tvTitle.setText(R.string.edit_ingredient);
                }
            }
        }

        btnSave.setOnClickListener(v -> saveIngredient());
        btnCancel.setOnClickListener(v -> finish());
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void loadIngredient(long id) {
        Ingredient ingredient = dbHelper.getIngredientById(id);
        if (ingredient != null) {
            etName.setText(ingredient.getName());
            // Format quantity without unnecessary decimals
            double qty = ingredient.getQuantity();
            if (qty == (long) qty) {
                etQuantity.setText(String.valueOf((long) qty));
            } else {
                etQuantity.setText(String.valueOf(qty));
            }
            etUnit.setText(ingredient.getUnit());
            if (ingredient.getExpiryDate() != null) {
                etExpiry.setText(ingredient.getExpiryDate());
            }
        }
    }

    private void saveIngredient() {
        // Clear previous errors
        tilName.setError(null);
        tilQuantity.setError(null);
        tilUnit.setError(null);

        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String qtyStr = etQuantity.getText() != null ? etQuantity.getText().toString().trim() : "";
        String unit = etUnit.getText() != null ? etUnit.getText().toString().trim() : "";
        String expiry = etExpiry.getText() != null ? etExpiry.getText().toString().trim() : "";

        boolean valid = true;

        if (name.isEmpty()) {
            tilName.setError(getString(R.string.validation_name));
            valid = false;
        }

        double quantity = 0;
        if (qtyStr.isEmpty()) {
            tilQuantity.setError(getString(R.string.validation_quantity));
            valid = false;
        } else {
            try {
                quantity = Double.parseDouble(qtyStr);
                if (quantity <= 0) {
                    tilQuantity.setError(getString(R.string.validation_quantity));
                    valid = false;
                }
            } catch (NumberFormatException e) {
                tilQuantity.setError(getString(R.string.validation_quantity));
                valid = false;
            }
        }

        if (unit.isEmpty()) {
            tilUnit.setError(getString(R.string.validation_unit));
            valid = false;
        }

        if (!valid) {
            return;
        }

        // Optional expiry validation (basic format check)
        if (!expiry.isEmpty() && !expiry.matches("\\d{4}-\\d{2}-\\d{2}")) {
            tilExpiry.setError("Use format YYYY-MM-DD");
            return;
        }

        Ingredient ingredient = new Ingredient(name, quantity, unit, expiry.isEmpty() ? null : expiry);

        if (isEditMode) {
            ingredient.setId(ingredientId);
            dbHelper.updateIngredient(ingredient);
        } else {
            dbHelper.addIngredient(ingredient);
        }

        Toast.makeText(this, R.string.ingredient_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete)
                .setMessage(R.string.confirm_delete)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    dbHelper.deleteIngredient(ingredientId);
                    Toast.makeText(this, R.string.ingredient_deleted, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
