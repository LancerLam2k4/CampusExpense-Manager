package com.example.task1.ExpenseManager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.task1.Database.DatabaseHelper;
import com.example.task1.Model.Expense;
import com.example.task1.R;

import java.util.List;

public class ExpenseAdapter extends android.widget.ArrayAdapter<Expense> {

    private Context context;
    private List<Expense> expenseList;
    private DatabaseHelper databaseHelper;

    public ExpenseAdapter(@NonNull Context context, List<Expense> expenseList) {
        super(context, 0, expenseList);
        this.context = context;
        this.expenseList = expenseList;
        this.databaseHelper = new DatabaseHelper(context); // Initialize DatabaseHelper
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_expense, parent, false);
        }

        TextView expenseNameTextView = convertView.findViewById(R.id.expenseName);
        TextView expenseAmountTextView = convertView.findViewById(R.id.expenseAmount);
        TextView expenseCategoryTextView = convertView.findViewById(R.id.expenseCategory);
        TextView expenseDateTextView = convertView.findViewById(R.id.expenseDate);

        Expense expense = getItem(position);

        if (expense != null) {
            expenseNameTextView.setText(expense.getName());
            expenseAmountTextView.setText(String.valueOf(expense.getAmount()));
            expenseDateTextView.setText(expense.getDate());

            // Truy vấn tên danh mục từ categoryId
            String categoryName = databaseHelper.getCategoryNameById(expense.getCategoryId());
            expenseCategoryTextView.setText(categoryName);
        } else {
            Log.e("ExpenseAdapter", "Expense is null");
        }

        return convertView;
    }
}
