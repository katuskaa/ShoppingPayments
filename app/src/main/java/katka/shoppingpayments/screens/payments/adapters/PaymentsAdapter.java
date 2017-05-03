package katka.shoppingpayments.screens.payments.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import katka.shoppingpayments.R;
import katka.shoppingpayments.structures.Payment;

public class PaymentsAdapter extends ArrayAdapter<Payment> {
    private Context context;
    private ArrayList<Payment> payments;

    public PaymentsAdapter(Context context, ArrayList<Payment> payments) {
        super(context, R.layout.activity_payments__payment_row, payments);
        this.context = context;
        this.payments = payments;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_payments__payment_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewPrice = (TextView) convertView.findViewById(R.id.activity_payments__payment_row__price);
            viewHolder.textViewShop = (TextView) convertView.findViewById(R.id.activity_payments__payment_row__shop);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Payment payment = payments.get(position);
        viewHolder.textViewPrice.setText(payment.getPrice());
        viewHolder.textViewShop.setText(payment.getShop());

        return convertView;
    }

    static class ViewHolder {
        private TextView textViewPrice;
        private TextView textViewShop;
    }
}
