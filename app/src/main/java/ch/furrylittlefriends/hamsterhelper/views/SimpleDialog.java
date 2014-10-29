package ch.furrylittlefriends.hamsterhelper.views;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ch.furrylittlefriends.hamsterhelper.R;
import ch.furrylittlefriends.hamsterhelper.util.ViewIdGenerator;


public class SimpleDialog extends DialogFragment {

    private Builder builder;

    public SimpleDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        builder = (Builder) getArguments().getSerializable("builder");

        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return builder.create(getActivity(), inflater, container, new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        });
    }

    public static class DialogBuilder {

        private final Context context;
        private final FragmentManager fragmentManager;

        private final Builder builder;

        public DialogBuilder(Context context, FragmentManager fragmentManager) {
            this.context = context;
            this.fragmentManager = fragmentManager;
            this.builder = new Builder();
        }

        public DialogBuilder setTitle(String title) {
            this.builder.setTitle(title);
            return this;
        }

        public DialogBuilder setTitle(int titleId) {
            this.builder.setTitle(context.getText(titleId));
            return this;
        }

        public DialogBuilder setMessage(String message) {
            this.builder.setMessage(message);
            return this;
        }

        public DialogBuilder setMessage(int messageId) {
            this.builder.setMessage(context.getText(messageId));
            return this;
        }

        public DialogBuilder setPrimaryButton(CharSequence text, final View.OnClickListener listener) {
            this.builder.setPrimaryButton(text, listener);
            return this;
        }

        public DialogBuilder setSecondaryButton(CharSequence text, final View.OnClickListener listener) {
            this.builder.setSecondaryButton(text, listener);
            return this;
        }

        public DialogBuilder setAlertButton(CharSequence text, final View.OnClickListener listener) {
            this.builder.setAlertButton(text, listener);
            return this;
        }

        public DialogBuilder setPrimaryButton(int textId, final View.OnClickListener listener) {
            this.builder.setPrimaryButton(context.getText(textId), listener);
            return this;
        }

        public DialogBuilder setSecondaryButton(int textId, final View.OnClickListener listener) {
            this.builder.setSecondaryButton(context.getText(textId), listener);
            return this;
        }

        public DialogBuilder setAlertButton(int textId, final View.OnClickListener listener) {
            this.builder.setAlertButton(context.getText(textId), listener);
            return this;
        }

        public DialogFragment show() {

            final SimpleDialog fragment = new SimpleDialog();

            Bundle args = new Bundle();
            args.putSerializable("builder", builder);
            fragment.setArguments(args);

            fragment.show(fragmentManager, "simple_dialog");

            return fragment;
        }
    }

    protected static class Builder implements Serializable {

        private CharSequence title = null;
        private CharSequence message = null;

        private final List<ButtonConfig> buttonConfigs;

        protected Builder() {
            this.buttonConfigs = new ArrayList<ButtonConfig>();
        }

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        public Builder setPrimaryButton(CharSequence text, final View.OnClickListener listener) {
            this.buttonConfigs.add(new ButtonConfig(text, listener, R.layout.view_button_primary));
            return this;
        }

        public Builder setSecondaryButton(CharSequence text, final View.OnClickListener listener) {
            this.buttonConfigs.add(new ButtonConfig(text, listener, R.layout.view_button_secondary));
            return this;
        }

        public Builder setAlertButton(CharSequence text, final View.OnClickListener listener) {
            this.buttonConfigs.add(new ButtonConfig(text, listener, R.layout.view_button_secondary));
            return this;
        }

        public View create(Context context, LayoutInflater inflater, ViewGroup container, final Runnable dismisser) {

            View v = getDialogLayoutAndInitTitle(context, inflater, container);

            LinearLayout content = (LinearLayout) v.findViewById(R.id.dialog_content);

            TextView messageView = (TextView) v.findViewById(R.id.dialog_message);
            if (this.message != null) {
                messageView.setText(this.message);
                if (this.title == null) {
                    messageView.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            } else {
                messageView.setVisibility(View.GONE);
            }

            LinearLayout buttonPanel = (LinearLayout) v.findViewById(R.id.dialog_button_panel);

            for (int i = 0; i < buttonConfigs.size(); i++) {
                final ButtonConfig buttonConfig = buttonConfigs.get(i);

                Button btn = (Button) inflater.inflate(buttonConfig.layout, content, false);
                btn.setId(ViewIdGenerator.generateViewId());
                btn.setText(buttonConfig.text);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (buttonConfig.listener != null) {
                            buttonConfig.listener.onClick(v);
                        }
                        dismisser.run();
                    }
                });

                if (i + 1 < buttonConfigs.size()) {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    lp.setMargins(0, 0, 0, UiUtil.getPaddingInPx(context));
                    btn.setLayoutParams(lp);
                }
                buttonPanel.addView(btn);
            }

            return v;
        }

        private View getDialogLayoutAndInitTitle(Context context, LayoutInflater inflater, ViewGroup container) {

            View v = inflater.inflate(R.layout.simple_dialog, container, false);
            TextView tvTitle = (TextView) v.findViewById(R.id.dialog_title);
            View viewTitleDivider = v.findViewById(R.id.dialog_title_divider);
            if (title != null) {
                tvTitle.setText(title);
                //tvTitle.setTextColor(context.getResources().getColor(R.color.white));
                //FontUtil.setCustomFont(tvTitle, context, FontUtil.MUSEO_SANS_500);
                //viewTitleDivider.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.white)));
            } else {
                tvTitle.setVisibility(View.GONE);
                viewTitleDivider.setVisibility(View.GONE);
            }
            return v;
        }

    }

    private static class ButtonConfig implements Serializable {

        final int layout;
        final CharSequence text;
        final transient View.OnClickListener listener; //note: can be null if serialized, which results in dismissing simple_dialog without any action

        public ButtonConfig(CharSequence text, View.OnClickListener listener, int layout) {
            this.text = text;
            this.listener = listener;
            this.layout = layout;
        }
    }

}
