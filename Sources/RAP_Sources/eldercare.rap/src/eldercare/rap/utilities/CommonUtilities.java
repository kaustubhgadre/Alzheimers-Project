/**
 * 
 */
package eldercare.rap.utilities;

import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

/**
 * @author Kaustubh
 * 
 */
public class CommonUtilities {

	public static IPath getPlatformPath() {
		URL platformInstallationURL = Platform.getInstallLocation().getURL();
		String platformInstallationPath = platformInstallationURL.getPath();
		IPath newInstallationPath = new Path(platformInstallationPath);
		return newInstallationPath;
	}

	// Get platform path
	public static String getPlatformPathString() {
		URL platformInstallationURL = Platform.getInstallLocation().getURL();
		String platformInstallationPath = platformInstallationURL.getPath();
		return platformInstallationPath;
	}

	// Get installation path of corona
	public static String getInstallationDirectory() {
		IPath newInstallationPath = getPlatformPath();
		IPath installationDirectory = newInstallationPath.removeLastSegments(1);
		String platformInstallationDir = installationDirectory.toString();
		return platformInstallationDir;
	}
}
