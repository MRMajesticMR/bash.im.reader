package ru.majestic.bashimreader.loaders;

import ru.majestic.bashimreader.loaders.listeners.OnPageLoadListener;

public interface IPageLoader {

	public void setOnPageLoadListener(OnPageLoadListener onPageLoadListener);
	public void load(String url);
	
}
