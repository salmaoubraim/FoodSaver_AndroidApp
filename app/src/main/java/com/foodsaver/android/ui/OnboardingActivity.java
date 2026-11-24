package com.foodsaver.android.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.foodsaver.android.ui.BaseActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.foodsaver.android.R;
import com.foodsaver.android.ui.auth.LoginActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends BaseActivity {

    private static final String PREFS_APP = "FoodSaverApp";
    private static final String KEY_ONBOARDING_DONE = "onboarding_done";

    private ViewPager2 viewPager;
    private LinearLayout dotsContainer;
    private MaterialButton btnNext;
    private final List<SlideItem> slides = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        slides.add(new SlideItem(
            getString(R.string.onboarding_slide1_title),
            getString(R.string.onboarding_slide1_desc),
            R.drawable.bg_onboarding_slide1,
            false
        ));
        slides.add(new SlideItem(
            getString(R.string.onboarding_slide2_title),
            getString(R.string.onboarding_slide2_desc),
            R.drawable.bg_onboarding_slide2,
            false
        ));
        slides.add(new SlideItem(
            getString(R.string.onboarding_slide3_title),
            getString(R.string.onboarding_slide3_desc),
            R.drawable.bg_onboarding_slide3,
            true
        ));

        viewPager = findViewById(R.id.viewPagerOnboarding);
        dotsContainer = (LinearLayout) findViewById(R.id.dotsContainer);
        btnNext = findViewById(R.id.btnNext);
        MaterialButton btnSkip = findViewById(R.id.btnSkip);

        viewPager.setAdapter(new SlideAdapter(slides));
        addDots(0);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                addDots(position);
                btnNext.setText(position == slides.size() - 1
                    ? R.string.onboarding_start
                    : R.string.onboarding_next);
            }
        });

        btnNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() == slides.size() - 1) {
                getSharedPreferences(PREFS_APP, MODE_PRIVATE)
                    .edit()
                    .putBoolean(KEY_ONBOARDING_DONE, true)
                    .apply();
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }
        });

        btnSkip.setOnClickListener(v -> {
            getSharedPreferences(PREFS_APP, MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_ONBOARDING_DONE, true)
                .apply();
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

    private void addDots(int selectedIndex) {
        dotsContainer.removeAllViews();
        int size = slides.size();
        for (int i = 0; i < size; i++) {
            View dot = LayoutInflater.from(this).inflate(android.R.layout.simple_list_item_1, dotsContainer, false);
            if (dot instanceof TextView) {
                TextView tv = (TextView) dot;
                tv.setText("•");
                tv.setTextSize(24f);
                tv.setTextColor(getColor(i == selectedIndex ? R.color.primary : R.color.text_hint));
                tv.setPadding(8, 0, 8, 0);
            }
            dotsContainer.addView(dot);
        }
    }

    private static class SlideItem {
        final String title;
        final String description;
        final int backgroundRes;
        final boolean orangeBg;

        SlideItem(String title, String description, int backgroundRes, boolean orangeBg) {
            this.title = title;
            this.description = description;
            this.backgroundRes = backgroundRes;
            this.orangeBg = orangeBg;
        }
    }

    private static class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {
        private final List<SlideItem> items;

        SlideAdapter(List<SlideItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SlideViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding_slide, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
            SlideItem item = items.get(position);
            holder.title.setText(item.title);
            holder.description.setText(item.description);
            holder.topBg.setBackgroundResource(item.backgroundRes);
            holder.slideImage.setVisibility(View.GONE);
            holder.slideImage.setBackgroundResource(item.backgroundRes);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class SlideViewHolder extends RecyclerView.ViewHolder {
            TextView title, description;
            View topBg;
            ImageView slideImage;

            SlideViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.slideTitle);
                description = itemView.findViewById(R.id.slideDescription);
                topBg = itemView.findViewById(R.id.slideTopBg);
                slideImage = itemView.findViewById(R.id.slideImage);
            }
        }
    }
}
