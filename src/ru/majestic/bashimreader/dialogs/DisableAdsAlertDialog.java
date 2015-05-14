package ru.majestic.bashimreader.dialogs;

import ru.majestic.bashimreader.dialogs.listeners.DialogAnswerListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DisableAdsAlertDialog implements DialogInterface.OnClickListener {

	private AlertDialog dialog;
	
	private DialogAnswerListener dialogAnswerListener;

	public DisableAdsAlertDialog(Context context, DialogAnswerListener dialogAnswerListener) {
		this.dialogAnswerListener = dialogAnswerListener;		
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Надоела реклама?");
		builder.setMessage("Теперь Вы можете её вырубить, чтобы не мешала, проклятая! " +
				"Всего за 30р Вы сможете получать еще больше удовольствия от приложения, ну и поддержите нас финансово. " +
				"Если не выключите сейчас, то это всегда можно сделать в настройках.");
		
		builder.setPositiveButton("Ладно, уговорили...", this);
		builder.setNegativeButton("Нет, хочу больше рекламы!", this);

		dialog = builder.create();
	}		
	
	public void show() {
		dialog.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch(which) {
		case DialogInterface.BUTTON_POSITIVE:
			dialogAnswerListener.onDialogConfirmed();
			break;
			
		case DialogInterface.BUTTON_NEGATIVE:
			dialogAnswerListener.onDialogCanceled();
			break;
		}		
	}
	
}
