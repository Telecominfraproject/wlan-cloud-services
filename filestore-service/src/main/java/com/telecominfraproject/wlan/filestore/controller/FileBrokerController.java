package com.telecominfraproject.wlan.filestore.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.server.exceptions.GenericErrorException;


/**
 * @author ekeddy
 * @author dtoptygin
 * We allow to post and read files from a single level of directory.
 */
@RestController
@RequestMapping(value = "/filestore")
public class FileBrokerController {

    private static final Logger LOG = LoggerFactory.getLogger(FileBrokerController.class);
    
    @Value("${tip.wlan.fileStoreDirectory:/tmp/tip-wlan-filestore}")
	private String fileStoreDirectoryName;

    @GetMapping(value = "/{fileName}")
    public void getFile(@PathVariable String fileName,  HttpServletResponse response) {
    	
    	File fileStoreDir = new File(fileStoreDirectoryName);
    	
    	if(!fileStoreDir.canRead()) {
            throw new GenericErrorException("Cannot read files from " + fileStoreDir.getAbsolutePath());
    	}
    	
        if (fileName.contains("/")) {
            throw new GenericErrorException("File name cannot refer to other directories");
        }
        
        File targetFile = new File(fileStoreDir, fileName);
        
    	if(!targetFile.canRead()) {
            throw new GenericErrorException("Cannot read file " + targetFile.getAbsolutePath());
    	}

        try(InputStream is = new FileInputStream(targetFile)) {
            LOG.debug("Reading file {}", targetFile);

            // copy it to response's OutputStream
            FileCopyUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
            LOG.debug("Retrieved file {}", targetFile);
        } catch (IOException ex) {
            LOG.error("Error reading file. Filename was '{}'", targetFile, ex);
            throw new GenericErrorException("IOError while reading from file");
        } 
     }

    @RequestMapping(value = "/{fileName}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public GenericResponse uploadFile(@PathVariable String fileName, HttpServletRequest request) {
        LOG.debug("handleLogFileUpload({})", fileName);
        
    	File fileStoreDir = new File(fileStoreDirectoryName);
    	
    	if(!fileStoreDir.canWrite()) {
            throw new GenericErrorException("Cannot write files into " + fileStoreDir.getAbsolutePath());
    	}
    	
        if (fileName.contains("/")) {
            throw new GenericErrorException("File name cannot refer to other directories");
        }
        
        File targetFile = new File(fileStoreDir, fileName);

        try(OutputStream os = new FileOutputStream(targetFile)) {
            LOG.debug("Writing file {}", targetFile);
            FileCopyUtils.copy(request.getInputStream(), os);
            os.flush();
            LOG.debug("Wrote file {}", targetFile);
        } catch (IOException ex) {
            LOG.error("Error writing file. Filename was '{}'", targetFile, ex);
            throw new GenericErrorException("IOError while writing file");
        } 

        
        return new GenericResponse(true, "");
    }
}
