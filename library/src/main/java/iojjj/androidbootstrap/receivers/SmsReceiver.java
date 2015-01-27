package iojjj.androidbootstrap.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Receiver for detecting new sms
 */
public abstract class SmsReceiver extends BroadcastReceiver {

    public static final List<SmsListener> SMS_LISTENERS = new ArrayList<SmsListener>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            return;
        }
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }
        Object[] pdus = (Object[]) bundle.get("pdus");
        SmsMessage[] msgs = new SmsMessage[pdus.length];
        for (int i = 0; i < msgs.length; i++) {
            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            if (msgs[i] == null)
                continue;
            String msgBody = msgs[i].getMessageBody();
            if (TextUtils.isEmpty(msgBody))
                continue;
            Matcher matcher = getSmsCodePatter().matcher(msgBody);
            if (matcher.find()) {
                String code = matcher.group(1);
                Iterator<SmsListener> iterator = SMS_LISTENERS.iterator();
                while (iterator.hasNext()) {
                    SmsListener listener = iterator.next();
                    if (listener == null) {
                        iterator.remove();
                        continue;
                    }
                    listener.onCodeReceived(code);
                }
                break;
            }
        }
    }

    protected abstract Pattern getSmsCodePatter();

    public interface SmsListener {
        void onCodeReceived(String code);
    }
}
