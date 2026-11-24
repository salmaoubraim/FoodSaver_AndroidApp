package com.foodsaver.android.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsaver.android.R;
import com.foodsaver.android.auth.SessionManager;
import com.foodsaver.android.data.User;
import com.foodsaver.android.data.UserRepository;
import com.foodsaver.android.ui.BaseActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class UserManagementActivity extends BaseActivity {

    private ProgressBar progressBar;
    private TextView    tvEmpty;
    private UserAdapter adapter;
    private final List<User> users = new ArrayList<>();
    private UserRepository userRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.users_management);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.recyclerUsers);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty     = findViewById(R.id.tvEmpty);
        userRepo    = new UserRepository(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter();
        recyclerView.setAdapter(adapter);

        loadUsers();
    }

    private void loadUsers() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
        users.clear();
        users.addAll(userRepo.getAllUsers());
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(users.isEmpty() ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void showEditRoleDialog(User user) {
        String[] roles = {"client", "merchant", "admin"};
        int selectedIndex = 0;
        for (int i = 0; i < roles.length; i++) {
            if (roles[i].equals(user.role)) { selectedIndex = i; break; }
        }
        final int[] selection = {selectedIndex};
        new AlertDialog.Builder(this)
                .setTitle("Modifier le rôle")
                .setSingleChoiceItems(roles, selectedIndex, (d, w) -> selection[0] = w)
                .setPositiveButton("Enregistrer", (d, w) -> {
                    user.role = roles[selection[0]];
                    userRepo.updateUser(user);
                    Toast.makeText(this, "Rôle mis à jour", Toast.LENGTH_SHORT).show();
                    loadUsers();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void confirmDeleteUser(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer l'utilisateur")
                .setMessage("Voulez-vous vraiment supprimer " + user.email + " ?")
                .setPositiveButton("Supprimer", (d, w) -> {
                    // ✅ getUserId() retourne String — comparaison avec .equals()
                    String currentId = new SessionManager(this).getUserId();
                    if (user.id.equals(currentId)) {
                        new SessionManager(this).logout();
                        finish();
                        return;
                    }
                    // ✅ deleteUser prend String
                    userRepo.deleteUser(user.id);
                    Toast.makeText(this, "Utilisateur supprimé", Toast.LENGTH_SHORT).show();
                    loadUsers();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
        @NonNull @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_admin, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            User user = users.get(position);
            holder.tvName.setText(user.name);
            holder.tvEmail.setText(user.email);
            holder.tvRole.setText(user.role);
            holder.btnEdit.setOnClickListener(v -> showEditRoleDialog(user));
            holder.btnDelete.setOnClickListener(v -> confirmDeleteUser(user));
        }

        @Override public int getItemCount() { return users.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvEmail, tvRole;
            MaterialButton btnEdit, btnDelete;
            ViewHolder(View v) {
                super(v);
                tvName   = v.findViewById(R.id.tvUserName);
                tvEmail  = v.findViewById(R.id.tvUserEmail);
                tvRole   = v.findViewById(R.id.tvUserRole);
                btnEdit  = v.findViewById(R.id.btnEditRole);
                btnDelete = v.findViewById(R.id.btnDeleteUser);
            }
        }
    }
}