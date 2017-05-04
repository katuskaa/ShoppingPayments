package katka.shoppingpayments.helpers;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class DatePickerHelper {
    private Context context;
    private EditText editText;
    private int year;
    private int month;
    private int day;

    public DatePickerHelper(Context context, EditText editText) {
        this.context = context;
        this.editText = editText;
        getCurrentDate();
    }

    public DatePickerDialog getCurrentDate() {
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                year = selectedYear;
                month = selectedMonth;
                day = selectedDay;
                editText.setText(getMonth() + " " + getYear());
            }
        };

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        editText.setText(getMonth() + " " + getYear());
        return new DatePickerDialog(context, dateListener, year, month, day);
    }

    private String getYear() {
        return String.valueOf(year);
    }

    private String getMonth() {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
        String currentMonth = dateFormatSymbols.getShortMonths()[month];
        return currentMonth.substring(0,1).toUpperCase() + currentMonth.substring(1);
    }
}
