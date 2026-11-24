package com.foodsaver.android.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsaver.android.R;
import com.google.android.material.appbar.MaterialToolbar;

public class NotificationsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        RecyclerView recycler = findViewById(R.id.recyclerNotifications);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        NotificationsAdapter adapter = new NotificationsAdapter(n -> {
            // Marquer comme lue quand l'utilisateur clique
            viewModel.markRead(n.id);
        });
        recycler.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        viewModel.getNotifications().observe(this, adapter::submitList);
        viewModel.markAllRead();
    }

    private NotificationsViewModel viewModel;
}

