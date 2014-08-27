package com.xpedx.nextgen.catalog.api.eb3359;

public class StopWatchFor3359 {

	private long startMillis;
	private long stopMillis;

	public StopWatchFor3359(boolean start) {
		super();
		if (start) {
			start();
		}
	}

	public void start() {
		startMillis = System.currentTimeMillis();
	}

	public long stop() {
		if (startMillis == 0) {
			throw new IllegalStateException("Not yet started");
		}
		stopMillis = System.currentTimeMillis();
		return stopMillis - startMillis;
	}

	public void reset() {
		startMillis = 0;
		stopMillis = 0;
	}

}
