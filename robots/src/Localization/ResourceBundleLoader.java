package Localization;

import java.io.Console;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;
import java.io.File;

import java.util.ArrayList;
import Localization.LanguageChangeable;

public class ResourceBundleLoader {
	
	private static Locale currentLocale = Locale.getDefault();
	private static ArrayList<LanguageChangeable> elements = new ArrayList<LanguageChangeable>();
	
	public static ResourceBundle load(String bundleName, Locale locale)
	{
		currentLocale = locale;
		return load(bundleName);
	}
	
	public static ResourceBundle load(String bundleName)
	{
		String dir = System.getProperty("user.dir") + File.separator + "localization";
        File file = new File(dir);
        try {
        	URL[] urls = {file.toURI().toURL()};
        	ClassLoader loader = new URLClassLoader(urls);
        	return ResourceBundle.getBundle(bundleName, currentLocale, loader);
        } catch (MalformedURLException e) {
        	e.printStackTrace();
        }
        return null;
	}
	
	public static void addElementToUpdate(LanguageChangeable elem)
	{
		elements.add(elem);
	}
	
	public static void updateLanguage(Locale locale)
	{
		currentLocale = locale;
		for (LanguageChangeable x : elements)
			x.changeLanguage();
	}
}
