package com.github.devnied.emvnfccard.enums;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import fr.devnied.bitlib.BytesUtils;

/**
 * Class used to define all supported NFC EMV paycard. <link>http://en.wikipedia.org/wiki/Europay_Mastercard_Visa</link>
 * 
 * @author MILLAU Julien
 * 
 */
public enum EmvCardScheme {

	VISA("VISA", "^4[0-9]{12,15}", "A0 00 00 00 03"), //
	MASTER_CARD("Master card", "^5[0-5][0-9]{14}", "A0 00 00 00 04", "A0 00 00 00 05"), //
	AMERICAN_EXPRESS("American express", "^3[47][0-9]{13}", "A0 00 00 00 25"), //
	CB("CB", null, "A0 00 00 00 42"), //
	LINK("LINK", null, "A0 00 00 00 29"), //
	JCB("JCB", "^35[0-9]{14}", "A0 00 00 00 65"), //
	DANKORT("Dankort", null, "A0 00 00 01 21"), //
	COGEBAN("CoGeBan", null, "A0 00 00 01 41"), //
	DISCOVER("Discover", "(6011|65|64[4-9]|622)[0-9]*", "A0 00 00 01 52"), //
	BANRISUL("Banrisul", null, "A0 00 00 01 54"), //
	SPAN("Saudi Payments Network", null, "A0 00 00 02 28"), //
	INTERAC("Interac", null, "A0 00 00 02 77"), //
	ZIP("Discover Card", null, "A0 00 00 03 24"), //
	UNIONPAY("UnionPay", "^62[0-9]{14,17}", "A0 00 00 03 33"), //
	EAPS("Euro Alliance of Payment Schemes", null, "A0 00 00 03 59"), //
	VERVE("Verve", null, "A0 00 00 03 71"), //
	TENN("The Exchange Network ATM Network", null, "A0 00 00 04 39"), //
	RUPAY("Rupay", null, "A0 00 00 05 24"), //
	ПРО100("ПРО100", null, "A0 00 00 04 32");

	/**
	 * array of Card AID or partial AID (RID)
	 */
	private final String[] aids;

	/**
	 * array of Aid in byte
	 */
	private final byte[][] aidsByte;

	/**
	 * Card scheme (card number IIN ranges)
	 */
	private final String name;

	/**
	 * Card number regex
	 */
	private final String regex;

	/**
	 * Constructor using fields
	 * 
	 * @param pAid
	 *            Card AID or RID
	 * @param pScheme
	 *            scheme name
	 * @param pRegex
	 *            Card regex
	 */
	private EmvCardScheme(final String pScheme, final String pRegex, final String... pAids) {
		aids = pAids;
		aidsByte = new byte[pAids.length][];
		for (int i = 0; i < aids.length; i++) {
			aidsByte[i] = BytesUtils.fromString(pAids[i]);
		}
		name = pScheme;
		regex = pRegex;
	}

	/**
	 * Method used to get the field aid
	 * 
	 * @return the aid
	 */
	public String[] getAid() {
		return aids;
	}

	/**
	 * Method used to get the field name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get card type by AID
	 * 
	 * @param pAid
	 *            card AID
	 * @return CardType or null
	 */
	public static EmvCardScheme getCardTypeByAid(final String pAid) {
		EmvCardScheme ret = null;
		if (pAid != null) {
			String aid = StringUtils.deleteWhitespace(pAid);
			for (EmvCardScheme val : EmvCardScheme.values()) {
				for (String schemeAid : val.getAid()) {
					if (aid.startsWith(StringUtils.deleteWhitespace(schemeAid))) {
						ret = val;
						break;
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Method used to the the card type with regex
	 * 
	 * @param pCardNumber
	 *            card number
	 * @return the type of the card using regex
	 */
	public static EmvCardScheme getCardTypeByCardNumber(final String pCardNumber) {
		EmvCardScheme ret = null;
		if (pCardNumber != null) {
			for (EmvCardScheme val : EmvCardScheme.values()) {
				if (val.regex != null && Pattern.matches(val.regex, StringUtils.deleteWhitespace(pCardNumber))) {
					ret = val;
					break;
				}
			}
		}
		return ret;
	}

	/**
	 * Method used to get the field aidByte
	 * 
	 * @return the aidByte
	 */
	public byte[][] getAidByte() {
		return aidsByte;
	}

}
