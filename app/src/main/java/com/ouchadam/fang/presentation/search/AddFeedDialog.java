package com.ouchadam.fang.presentation.search;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.github.frankiesardo.icepick.annotation.Icicle;
import com.github.frankiesardo.icepick.bundle.Bundles;
import com.ouchadam.fang.debug.ParseHelper;

import java.net.MalformedURLException;
import java.net.URL;

public class AddFeedDialog extends DialogFragment {

    @Icicle
    String currentUrl = "";

    private EditText urlInputView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        urlInputView = createUrlInputView(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundles.restoreInstanceState(this, savedInstanceState);
        if (urlInputView != null && currentUrl != null) {
            urlInputView.setText(currentUrl);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        currentUrl = urlInputView.getText().toString();
        Bundles.saveInstanceState(this, outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Add feed from URL");
        builder.setView(urlInputView);
        builder.setNegativeButton("Cancel", negativeClick);
        builder.setPositiveButton("Add", null);

        return builder.create();
    }

    private final DialogInterface.OnClickListener negativeClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dismiss();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        setPositionListener(positiveClick);
    }

    private void setPositionListener(View.OnClickListener positionListener) {
        AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null) {
            View positionButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
            if (positionButton != null) {
                positionButton.setOnClickListener(positionListener);
            }
        }
    }

    private final View.OnClickListener positiveClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                URL url = new URL(urlInputView.getText().toString());
                getAndAddFeedFromUrl(url);
                dismiss();
            } catch (MalformedURLException e) {
                shakeUrlInput();
                urlInputView.setError("Bad url");
            }
        }
    };

    private void shakeUrlInput() {
        Animation shake = new TranslateAnimation(0, 5, 0, 0);
        shake.setInterpolator(new CycleInterpolator(5));
        shake.setDuration(300);
        urlInputView.startAnimation(shake);
    }

    private void getAndAddFeedFromUrl(URL url) {
        ParseHelper parseHelper = new ParseHelper(getActivity().getContentResolver(), null);
        parseHelper.parse(getActivity(), url.toString());
    }

    private EditText createUrlInputView(Context context) {
        EditText editText = new EditText(context);
        editText.setOnClickListener(onTextListener);
        editText.setHint("Podcast URL");
        return editText;
    }

    private View.OnClickListener onTextListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((EditText) view).setError(null);
        }
    };

}
