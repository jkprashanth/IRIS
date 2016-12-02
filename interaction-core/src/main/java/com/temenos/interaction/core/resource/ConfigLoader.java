package com.temenos.interaction.core.resource;

/*
 * #%L
 * interaction-core
 * %%
 * Copyright (C) 2012 - 2015 Temenos Holdings N.V.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class provides an abstraction from the underlying mechanism used to load config files  
 *
 */
public class ConfigLoader { 
	private String irisConfigDirPath;
	private Set<String> irisConfigDirPaths = new LinkedHashSet<>();
	
	// Webapp context param defining the location of the unpacked IRIS configuration files
	public static final String IRIS_CONFIG_DIR_PARAM = "com.temenos.interaction.config.dir";
	
	private final static Logger logger = LoggerFactory.getLogger(ConfigLoader.class);	
		
	/**
	 * Overrides the default IRIS configuration location with the paths given
	 * 
	 * @param irisConfigDirPath The IRIS configuration location
	 */
	public void setIrisConfigDirPath(String irisConfigDirPath) {
		StringBuilder onlyExistingPaths = new StringBuilder();
		for(String pathString : irisConfigDirPath.split(",")) {
			Path path = Paths.get(pathString.trim());
			if(Files.exists(path) && Files.isDirectory(path)) {
				irisConfigDirPaths.add(path.toString());
				if(onlyExistingPaths.length() != 0) {
					onlyExistingPaths.append(",");
				}
				onlyExistingPaths.append(path.toString());
			}
		}
		if(onlyExistingPaths.length() == 0) {
			this.irisConfigDirPath = null;
			logger.error("None of the given directories exists (" + irisConfigDirPath + ") !");
		} else {
			this.irisConfigDirPath = onlyExistingPaths.toString();
		}
	}	

	/**
	 * @return The path of the overridden IRIS configuration location
	 */
	public String getIrisConfigDirPath() {
		return irisConfigDirPath;
	}

	public Set<String> getIrisConfigDirPaths() {
		return irisConfigDirPaths;
	}



	public boolean isExist(String filename) {
		if(irisConfigDirPaths.isEmpty()) {
			return getClass().getClassLoader().getResource(filename) != null;
		} else {
			File file = searchInDirectories(filename);
			return file != null;
		}
	}

	public InputStream load(String filename) throws FileNotFoundException, Exception {
		InputStream is = null;
		
		if(irisConfigDirPaths.isEmpty()) {
			is = getClass().getClassLoader().getResourceAsStream(filename);
			
			if(is == null) {
				logger.error("Unable to load " + filename + " from classpath.");
				logger.error("There aren`t any Iris configuration directories specified.");
				throw new Exception("Unable to load " + filename + " from classpath.");
			}
		} else {
			File file = searchInDirectories(filename);
			if (file != null) {
				is = new FileInputStream(file);
			} else {
				throw new Exception("Cannot find or load '" + filename + "'");
			}
		}
		
		return is;
	}

	private File searchInDirectories(String filename) {
		if(irisConfigDirPaths.isEmpty()) {
			return null;
		}

		for(String directoryPath : irisConfigDirPaths) {
			Path filePath = Paths.get(directoryPath, filename);
			if(Files.exists(filePath) && !Files.isDirectory(filePath)) {
				return filePath.toFile();
			}
		}
		return null;
	}
}
