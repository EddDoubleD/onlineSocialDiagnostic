package ru.hardwork.onlinesocialdiagnosticapp.ui.dialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.base.Optional;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.DiagnosticTest;

/**
 * Universal dialog for creating / editing invitations
 */
public class InviteDialog {
    private static final String DEFAULT_TITLE = "Создание приглашения";
    private static final String DEFAULT_NEGATIVE = "ОТМЕНА";
    private static final String DEFAULT_POSITIVE = "СОЗДАТЬ";

    private final AlertDialog.Builder builder;

    public final MaterialEditText aliasText;
    public final Spinner spinner;



    public InviteDialog(@NonNull View view, @Nullable String title, @Nullable String negative) {
        aliasText = view.findViewById(R.id.aliasText);

        List<String> data = new ArrayList<>();
        for (DiagnosticTest diagnostic : Common.diagnosticTests) data.add(diagnostic.getName());

        spinner = view.findViewById(R.id.diagnosticSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        builder = new AlertDialog.Builder(view.getContext(), R.style.DialogTheme);
        builder.setTitle(Optional.fromNullable(title).or(DEFAULT_TITLE));
        builder.setNegativeButton(Optional.fromNullable(negative).or(DEFAULT_NEGATIVE), (dialogInterface, i) -> dialogInterface.dismiss());

        builder.setView(view);
    }

    public InviteDialog setPositiveButtonListener( @Nullable String positive, @NonNull DialogInterface.OnClickListener listener) {
        builder.setPositiveButton(Optional.fromNullable(positive).or(DEFAULT_POSITIVE), listener);
        return this;
    }

    public void show() {
        builder.show();
    }


    public boolean validate() {
        return StringUtils.isNotBlank(aliasText.getText());
    }
}
