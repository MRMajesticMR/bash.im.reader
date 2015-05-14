package ru.majestic.bashimreader.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SocialUtils {

	public static final String VK 			= "http://vk.com/bash_im_reader";
	public static final String TWITTER 		= "https://twitter.com/bash_im_reader";
	public static final String GOOGLE 		= "https://plus.google.com/u/0/communities/103880621493998805783";
	public static final String PDA 			= "http://4pda.ru/forum/index.php?showtopic=489084";

	public static final void openLink(final Context context, final String url) {
		final Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		context.startActivity(i);
	}

}
