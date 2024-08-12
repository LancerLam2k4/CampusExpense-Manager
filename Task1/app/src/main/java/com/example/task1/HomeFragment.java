package com.example.task1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements AddExpenseDialogFragment.AddExpenseListener {

    private TextView totalExpensesValue;
    private ListView expensesList;
    private FloatingActionButton fabAddExpense;
    private ArrayAdapter<Expense> adapter;
    private List<Expense> expenses = new ArrayList<>();

    public static class Expense {
        private String name;
        private String amount;
        private String category;
        private String date;

        public Expense(String name, String amount, String category, String date) {
            this.name = name;
            this.amount = amount;
            this.category = category;
            this.date = date;
        }

        public String getName() { return name; }
        public String getAmount() { return amount; }
        public String getCategory() { return category; }
        public String getDate() { return date; }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        totalExpensesValue = view.findViewById(R.id.totalExpensesValue);
        expensesList = view.findViewById(R.id.expensesList);
        fabAddExpense = view.findViewById(R.id.fabAddExpense);

        adapter = new ExpenseAdapter(getContext(), expenses);
        expensesList.setAdapter(adapter);

        fabAddExpense.setOnClickListener(v -> {
            AddExpenseDialogFragment dialog = new AddExpenseDialogFragment(this);
            dialog.show(getChildFragmentManager(), "AddExpenseDialog");
        });

        return view;
    }

    @Override
    public void onExpenseAdded(String name, String amount, String category, String date) {
        Expense newExpense = new Expense(name, amount, category, date);
        expenses.add(newExpense);
        adapter.notifyDataSetChanged();
        // Update total expenses logic if needed
    }
}
