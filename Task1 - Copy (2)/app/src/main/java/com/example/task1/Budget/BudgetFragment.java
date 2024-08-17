package com.example.task1.Budget;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.task1.Budget.CategoryAdapter;
import com.example.task1.Database.DatabaseHelper;
import com.example.task1.Model.Category;
import com.example.task1.Model.User;
import com.example.task1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class BudgetFragment extends Fragment {

    private FloatingActionButton fabAddCategory;
    private ListView listViewCategories;
    private DatabaseHelper databaseHelper;
    private int userId; // Đảm bảo bạn đã truyền userId từ activity hoặc fragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        fabAddCategory = view.findViewById(R.id.fabAddCategory);
        listViewCategories = view.findViewById(R.id.expensesList);

        databaseHelper = new DatabaseHelper(getActivity());
        setUserIdFromSession();
        // Kiểm tra và hiển thị danh mục từ cơ sở dữ liệu
        loadCategories();

        fabAddCategory.setOnClickListener(v -> showAddCategoryDialog());

        return view;
    }

    private void showAddCategoryDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_category, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        EditText categoryNameEditText = dialogView.findViewById(R.id.categoryNameEditText);
        EditText categoryBudgetEditText = dialogView.findViewById(R.id.categoryBudgetEditText);
        Button btnSaveCategory = dialogView.findViewById(R.id.btnSaveCategory);

        btnSaveCategory.setOnClickListener(v -> {
            String categoryName = categoryNameEditText.getText().toString();
            String categoryBudget = categoryBudgetEditText.getText().toString();

            if (!categoryName.isEmpty() && !categoryBudget.isEmpty()) {
                double budget = Double.parseDouble(categoryBudget);
                databaseHelper.addCategory(categoryName, budget, userId);
                loadCategories(); // Load categories after adding a new one
                dialog.dismiss();
            } else {
                Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategories() {
        List<Category> categories = databaseHelper.getCategories(userId);
        CategoryAdapter adapter = new CategoryAdapter(getActivity(), categories);
        listViewCategories.setAdapter(adapter);
    }
    private void setUserIdFromSession() {
        User sessionUser = databaseHelper.getSessionUser();
        if (sessionUser != null) {
            this.userId = sessionUser.getId(); // Assuming getId() is a method in User class
        }
    }
}
