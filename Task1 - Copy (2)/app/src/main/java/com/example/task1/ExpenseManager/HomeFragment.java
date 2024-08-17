package com.example.task1.ExpenseManager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.task1.Database.DatabaseHelper;
import com.example.task1.Model.Expense;
import com.example.task1.Model.User;
import com.example.task1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FloatingActionButton fabAddExpense;
    private ListView listViewExpenses;
    private DatabaseHelper databaseHelper;
    private int userId;
    private String selectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        fabAddExpense = view.findViewById(R.id.fabAddExpense);
        listViewExpenses = view.findViewById(R.id.expensesList);

        databaseHelper = new DatabaseHelper(getActivity());
        setUserIdFromSession();
        loadExpenses();

        fabAddExpense.setOnClickListener(v -> showAddExpenseDialog());

        return view;
    }

    private void showAddExpenseDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_expense, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        EditText expenseNameEditText = dialogView.findViewById(R.id.expenseName);
        EditText amountEditText = dialogView.findViewById(R.id.expenseAmount);
        Spinner categorySpinner = dialogView.findViewById(R.id.categorySpinner);
        Button btnSelectDate = dialogView.findViewById(R.id.btnSelectDate);
        Button btnSaveExpense = dialogView.findViewById(R.id.btnAddExpense);

        // Set current date on the Button
        selectedDate = getCurrentDate();
        btnSelectDate.setText(selectedDate);

        // Load categories into spinner
        loadCategoriesIntoSpinner(categorySpinner);

        btnSelectDate.setOnClickListener(v -> showDatePickerDialog(btnSelectDate));

        btnSaveExpense.setOnClickListener(v -> {
            String expenseName = expenseNameEditText.getText().toString();
            String amountStr = amountEditText.getText().toString();
            String categoryName = categorySpinner.getSelectedItem().toString();

            if (!expenseName.isEmpty() && !amountStr.isEmpty()) {
                try {
                    double amount = Double.parseDouble(amountStr);
                    int categoryId = getCategoryIdByName(categoryName);
                    double totalExpenses = databaseHelper.getTotalExpensesByCategoryId(categoryId);
                    double budget = databaseHelper.getCategoryAmount(categoryId);

                    if (totalExpenses + amount > budget) {
                        Toast.makeText(getActivity(), "Exceeds budget limit!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Add expense to database
                        databaseHelper.addExpense(new Expense(userId, expenseName, amount, categoryId, selectedDate));
                        loadExpenses();
                        dialog.dismiss();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Invalid amount format", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePickerDialog(Button dateButton) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, year1, month1, dayOfMonth) -> {
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(year1, month1, dayOfMonth);
            selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedCalendar.getTime());
            dateButton.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void loadCategoriesIntoSpinner(Spinner spinner) {
        List<String> categoryNames = databaseHelper.getAllCategoryNames(userId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                categoryNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void loadExpenses() {
        List<Expense> expenses = databaseHelper.getExpensesByUser(userId);
        ExpenseAdapter adapter = new ExpenseAdapter(getActivity(), expenses);
        listViewExpenses.setAdapter(adapter);
    }

    private void setUserIdFromSession() {
        User sessionUser = databaseHelper.getSessionUser();
        if (sessionUser != null) {
            this.userId = sessionUser.getId();
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private int getCategoryIdByName(String categoryName) {
        return databaseHelper.getCategoryIdByName(userId, categoryName);
    }
}
