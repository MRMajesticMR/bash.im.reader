package ru.majestic.bashimreader.dialogs;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.preference.ApplicationSettings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class ExitApplicationAlertDialog {
	
	private Context context;
	private Activity requestedActivity;
	private AlertDialog dialog;

	public ExitApplicationAlertDialog(final Activity requestedActivity) {
		this.requestedActivity = requestedActivity;
		this.context = requestedActivity.getApplicationContext();		
				
		final AlertDialog.Builder builder = new AlertDialog.Builder(requestedActivity);
		builder.setTitle(R.string.dialog_exit_application_title);
		builder.setMessage(R.string.dialog_exit_application_message);
		builder.setPositiveButton(R.string.dialog_exit_application_btn_ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(context, R.string.dialog_exit_application_toast_thanks, Toast.LENGTH_LONG).show();
				new ApplicationSettings(context).userVoted();				
				launchMarket();
			}
		});

		builder.setNegativeButton(R.string.dialog_exit_application_btn_later, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				requestedActivity.finish();
			}
		});

		dialog = builder.create();
	}
	
	private void launchMarket() {
		final Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName()));
		try {
			requestedActivity.startActivity(myAppLinkToMarket);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(context, R.string.dialog_exit_application_toast_where_is_market, Toast.LENGTH_LONG).show();
		}
	}
	
	public void show() {
		dialog.show();
	}

}
