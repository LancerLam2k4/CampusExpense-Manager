package com.example.task1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<HomeFragment.Expense> {

    private final LayoutInflater inflater;

    public ExpenseAdapter(Context context, List<HomeFragment.Expense> expenses) {
        super(context, R.layout.list_item_expense, expenses);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_expense, parent, false);
        }

        HomeFragment.Expense expense = getItem(position);

        TextView name = convertView.findViewById(R.id.expenseName);
        TextView amount = convertView.findViewById(R.id.expenseAmount);
        ImageView iconUpdate = convertView.findViewById(R.id.ic_update);
        ImageView iconDelete = convertView.findViewById(R.id.ic_delete);

        if (expense != null) {
            name.setText(expense.getName());
            amount.setText("$" + expense.getAmount());
        }

        // Set onClickListeners for the icons if needed
        iconUpdate.setOnClickListener(v -> {
            // Handle update logic
        });

        iconDelete.setOnClickListener(v -> {
            // Handle delete logic
        });

        return convertView;
    }
}
