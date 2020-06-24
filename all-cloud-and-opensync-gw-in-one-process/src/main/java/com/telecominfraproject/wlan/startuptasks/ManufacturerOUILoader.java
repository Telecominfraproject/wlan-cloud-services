package com.telecominfraproject.wlan.startuptasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;

import com.telecominfraproject.wlan.manufacturer.ManufacturerInterface;
import com.telecominfraproject.wlan.manufacturer.util.ManufacturerOuiUtil;

@Configuration
public class ManufacturerOUILoader implements ApplicationRunner {

	private static final Logger LOG = LoggerFactory.getLogger(ManufacturerOUILoader.class);

    @Value("${tip.wlan.ouiFileUrl:https://tip-read:tip-read@tip.jfrog.io/artifactory/tip-wlan-cloud-schemas/oui.txt.gz}")
	private String ouiFileUrl;

    @Value("${tip.wlan.ouiFileLocalPath:./oui.txt.gz}")
	private String ouiFileLocalPath;

    
    @Autowired
    private RestOperations restTemplate;

	@Autowired
	private ManufacturerInterface manufacturerInterface;
	

	@Override
	public void run(ApplicationArguments args) {
		try {
			ManufacturerOuiUtil.loadAndCacheOUIFile(ouiFileUrl,  ouiFileLocalPath, restTemplate, manufacturerInterface);
		} catch (Exception e) {
			LOG.error("Got Exception ", e);
			throw new RuntimeException(e);
		}
	}
	

}
