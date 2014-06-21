package com.jj.speedwave.services.speed;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class IngressIntentBuilder {

	private Context context;

	public IngressIntentBuilder(Context context) {
		this.context = context;
	}
	
	public PendingIntent build() {
		Intent openIngressIntent = new Intent();
		openIngressIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		openIngressIntent.setAction("android.intent.action.VIEW");
		openIngressIntent.setComponent(ComponentName.unflattenFromString("com.nianticproject.ingress/com.nianticproject.ingress.NemesisActivity"));
		return PendingIntent.getActivity(context, 0, openIngressIntent, 0);
	}

}
