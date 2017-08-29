/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.external.cloudflare;

import de.appsolve.padelcampus.external.cloudflare.model.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * @author dominik
 */
@Component
public class CloudFlareApiClient {

    private static final String CLOUDFLARE_API_URL = "https://api.cloudflare.com/client/v4/";

    @Autowired
    RestTemplate cloudFlareApiRestTemplate;

    public String addCnameRecord(String subDomainName, String cloudFlareDomainName, String originalDomainName) throws CloudFlareApiException {
        String subDomainEntry = subDomainName + "." + cloudFlareDomainName;
        DnsRecord dnsRecord = new DnsRecord();
        dnsRecord.setType("CNAME");
        dnsRecord.setName(subDomainEntry);
        dnsRecord.setContent(originalDomainName);
        dnsRecord.setProxied(true);

        ZonesResponse zonesResponse = getZones(cloudFlareDomainName);
        List<Zone> result = zonesResponse.getResult();
        Zone zone = result.get(0);
        DnsRecordsResponse dnsRecordsResponse = getDnsRecords(zone.getId(), "CNAME", subDomainEntry);
        if (dnsRecordsResponse.getResult().isEmpty()) {
            postDnsRecord(zone.getId(), dnsRecord);
        }
        return subDomainEntry;
    }

    private ZonesResponse getZones(String dnsName) throws CloudFlareApiException {
        ZonesResponse response = cloudFlareApiRestTemplate.getForObject(CLOUDFLARE_API_URL + "zones?status=active&name=" + dnsName, ZonesResponse.class);
        if (!response.isSuccess()) {
            throw new CloudFlareApiException(response);
        }
        return response;
    }

    private DnsRecordsResponse getDnsRecords(String zoneId, String recordType, String recordName) throws CloudFlareApiException {
        String url = getDnsEntriesUrl(zoneId, recordType, recordName);
        DnsRecordsResponse response = cloudFlareApiRestTemplate.getForObject(url, DnsRecordsResponse.class);
        if (!response.isSuccess()) {
            throw new CloudFlareApiException(response);
        }
        return response;
    }

    private String getDnsEntriesUrl(String zoneId, String recordType, String recordName) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(CLOUDFLARE_API_URL + "zones/" + zoneId + "/dns_records");
        if (!StringUtils.isEmpty(recordType)) {
            builder.queryParam("type", recordType);
        }
        if (!StringUtils.isEmpty(recordName)) {
            builder.queryParam("name", recordName);
        }
        return builder.toUriString();
    }

    private CloudFlareApiResponse postDnsRecord(String zoneId, DnsRecord dnsRecord) throws CloudFlareApiException {
        String url = getDnsEntriesUrl(zoneId, null, null);
        CloudFlareApiResponse response = cloudFlareApiRestTemplate.postForObject(url, dnsRecord, CloudFlareApiResponse.class);
        if (!response.isSuccess()) {
            throw new CloudFlareApiException(response);
        }
        return response;
    }
}
