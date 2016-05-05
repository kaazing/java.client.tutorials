/**
 * Copyright (c) 2007-2015, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.gateway.jms.client.demo;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * The class is used to add Runnable in a queue and the runnable added to the queue 
 * will be run in a first in first out basis. This class is useful to run a series of tasks 
 * sequentially in a separate thread from the main thread.
 *
 */
public class DispatchQueue extends HandlerThread {

	private Handler handler;

	public DispatchQueue(String name) {
		super(name);
	}
	
	/**
	 * The message blocks until the thread is started. This should be called 
	 * after call to start() to ensure the thread is ready.
	 */
	public void waitUntilReady() {
		handler = new Handler(getLooper());
	}
	
	/**
	 * Adds the Runnable to the message queue which will be run on the thread.
	 * The runnable will be run in a first in first out basis.
	 */
	public void dispatchAsync(Runnable task) {
		handler.post(task);
	}
	
	public void removePendingJobs() {
		handler.removeCallbacksAndMessages(null);
	}

}