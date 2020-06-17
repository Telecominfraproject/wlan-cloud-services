package com.telecominfraproject.wlan.manufacturer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import com.telecominfraproject.wlan.core.client.BaseRemoteClient;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerDetailsRecord;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerOuiDetails;
import com.telecominfraproject.wlan.server.exceptions.GenericErrorException;

/**
 * @author mpreston
 *
 */
@Configuration
public class ManufacturerServiceRemote extends BaseRemoteClient implements ManufacturerInterface {

    private static final Logger LOG = LoggerFactory.getLogger(ManufacturerServiceRemote.class);
    private static final int MAX_OUIS_PER_REQUEST = 200;
    private static final ParameterizedTypeReference<List<ManufacturerOuiDetails>> manufacturerOuiDetailsListClassToken;
    private static final ParameterizedTypeReference<Map<String, ManufacturerOuiDetails>> manufacturerOuiDetailsMapClassToken;
    private static final ParameterizedTypeReference<List<String>> stringListClassToken;
    static {
        manufacturerOuiDetailsListClassToken = new ParameterizedTypeReference<List<ManufacturerOuiDetails>>() {
        };
        stringListClassToken = new ParameterizedTypeReference<List<String>>() {
        };
        manufacturerOuiDetailsMapClassToken = new ParameterizedTypeReference<Map<String, ManufacturerOuiDetails>>() {
        };
    }

    @Autowired
    private ManufacturerAsyncInvokator asyncInvokator;
    private String baseUrl;

    @Override
    public ManufacturerOuiDetails createOuiDetails(ManufacturerOuiDetails ouiDetails) {
        LOG.debug("calling createOuiDetails {} ", ouiDetails);

        HttpEntity<ManufacturerOuiDetails> request = new HttpEntity<>(ouiDetails, headers);

        ResponseEntity<ManufacturerOuiDetails> responseEntity = restTemplate
                .postForEntity(getBaseUrl() + "/oui", request, ManufacturerOuiDetails.class);

        ManufacturerOuiDetails ret = responseEntity.getBody();

        LOG.debug("completed createOuiDetails {} ", ret);

        return ret;
    }

