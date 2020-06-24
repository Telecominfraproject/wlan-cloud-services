package com.telecominfraproject.wlan.manufacturer.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestOperations;

import com.telecominfraproject.wlan.manufacturer.ManufacturerInterface;

public class ManufacturerOuiUtil {

	private static final Logger LOG = LoggerFactory.getLogger(ManufacturerOuiUtil.class);

	public static void loadAndCacheOUIFile(String ouiFileUrl,  String ouiFileLocalPath, RestOperations restTemplate, ManufacturerInterface manufacturerInterface) throws IOException {
		byte[] gzippedContent = null;
		
		//	Store the file locally to reduce the load on the server:  		
		File file = new File(ouiFileLocalPath);
		if(file.canRead()) {
			//found cached local file, load it
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1500000);
			try(FileInputStream fis = new FileInputStream(file)){
				StreamUtils.copy(fis, baos);
				baos.flush();
				baos.close();
				gzippedContent = baos.toByteArray();
				LOG.info("Loaded OUI registrations from local cache {}", file.getAbsolutePath());
			} catch (IOException e ) {
				LOG.error("Cannot read local cache of OUI registrations file {}", file.getAbsolutePath(), e);
			}

		} 
		
		if(gzippedContent == null) {
			LOG.info("Loading Manufacturer OUI registry from {}", ouiFileUrl);

			//local cache file does not exist, get it from external URL
			gzippedContent = restTemplate.getForObject(ouiFileUrl, byte[].class);

			LOG.info("Downloaded manufacturer details");

			if(!file.exists()) {
				//cache it for the future
				try(FileOutputStream fos = new FileOutputStream(file)){
					StreamUtils.copy(gzippedContent, fos);
					fos.flush();				
					LOG.info("Saved OUI registrations into local cache {}", file.getAbsolutePath());
				} catch (IOException e ) {
					LOG.error("Cannot save local cache of OUI registrations file {}", file.getAbsolutePath(), e);
					if(file.exists() && !file.delete()) {
						LOG.error("Cannot remove local cache of OUI registrations file {} - please remove it manually", file.getAbsolutePath());
					}

				}
			}

		}
		
		
		manufacturerInterface.uploadOuiDataFile("oui.txt.gz", gzippedContent);
		LOG.info("Loaded manufacturer details");
	}

}
