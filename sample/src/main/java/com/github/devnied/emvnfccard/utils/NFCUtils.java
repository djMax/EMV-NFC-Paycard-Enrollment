package com.github.devnied.emvnfccard.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;

/**
 * 
 * Utils class used to manager NFC Adapter
 * 
 * @author MILLAU julien
 * 
 */
public class NFCUtils {

	/**
	 * Check if the NFCAdapter is enable
	 * 
	 * @return true if the NFCAdapter is available enable
	 */
	public static boolean isNfcEnable(final Context pContext) {
		boolean ret = true;
		try {
			NfcAdapter adpater = NfcAdapter.getDefaultAdapter(pContext);
			ret = adpater != null && adpater.isEnabled();
		} catch (UnsupportedOperationException e) {
			ret = false;
		}
		return ret;
	}

	/**
	 * NFC adapter
	 */
	private final NfcAdapter mNfcAdapter;
	/**
	 * Intent sent
	 */
	private final PendingIntent mPendingIntent;

	/**
	 * Parent Activity
	 */
	private final Activity mActivity;

	/**
	 * Inetnt filter
	 */
	private static final IntentFilter[] INTENT_FILTER = new IntentFilter[] { new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED) };

	/**
	 * Tech List
	 */
	private static final String[][] TECH_LIST = new String[][] { { IsoDep.class.getName() } };

	/**
	 * Constructor of this class
	 * 
	 * @param pActivity
	 *            activity context
	 */
	public NFCUtils(final Activity pActivity) {
		mActivity = pActivity;
		mNfcAdapter = NfcAdapter.getDefaultAdapter(mActivity);
		mPendingIntent = PendingIntent.getActivity(mActivity, 0,
				new Intent(mActivity, mActivity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	}

	/**
	 * Disable dispacher Remove the most important priority for foreground application
	 */
	public void disableDispatch() {
		if (mNfcAdapter != null) {
			mNfcAdapter.disableForegroundDispatch(mActivity);
            mNfcAdapter.disableReaderMode(mActivity);
		}
	}

	/**
	 * Activate NFC dispacher to read NFC Card Set the most important priority to the foreground application
	 */
	public void enableDispatch() {
		if (mNfcAdapter != null) {
			mNfcAdapter.enableForegroundDispatch(mActivity, mPendingIntent, INTENT_FILTER, TECH_LIST);
            mNfcAdapter.enableReaderMode(mActivity, new NfcAdapter.ReaderCallback() {
                @Override
                public void onTagDiscovered(Tag tag) {
                    IsoDep iso = IsoDep.get(tag);
                    try {
                        iso.connect();
                        // SELECT 1PAY.SYS.DDF01
                        byte[] SELECT = new ApduCommand()
                                .Instruction(0xA4)
                                .P1(0x04)
                                .P2(0)
                                .appendAscii("2PAY.SYS.DDF01")
                                .getBytes();

                        bytes[] hist = iso.getHistoricalBytes();
                        byte[] response = iso.transceive(SELECT);

                        System.out.println(response.toString());
                    } catch (Exception x) {
                        System.out.println(x.toString());
                    }
                }
            },  NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK | NfcAdapter.FLAG_READER_NFC_A,
                    null);
		}
	}

	/**
	 * Getter mNfcAdapter
	 * 
	 * @return the mNfcAdapter
	 */
	public NfcAdapter getmNfcAdapter() {
		return mNfcAdapter;
	}
}
