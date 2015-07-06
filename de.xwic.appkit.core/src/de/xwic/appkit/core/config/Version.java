/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
package de.xwic.appkit.core.config;

/**
 * Represents a version of a configuration, database or alike.
 * @author Florian Lippisch
 */
public class Version implements Comparable<Version> {

	/** Major level only */
	public final static int MAJOR_LEVEL = 0;
	/** Major and Minor level*/
	public final static int MINOR_LEVEL = 1;
	/** Major, Minor and Patch level */
	public final static int PATCH_LEVEL = 2;
	/** Major, Minor, Patch and Build level */
	public final static int BUILD_LEVEL = 3;
	
	private int major = 0;
	private int minor = 0;
	private int patch = 0;
	private int build = 0;
	private String adds = "";
	
	/**
	 * Creates a version from a string.
	 * @param version
	 */
	public Version(String version) {
		parse(version);
	}
	
	/**
	 * @param major
	 * @param minor
	 * @param patch
	 * @param build
	 */
	public Version(int major, int minor, int patch, int build) {
		super();
		this.major = major;
		this.minor = minor;
		this.patch = patch;
		this.build = build;
	}

	/** 
	 * Default constructor that creates a version 0.0.0.0.
	 */
	public Version() {
		
	}
	
	/**
	 * Copies the values from the given version.
	 * @param databaseVersion
	 */
	public Version(Version version) {
		major = version.major;
		minor = version.minor;
		patch = version.patch;
		build = version.build;
		adds = version.adds;
	}

	/**
	 * Parses the string to find out the version number.
	 * @param version
	 */
	public void parse(String version) {
		// reset first
		major = 0;
		minor = 0;
		patch = 0;
		build = 0;
		String tmp = version.trim();
		int len = tmp.length();
		int idx = 0;
		boolean started = false;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			char c = tmp.charAt(i);
			if (idx < 4 && (c >= '0' && c <= '9')) {
				if (!started) {
					started = true;
				}
				sb.append(c);
			} else if (idx < 4 && (c == ' ' || c == '.' || c == '-')) {
				if (started) {
					setValue(idx, sb.toString());
					sb.setLength(0);
					idx++;
					started = false;
				}
			} else {
				if (idx < 4 && sb.length() != 0) {
					setValue(idx, sb.toString());
					sb.setLength(0);
				}
				idx = 4;
				sb.append(c);
			}
		}
		if (sb.length() != 0) {
			setValue(idx, sb.toString());
		}
	}
	
	/**
	 * Internally set the value.
	 * @param idx
	 * @param string
	 */
	private void setValue(int idx, String string) {
		switch (idx) {
		case 0: 
			major = Integer.parseInt(string);
			break;
		case 1: 
			minor = Integer.parseInt(string);
			break;
		case 2: 
			patch = Integer.parseInt(string);
			break;
		case 3: 
			build = Integer.parseInt(string);
			break;
		case 4:
			adds = string;
		}
	}
	/**
	 * @return the adds
	 */
	public String getAdds() {
		return adds;
	}
	/**
	 * @param adds the adds to set
	 */
	public void setAdds(String adds) {
		this.adds = adds;
	}
	/**
	 * @return the build
	 */
	public int getBuild() {
		return build;
	}
	/**
	 * @param build the build to set
	 */
	public void setBuild(int build) {
		this.build = build;
	}
	/**
	 * @return the major
	 */
	public int getMajor() {
		return major;
	}
	/**
	 * @param major the major to set
	 */
	public void setMajor(int major) {
		this.major = major;
	}
	/**
	 * @return the minor
	 */
	public int getMinor() {
		return minor;
	}
	/**
	 * @param minor the minor to set
	 */
	public void setMinor(int minor) {
		this.minor = minor;
	}
	/**
	 * @return the patch
	 */
	public int getPatch() {
		return patch;
	}
	/**
	 * @param patch the patch to set
	 */
	public void setPatch(int patch) {
		this.patch = patch;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return major + "." + minor + "." + patch + "." + build + adds;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((adds == null) ? 0 : adds.hashCode());
		result = PRIME * result + build;
		result = PRIME * result + major;
		result = PRIME * result + minor;
		result = PRIME * result + patch;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Version other = (Version) obj;
		if (adds == null) {
			if (other.adds != null)
				return false;
		} else if (!adds.equals(other.adds))
			return false;
		if (build != other.build)
			return false;
		if (major != other.major)
			return false;
		if (minor != other.minor)
			return false;
		if (patch != other.patch)
			return false;
		return true;
	}
	
	/**
	 * Comparse with another version until a specific level. The levels are:
	 * 0 = Major<br>
	 * 1 = Major & Minor<br>
	 * 2 = Major, Minor and Patchlevel
	 * 3 = Major, Minor, Patchlevel and Build
	 * @param otherVersion
	 * @param level
	 * @return
	 */
	public boolean equals(Version otherVersion, int level) {
		if (otherVersion == null) {
			return false;
		}
		switch (level) {
		case BUILD_LEVEL:
			if (otherVersion.build != build) {
				return false;
			}
			// fall through
		case PATCH_LEVEL: 
			if (otherVersion.patch != patch) {
				return false;
			}
			// fall through			
		case MINOR_LEVEL: 
			if (otherVersion.minor != minor) {
				return false;
			}
			// fall through
		case MAJOR_LEVEL:
			if (otherVersion.major != major) {
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Version o) {
		if (! (o instanceof Version)) {
			return 1;
		}
		
		Version other = (Version)o;
		if (other.major > major) {
			return -1;
		} else if (other.major < major) {
			return 1;
		}
		if (other.minor > minor) {
			return -1;
		} else if (other.minor < minor) {
			return 1;
		}
		if (other.patch > patch) {
			return -1;
		} else if (other.patch < patch) {
			return 1;
		}
		if (other.build > build) {
			return -1;
		} else if (other.build < build) {
			return 1;
		}
		return adds.compareTo(other.adds);
	}
	
	/**
	 * Returns true if this version is lower then the specified version.
	 * @param otherVersion
	 * @return
	 */
	public boolean isLower(Version otherVersion) {
		return compareTo(otherVersion) < 0;
	}
	/**
	 * Returns true if this version is higher then the specified version.
	 * @param otherVersion
	 * @return
	 */
	public boolean isHigher(Version otherVersion) {
		return compareTo(otherVersion) > 0;
	}
	
	
}
