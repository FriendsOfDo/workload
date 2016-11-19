package de.friendsofdo.workload.android.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.github.iweinzierl.android.utils.UiUtils;

import java.util.List;

import de.friendsofdo.workload.android.R;
import de.friendsofdo.workload.android.api.Address;
import de.friendsofdo.workload.android.api.Workplace;

public class WorkplaceSaveDialog {

    public interface Callback {
        void onSave(Workplace workplace);

        void onCancel();
    }

    private final Context context;
    private final Callback callback;
    private final List<Address> addressOptions;

    private AlertDialog dialog;
    private View contentView;

    public WorkplaceSaveDialog(Context context, Callback callback, List<Address> addressOptions) {
        this.context = context;
        this.callback = callback;
        this.addressOptions = addressOptions;
    }

    public void show() {
        if (dialog == null) {
            dialog = build();
        }

        dialog.show();
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private AlertDialog build() {
        contentView = buildView();

        return new AlertDialog.Builder(context)
                .setCancelable(true)
                .setMessage("New Workplace")
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        callback.onCancel();
                        dialogInterface.dismiss();
                    }
                })
                .setView(contentView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callback.onSave(buildWorkplace());
                    }
                })
                .create();
    }

    private View buildView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.dialog_save_workplace, null, false);

        if (addressOptions != null && !addressOptions.isEmpty()) {
            Address bestMatch = addressOptions.get(0);

            UiUtils.setSafeText(view, R.id.street, bestMatch.getStreet());
            UiUtils.setSafeText(view, R.id.number, bestMatch.getNumber());
            UiUtils.setSafeText(view, R.id.city, bestMatch.getCity());
            UiUtils.setSafeText(view, R.id.postcode, bestMatch.getPostcode());
        }

        return view;
    }

    private Workplace buildWorkplace() {
        String name = UiUtils.getSafeTextOrNull(contentView, R.id.name);
        String street = UiUtils.getSafeTextOrNull(contentView, R.id.street);
        String number = UiUtils.getSafeTextOrNull(contentView, R.id.number);
        String postcode = UiUtils.getSafeTextOrNull(contentView, R.id.postcode);
        String city = UiUtils.getSafeTextOrNull(contentView, R.id.city);

        // TODO Add validation

        return new Workplace.Builder()
                .name(name)
                .street(street + " " + number)
                .postcode(postcode)
                .city(city)
                .build();
    }
}