    @Override
    public ManufacturerOuiDetails deleteOuiDetails(String oui) {
        LOG.debug("calling deleteOuiDetails {} ", oui);

        ResponseEntity<ManufacturerOuiDetails> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/oui?oui={oui}", HttpMethod.DELETE, null,
                ManufacturerOuiDetails.class, oui);
        ManufacturerOuiDetails ret = responseEntity.getBody();
        LOG.debug("completed delete {} ", ret);
        return ret;
    }

    @Override
    public List<String> getOuiListForManufacturer(String manufacturer, boolean exactMatch) {
        LOG.debug("calling getOuiListForManufacturer {}, {} ", manufacturer, exactMatch);

        ResponseEntity<List<String>> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/oui/forManufacturer?manufacturer={manufacturer}&exactMatch={exactMatch}",
                HttpMethod.GET, null, stringListClassToken, manufacturer, exactMatch);

        List<String> ret = responseEntity.getBody();

        LOG.debug("completed getOuiListForManufacturer and found {} ", ret);

        return ret;
    }

    @Override
    public ManufacturerOuiDetails getByOui(String oui) {
        LOG.debug("calling getByOui {} ", oui);

        ResponseEntity<ManufacturerOuiDetails> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/oui?oui={pui}", HttpMethod.GET, null, ManufacturerOuiDetails.class, oui);

        ManufacturerOuiDetails ret = responseEntity.getBody();

        LOG.debug("completed getByOui and found {} ", ret);

        return ret;
    }

    @Override
    public ManufacturerDetailsRecord createManufacturerDetails(ManufacturerDetailsRecord clientManufacturerDetails) {
        LOG.debug("calling createManufacturerDetails {} ", clientManufacturerDetails);

        HttpEntity<ManufacturerDetailsRecord> request = new HttpEntity<>(
                clientManufacturerDetails, headers);

        ResponseEntity<ManufacturerDetailsRecord> responseEntity = restTemplate.postForEntity(
                getBaseUrl(), request, ManufacturerDetailsRecord.class);

        ManufacturerDetailsRecord ret = responseEntity.getBody();

        LOG.debug("completed createManufacturerDetails and stored {} ", ret);

        return ret;
    }

    @Override
    public ManufacturerDetailsRecord updateManufacturerDetails(ManufacturerDetailsRecord clientManufacturerDetails) {
        LOG.debug("calling updateManufacturerDetails {} ", clientManufacturerDetails);

        HttpEntity<ManufacturerDetailsRecord> request = new HttpEntity<>(
                clientManufacturerDetails, headers);

        ResponseEntity<ManufacturerDetailsRecord> responseEntity = restTemplate.exchange(
                getBaseUrl(), HttpMethod.PUT, request,
                ManufacturerDetailsRecord.class);

        ManufacturerDetailsRecord ret = responseEntity.getBody();

        LOG.debug("completed updateManufacturerDetails and updated {} ", ret);

        return ret;
    }

    @Override
    public ManufacturerDetailsRecord deleteManufacturerDetails(long id) {
        LOG.debug("calling deleteManufacturerDetails {} ", id);

        ManufacturerDetailsRecord ret = getById(id);

        restTemplate.delete(getBaseUrl() + "?id=" + id);

        LOG.debug("completed deleteManufacturerDetails {} ", ret);
        return ret;
    }

    @Override
    public ManufacturerDetailsRecord getById(long id) {
        LOG.debug("calling getById {} ", id);

        ResponseEntity<ManufacturerDetailsRecord> responseEntity = restTemplate.exchange(
                getBaseUrl() + "?id={id}", HttpMethod.GET, null,
                ManufacturerDetailsRecord.class, id);

        ManufacturerDetailsRecord ret = responseEntity.getBody();

        LOG.debug("completed getById and found {} ", ret);

        return ret;
    }

    @Override
    public List<ManufacturerOuiDetails> getAllManufacturerData() {
        LOG.debug("calling getAllManufacturerData");

        ResponseEntity<List<ManufacturerOuiDetails>> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/oui/all", HttpMethod.GET, null,
                manufacturerOuiDetailsListClassToken);

        List<ManufacturerOuiDetails> ret = responseEntity.getBody();

        LOG.debug("completed getAllManufacturerData and found {} records", ret.size());

        return ret;
    }

    @Override
    public GenericResponse uploadOuiDataFile(String fileName, byte[] base64GzippedContent) {
        LOG.debug("uploadOuiDataFile({})", fileName);
        GenericResponse ret;
        if (null != base64GzippedContent) {
            HttpHeaders fileHeaders = new HttpHeaders();
            fileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            fileHeaders.set("Accept-Encoding", "gzip,deflate");

            HttpEntity<byte[]> request = new HttpEntity<>(base64GzippedContent, fileHeaders);

            ResponseEntity<GenericResponse> responseEntity = restTemplate.postForEntity(
                    getBaseUrl() + "/oui/upload?fileName={fileName}", request, GenericResponse.class,
                    fileName);

            ret = responseEntity.getBody();
        } else {
            ret = new GenericResponse(false, "missing compressed data file content");
        }
        LOG.debug("completed uploadOuiDataFile({}) with result {} ", fileName, ret);

        return ret;
    }

    @Override
    public Map<String, ManufacturerOuiDetails> getManufacturerDetailsForOuiSet(Set<String> ouiSet) {
        LOG.debug("calling getManufacturerDetailsForOuiList, ouiSet.size={}",ouiSet==null?0:ouiSet.size());

        if (CollectionUtils.isEmpty(ouiSet)) {
            return new HashMap<>();
        }

        Map<String, ManufacturerOuiDetails> ret;
        if(ouiSet.size() <= MAX_OUIS_PER_REQUEST) {
            ret = getManufacturueDetailsForOuiSetImpl(ouiSet);
        }
        else {
            List<Future<Map<String, ManufacturerOuiDetails>>> futures = new ArrayList<>();
            Set<String> subSet = new HashSet<>(MAX_OUIS_PER_REQUEST);
            for(String oui: ouiSet) {
                subSet.add(oui);
                if(subSet.size()>=MAX_OUIS_PER_REQUEST) {
                    futures.add(asyncInvokator.getManufacturerDetailsForOuiSet(this,new HashSet<>(subSet)));
                    subSet.clear();
               }
            }
            futures.add(asyncInvokator.getManufacturerDetailsForOuiSet(this,subSet));

            ret = new HashMap<>();
            for(Future<Map<String, ManufacturerOuiDetails>> future : futures) {
                Map<String, ManufacturerOuiDetails> partialResult;
                try {
                    partialResult = future.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new GenericErrorException("Unable to get result", e);
                }
                if(partialResult != null) {
                    ret.putAll(partialResult);
                }
            }

        }

        LOG.debug("completed getManufacturerDetailsForOuiList and found {} records", ret.size());

        return ret;
    }

    Map<String, ManufacturerOuiDetails> getManufacturueDetailsForOuiSetImpl(Set<String> ouiSet) {
        Map<String, ManufacturerOuiDetails> ret;
        
        String ouiSetStr = null;
        if (ouiSet != null && !ouiSet.isEmpty()) {
            ouiSetStr = ouiSet.toString();
            // remove [] around the string, otherwise will get:
            // Failed to convert value of type 'java.lang.String' to required
            // type 'java.util.Set'; nested exception is
            // java.lang.NumberFormatException: For input string: "[690]"
            ouiSetStr = ouiSetStr.substring(1, ouiSetStr.length() - 1);
        }

        ResponseEntity<Map<String, ManufacturerOuiDetails>> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/oui/list?ouiList={ouiSetStr}" , HttpMethod.GET, null,
                manufacturerOuiDetailsMapClassToken, ouiSetStr);

        ret = responseEntity.getBody();
        return ret;
    }

    @Override
    public ManufacturerOuiDetails updateOuiAlias(ManufacturerOuiDetails ouiDetails) {
        LOG.debug("calling updateOuiAlias");

        HttpEntity<ManufacturerOuiDetails> request = new HttpEntity<>(ouiDetails, headers);

        ResponseEntity<ManufacturerOuiDetails> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/oui/alias",
                HttpMethod.PUT, request, ManufacturerOuiDetails.class);

        ManufacturerOuiDetails ret = responseEntity.getBody();

        LOG.debug("completed updateOuiAlias and updated {} ", ret);

        return ret;
    }

    @Override
    public List<String> getAliasValuesThatBeginWith(String prefix, int maxResults) {
        LOG.debug("calling getStoredAliasValuesThatBeginWith");

        List<String> result = new ArrayList<>();
        if (prefix == null || prefix.length() == 0) {
            return result;
        }

        ResponseEntity<List<String>> responseEntity = restTemplate.exchange(getBaseUrl()
                + "/oui/alias?prefix={prefix}&maxResults={maxResults}", HttpMethod.GET, null, stringListClassToken, prefix, maxResults);

        result = responseEntity.getBody();

        LOG.debug("completed getStoredAliasValuesThatBeginWith and found {} records", result.size());

        return result;
    }

    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.manufacturerServiceBaseUrl").trim()+"/api/manufacturer";
        }

    	return baseUrl;
    }

}
