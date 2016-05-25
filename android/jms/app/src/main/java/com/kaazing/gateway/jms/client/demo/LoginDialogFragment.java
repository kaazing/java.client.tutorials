/**
 * Copyright (c) 2007-2015, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.gateway.jms.client.demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

public class LoginDialogFragment extends DialogFragment {
	
	private String username;
	private String password;
	private LoginDialogListener listener;
	private boolean cancelled;
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater layoutInflaor = getActivity().getLayoutInflater();
		builder.setView(layoutInflaor.inflate(R.layout.login, null)).setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				EditText usernameText = (EditText)LoginDialogFragment.this.getDialog().findViewById(R.id.username);
				EditText passwordText = (EditText)LoginDialogFragment.this.getDialog().findViewById(R.id.password);
				username = usernameText.getText().toString();
				password = passwordText.getText().toString();
				LoginDialogFragment.this.getDialog().dismiss();
				listener.onDismissed();
				
			}
		})
        .setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	cancelled = true;
                LoginDialogFragment.this.getDialog().cancel();
                listener.onDismissed();
            }
        });
		return builder.create();
		
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setListener(LoginDialogListener listener) {
		this.listener = listener;
	}
	
	public interface LoginDialogListener {
		public void onDismissed();
	}
}
