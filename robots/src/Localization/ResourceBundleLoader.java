package Localization;

import java.io.Console;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;
import java.io.File;

public class ResourceBundleLoader {
	public static ResourceBundle load(String bundleName)
	{
		
		String dir = System.getProperty("user.dir") +File.separator+"robots"+File.separator+"localization";
        File file = new File(dir);
        try {
        	URL[] urls = {file.toURI().toURL()};
        	ClassLoader loader = new URLClassLoader(urls);
        	return ResourceBundle.getBundle(bundleName, Locale.getDefault(), loader);
        } catch (MalformedURLException e) {
        	e.printStackTrace();
        }
        return null;
	}
}
