package org.wordpress.android.util;

/**
 * As of January 20 2012:
 * The BlackBerry Runtime for Android Apps supports Android 2.3.3 applications.
 * <p/>
 * Unsupported App Types:
 * - Widget apps : Apps that are only meant to be widgets are not supported
 * - Apps that include no launchable Activity
 * - Apps that include more than one launchable Activity
 * - Apps whose minimum required Android API level is more than 10, and whose maximum supported level is less than 10
 * <p/>
 * Unsupported Hardware Features:
 * - Telephony (including SMS and MMS)
 * - Bluetooth
 * - Camera:  The intent to launch the camera is supported. However, currently the Camera class in the Android SDK is not supported.
 * As a result, although you can launch the camera application, you cannot access the Camera hardware.
 * - NFC
 * - Barometers
 * - Ambient light sensor
 * - Proximity sensor
 * - VoIP
 * <p/>
 * Unsupported Software Features:
 * - Vending (In App Payments): com.android.vending
 * - Cloud To Device Messaging (Push): com.google.android.c2dm
 * - Google Maps: com.google.android.maps
 * - Text to Speech: com.google.tts
 * <p/>
 * <p/>
 * Major Details here: https://bdsc.webapps.blackberry.com/android/apisupport
 *
 * @author daniloercoli
 */


public class DeviceUtils {

	private static DeviceUtils instance;

	private boolean isBlackBerry = false;
	private boolean isKindleFire = false;

	private DeviceUtils() {
	    /*isPlayBook =  android.os.Build.MANUFACTURER.equalsIgnoreCase( "Research in Motion" ) &&
	            android.os.Build.MODEL.startsWith( "BlackBerry Runtime for Android" ); */
		isBlackBerry = System.getProperty("os.name").equalsIgnoreCase("qnx") ? true : false;
		isKindleFire = android.os.Build.MODEL.equalsIgnoreCase("kindle fire") ? true : false;
	}

	public static DeviceUtils getInstance() {
		if (instance == null) {
			instance = new DeviceUtils();
		}
		return instance;
	}

	public boolean isBlackBerry() {
		return isBlackBerry;
	}

	public boolean isKindleFire() {
		return isKindleFire;
	}

	;
}