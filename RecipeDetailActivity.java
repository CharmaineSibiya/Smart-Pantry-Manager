package com.smartpantry.manager.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.smartpantry.manager.R;
import com.smartpantry.manager.database.DatabaseHelper;
import com.smartpantry.manager.models.Recipe;

/**
 * Displays full details of a selected recipe:
 * name, complete ingredient list with quantities, and preparation method.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvName, tvIngredients, tvMethod, tvCanMake;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.recipe_detail);
        }

        tvName = findViewById(R.id.tv_recipe_name);
        tvIngredients = findViewById(R.id.tv_ingredients_list);
        tvMethod = findViewById(R.id.tv_method);
        tvCanMake = findViewById(R.id.tv_can_make);

        dbHelper = new DatabaseHelper(this);

        long recipeId = getIntent().getLongExtra("recipe_id", -1);
        if (recipeId != -1) {
            loadRecipe(recipeId);
        }
    }

    private void loadRecipe(long id) {
        Recipe recipe = dbHelper.getRecipeById(id);
        if (recipe == null) {
            finish();
            return;
        }

        tvName.setText(recipe.getName());
        tvMethod.setText(recipe.getMethod());

        // Build ingredients list
        StringBuilder sb = new StringBuilder();
        if (recipe.getRequiredIngredients() != null) {
            for (Recipe.RecipeIngredient ri : recipe.getRequiredIngredients()) {
                String qtyStr;
                if (ri.getQuantity() == (long) ri.getQuantity()) {
                    qtyStr = String.valueOf((long) ri.getQuantity());
                } else {
                    qtyStr = String.valueOf(ri.getQuantity());
                }
                sb.append("• ").append(qtyStr).append(" ").append(ri.getUnit())
                        .append(" ").append(ri.getName()).append("\n");
            }
        }
        tvIngredients.setText(sb.toString().trim());

        // Since this is opened from Suggested Recipes, the user can make it
        tvCanMake.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
