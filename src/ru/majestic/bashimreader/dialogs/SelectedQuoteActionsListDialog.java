package ru.majestic.bashimreader.dialogs;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.utils.BufferSaver;
import ru.majestic.bashimreader.utils.Sharer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.widget.Toast;

public class SelectedQuoteActionsListDialog implements DialogInterface.OnClickListener {

	private Quote 	selectedQuote;
	private Dialog 	dialog;
	private Context context;	

	public SelectedQuoteActionsListDialog(final Context context, final Quote selectedQuote) {
		this.context = context;
		this.selectedQuote = selectedQuote;
		this.dialog = initDialog(context);
	}

	private final Dialog initDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.dialog_selected_quote_actions_list_title) + selectedQuote.getId() + "?");
		builder.setItems(initActionsArrayLink(), this);

		return builder.create();
	}

	private final int initActionsArrayLink() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			return R.array.dialog_selected_quote_actions_list_array_honeycomd_and_higher;
		else
			return R.array.dialog_selected_quote_actions_list_array_earler_honeycomd;
	}

	public final void show() {
		dialog.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case 0:
			Sharer.share(context, selectedQuote);
			break;
		case 1:
			BufferSaver.saveInBuffer(context, selectedQuote);
			Toast.makeText(
					context,
					context.getString(R.string.dialog_selected_quote_actions_list_message_begin) + selectedQuote.getId() + " "
							+ context.getString(R.string.dialog_selected_quote_actions_list_message_end), Toast.LENGTH_SHORT).show();
			break;
		}
	}

}
