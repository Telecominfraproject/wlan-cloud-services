package com.telecominfraproject.wlan.client.session.models;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Maps;

public class UserAgentOsMapper 
{
    private enum SupportedOS
    {
        Win10,Win7,MacOSX,Linux,Win8_1,iOS,WinVista,Win8,Android,WinXP;
    }
    
    private static Map<SupportedOS, String> osToKeywords = Maps.newConcurrentMap();
    private static Map<SupportedOS, String> osToCleanEntry = Maps.newConcurrentMap();
    private static Map<SupportedOS, String> osToDhcpFingerPrintKeywords = Maps.newConcurrentMap();
    
    private static final Pattern ANDROID_VERSION_PATTERN = Pattern.compile("Android ([0-9]{1,3}(\\.[0-9]{1,3}){1,2})");
    private static final Pattern MACOS_VERSION_PATTERN = Pattern.compile("Mac OS X ([0-9]{1,3}([_\\.][0-9]{1,3}){1,2})");
    private static final Pattern IOS_VERSION_PATTERN = Pattern.compile("OS ([0-9]{1,3}([_\\.][0-9]{1,3}){1,2})");
        
    static
    {
        /**
         * These heuristics were taken from https://techblog.willshouse.com/2012/01/03/most-common-user-agents/
         */
        osToKeywords.put(SupportedOS.Win10, "Windows NT 10.0");
        osToKeywords.put(SupportedOS.WinXP, "Windows NT 5.1");
        osToKeywords.put(SupportedOS.WinVista, "Windows NT 6.0");
        osToKeywords.put(SupportedOS.Win7, "Windows NT 6.1");
        osToKeywords.put(SupportedOS.Win8, "Windows NT 6.2");
        osToKeywords.put(SupportedOS.Win8_1, "Windows NT 6.3");
        osToKeywords.put(SupportedOS.MacOSX, "Macintosh; Intel Mac OS X 10");
        osToKeywords.put(SupportedOS.Linux, "X11");
        osToKeywords.put(SupportedOS.iOS, "like Mac OS X");
        osToKeywords.put(SupportedOS.Android, "Android ");
        
        osToCleanEntry.put(SupportedOS.Win10, "Windows 10");
        osToCleanEntry.put(SupportedOS.Win7, "Windows 7");
        osToCleanEntry.put(SupportedOS.Win8, "Windows 8");
        osToCleanEntry.put(SupportedOS.WinXP, "Windows XP");
        osToCleanEntry.put(SupportedOS.Win8_1, "Windows 8.1");
        osToCleanEntry.put(SupportedOS.Linux, "Linux");
        osToCleanEntry.put(SupportedOS.WinVista, "Windows Vista");
        osToCleanEntry.put(SupportedOS.iOS, "iOS ");
        osToCleanEntry.put(SupportedOS.MacOSX, "Mac OS ");
        osToCleanEntry.put(SupportedOS.Android, "Android ");
        
        //
        // These are the keywords that the DHCP fingerprint must contain
        // in order for the platform to be valid. This was to avoid seeing Android dhcp
        // fingerprints and receiving a Windows UserAgent.
        //
        osToDhcpFingerPrintKeywords.put(SupportedOS.Android, "Android");
        osToDhcpFingerPrintKeywords.put(SupportedOS.MacOSX, "Macintosh");
        osToDhcpFingerPrintKeywords.put(SupportedOS.iOS, "iPhone");
        osToDhcpFingerPrintKeywords.put(SupportedOS.Win10, "Windows");
        osToDhcpFingerPrintKeywords.put(SupportedOS.Win7, "Windows");
        osToDhcpFingerPrintKeywords.put(SupportedOS.Win8, "Windows");
        osToDhcpFingerPrintKeywords.put(SupportedOS.Win8_1, "Windows");
        osToDhcpFingerPrintKeywords.put(SupportedOS.WinXP, "Windows");
        osToDhcpFingerPrintKeywords.put(SupportedOS.WinVista, "Windows");
    }

    
    
    /**
     * Will return a pretty pritned version of the platform specified in the user agent.
     * 
     * @param userAgent
     * @return null if we can't figure out the platform
     * 
     */
    public static String getPlatform(String userAgent, String dhcpFingerprint)
    {
        if(userAgent != null)
        {
            for(Entry<SupportedOS, String> entry : osToKeywords.entrySet())
            {
                if(userAgent.contains(entry.getValue()))
                {
                    return getPrettyPrint(entry.getKey(), userAgent, dhcpFingerprint);
                }
            }
        }
        
        return null;
    }
    
    
    static String getPrettyPrint(SupportedOS os, String fullUserAgent, String dhcpFingerprint)
    {
        //
        // Does this OS even make sense with the received dhcpFingerprints? 
        //
        String dhcpFingerprintRequiredKeyword = osToDhcpFingerPrintKeywords.get(os);
        
        if(dhcpFingerprintRequiredKeyword != null && dhcpFingerprint != null)
        {
            if(!dhcpFingerprint.contains(dhcpFingerprintRequiredKeyword))
            {
                return null;
            }
        }

        //
        // Since we passed the first pass, we'll start extracting the OS.
        //
        String returnValeu = osToCleanEntry.get(os);
        
        if(returnValeu == null)
        {
            returnValeu = "Unknown";
        }
        
        if(os == SupportedOS.Android)
        {
            Matcher m = ANDROID_VERSION_PATTERN.matcher(fullUserAgent);
            
            if(m.find())
            {
                return returnValeu += m.group(1);
            }
        }
        else if(os == SupportedOS.MacOSX)
        {
            Matcher m = MACOS_VERSION_PATTERN.matcher(fullUserAgent);
            
            if(m.find())
            {
                return returnValeu += m.group(1).replaceAll("_", ".");
            }
        }
        else if(os == SupportedOS.iOS)
        {
            Matcher m = IOS_VERSION_PATTERN.matcher(fullUserAgent);
            
            if(m.find())
            {
                return returnValeu += m.group(1).replaceAll("_", ".");
            }
        }

        return returnValeu;
    }
    
    
}

    