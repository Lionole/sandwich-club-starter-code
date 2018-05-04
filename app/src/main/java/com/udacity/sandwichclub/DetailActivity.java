package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @BindView(R.id.origin_tv) TextView placeOfOriginTextView;
    @BindView(R.id.also_known_tv) TextView alsoKnownTextView;
    @BindView(R.id.ingredients_tv) TextView ingredientsTextView;
    @BindView(R.id.description_tv) TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            //closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        if (!sandwich.getPlaceOfOrigin().isEmpty()) {
            placeOfOriginTextView.setText(sandwich.getPlaceOfOrigin());
        } else {
            placeOfOriginTextView.setVisibility(View.GONE);
        }
        List<String> alsoKnownAsList = sandwich.getAlsoKnownAs();
        if (!alsoKnownAsList.isEmpty()) {
            for (int i = 0; i < alsoKnownAsList.size(); i++) {
                if (i > 0) { alsoKnownTextView.append("\n"); }
                alsoKnownTextView.append(alsoKnownAsList.get(i));
            }
        } else {
            alsoKnownTextView.setVisibility(View.GONE);
        }
        List<String> ingredientsList = sandwich.getIngredients();
        for (int i = 0; i < ingredientsList.size(); i++) {
            if (i > 0) { ingredientsTextView.append("\n"); }
            ingredientsTextView.append(ingredientsList.get(i));
        }
        descriptionTextView.setText(getString(R.string.detail_description_content, sandwich.getDescription()));
    }
}