package com.example.task1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddExpenseDialogFragment extends DialogFragment {

    public interface AddExpenseListener {
        void onExpenseAdded(String name, String amount, String category, String date);
    }

    private AddExpenseListener listener;
    private EditText expenseName;
    private EditText expenseAmount;
    private Spinner categorySpinner;
    private Button dateButton;
    private String selectedDate;

    public AddExpenseDialogFragment(AddExpenseListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_expense, container, false);

        expenseName = view.findViewById(R.id.expenseName);
        expenseAmount = view.findViewById(R.id.expenseAmount);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        dateButton = view.findViewById(R.id.dateButton);
        Button btnAddExpense = view.findViewById(R.id.btnAddExpense);

        // Initialize categories
        String[] categories = {"Food", "Transport", "Entertainment", "Utilities"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Set default date
        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        dateButton.setText(selectedDate);

        // Date picker dialog
        dateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view1, year1, month1, dayOfMonth) -> {
                Calendar selectedDateCalendar = Calendar.getInstance();
                selectedDateCalendar.set(year1, month1, dayOfMonth);
                selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDateCalendar.getTime());
                dateButton.setText(selectedDate);
            }, year, month, day);

            datePickerDialog.show();
        });

        // Save button click listener
        btnAddExpense.setOnClickListener(v -> {
            String name = expenseName.getText().toString().trim();
            String amount = expenseAmount.getText().toString().trim();
            String category = categorySpinner.getSelectedItem().toString();

            if (!name.isEmpty() && !amount.isEmpty() && selectedDate != null) {
                if (listener != null) {
                    listener.onExpenseAdded(name, amount, category, selectedDate);
                }
                dismiss();
            }
        });

        return view;
    }
}
