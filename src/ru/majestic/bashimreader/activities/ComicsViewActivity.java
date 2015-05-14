package ru.majestic.bashimreader.activities;

import com.flurry.android.FlurryAgent;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.adapters.ComicsCalendarAdapter;
import ru.majestic.bashimreader.data.Comics;
import ru.majestic.bashimreader.loaders.listeners.OnComicsLoadListener;
import ru.majestic.bashimreader.managers.ComicsManager;
import ru.majestic.bashimreader.preference.ApplicationSettings;
import ru.majestic.bashimreader.utils.EndAnimationListener;
import ru.majestic.bashimreader.utils.Sharer;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ComicsViewActivity extends Activity implements OnClickListener, OnComicsLoadListener, OnItemSelectedListener, OnTouchListener {

	private static final String SAVED_POSITION = "SAVED_POSITION";
	private static final int SLIDE_DELTA = 40;

	private int mBeginTouchX;

	private ViewGroup mRootLyt, mTitleLyt;
	private TextView mTitleTxt, mDownloadTxt;

	private Button mBackBtn;
	private ImageView mMainComicsImg;
	private ComicsManager mComicsManager;
	private ViewGroup mDownloadViewRootLyt;
	private Gallery mComicsCalendarGallery;
	private ComicsCalendarAdapter mComicsCalendarAdapter;
	private ApplicationSettings mApplicationSettings;

	private Animation mDownloadViewInAnimation, mDownloadViewOutAnimation;
	private EndAnimationListener mDownloadViewAnimationListener = new EndAnimationListener() {

		@Override
		public void onAnimationEnd(Animation animation) {
			mDownloadViewRootLyt.setVisibility(View.GONE);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplicationSettings = new ApplicationSettings(this);

		initAnimation();
		initGUI(savedInstanceState);
		initNightMode();

		mComicsManager = new ComicsManager(this, savedInstanceState);
		mComicsManager.setOnComicsLoadListener(this);
		mComicsManager.start();

		mDownloadViewRootLyt.setVisibility(View.VISIBLE);
		mDownloadViewRootLyt.startAnimation(mDownloadViewInAnimation);
	}

	private final void initNightMode() {
		if (mApplicationSettings.isNightModeEnabled()) {
			mRootLyt.setBackgroundColor(getResources().getColor(R.color.night_mode_background));
			mDownloadViewRootLyt.setBackgroundDrawable(getResources().getDrawable(R.drawable.night_mode_download_view_background));
			mTitleLyt.setBackgroundDrawable(getResources().getDrawable(R.drawable.night_mode_quotes_title_background));

			mTitleTxt.setTextColor(getResources().getColor(R.color.night_mode_text));
			mDownloadTxt.setTextColor(getResources().getColor(R.color.night_mode_text));
		} else {
			mRootLyt.setBackgroundColor(getResources().getColor(R.color.light_mode_background));
			mTitleLyt.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_mode_quotes_title_background));
			mDownloadViewRootLyt.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_mode_download_view_background));

			mTitleTxt.setTextColor(getResources().getColor(R.color.light_mode_text));
			mDownloadTxt.setTextColor(getResources().getColor(R.color.light_mode_text));
		}
	}

	private void initAnimation() {
		mDownloadViewInAnimation = AnimationUtils.loadAnimation(this, R.anim.download_view_in);
		mDownloadViewOutAnimation = AnimationUtils.loadAnimation(this, R.anim.download_view_out);
		mDownloadViewOutAnimation.setAnimationListener(mDownloadViewAnimationListener);
	}

	private void initGUI(final Bundle pSavedInstance) {
		setContentView(R.layout.activity_comics_view);

		mRootLyt = (ViewGroup) findViewById(R.id.comics_view_lyt_root);
		mTitleLyt = (ViewGroup) findViewById(R.id.comics_view_lyt_title);
		mTitleTxt = (TextView) findViewById(R.id.comics_view_txt_title);
		mDownloadTxt = (TextView) findViewById(R.id.comics_view_txt_download);

		mMainComicsImg = (ImageView) findViewById(R.id.comics_view_img_main_comics);
		mBackBtn = (Button) findViewById(R.id.comics_view_btn_back);
		mDownloadViewRootLyt = (ViewGroup) findViewById(R.id.comics_view_lyt_download);
		mComicsCalendarGallery = (Gallery) findViewById(R.id.comics_calendar_gallery);
		mComicsCalendarAdapter = new ComicsCalendarAdapter(this, pSavedInstance);

		mBackBtn.setOnClickListener(this);
		mMainComicsImg.setOnClickListener(this);
		mMainComicsImg.setOnTouchListener(this);
		mComicsCalendarGallery.setAdapter(mComicsCalendarAdapter);
		mComicsCalendarGallery.setOnItemSelectedListener(this);

		if (pSavedInstance != null && pSavedInstance.containsKey(SAVED_POSITION)) {
			mComicsCalendarGallery.setSelection(pSavedInstance.getInt(SAVED_POSITION));
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		FlurryAgent.onStartSession(this);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		FlurryAgent.onEndSession(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		mComicsCalendarAdapter.saveState(outState);
		mComicsManager.saveState(outState);
		outState.putInt(SAVED_POSITION, mComicsCalendarGallery.getSelectedItemPosition());
	}

	private void exit() {
		finish();
		overridePendingTransition(R.anim.animation_activity_exit_out, R.anim.animation_activity_exit_in);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exit();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comics_view_btn_back:
			exit();
			break;
		case R.id.comics_view_img_main_comics:
			Sharer.share(this, mComicsCalendarAdapter.getLasSelectedComics());
			break;
		}
	}

	@Override
	public void onComicsLoad(final Comics pComics) {
		mDownloadViewRootLyt.startAnimation(mDownloadViewOutAnimation);
		mComicsCalendarAdapter.addComics(pComics);

		if (mComicsCalendarGallery.getSelectedItemPosition() > mComicsCalendarAdapter.getCount() - 5) {
			mComicsManager.loadNextComics();
		}
	}

	@Override
	public void onComicsLoadError() {
		mDownloadViewRootLyt.startAnimation(mDownloadViewOutAnimation);
		Toast.makeText(this, "Не удалось зарузить комикс", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pPosition, long arg3) {
		mMainComicsImg.setImageBitmap(((Comics) mComicsCalendarAdapter.getItem(pPosition)).getImage());
		mComicsCalendarAdapter.setLastSelectedComicsIndex(pPosition);
		
		if (pPosition > mComicsCalendarAdapter.getCount() - 5) {
			mComicsManager.loadNextComics();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			this.mBeginTouchX = (int) event.getX();
		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (mBeginTouchX - event.getX() > SLIDE_DELTA) {
				if (mComicsCalendarGallery.getSelectedItemPosition() < mComicsCalendarGallery.getCount() - 1)
					mComicsCalendarGallery.setSelection(mComicsCalendarGallery.getSelectedItemPosition() + 1);
				return true;
			}

			if (mBeginTouchX - event.getX() < -SLIDE_DELTA) {
				if (mComicsCalendarGallery.getSelectedItemPosition() > 0)
					mComicsCalendarGallery.setSelection(mComicsCalendarGallery.getSelectedItemPosition() - 1);
				return true;
			}
		}
		return false;
	}

}
