/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.listener;

import de.appsolve.padelcampus.utils.HttpUtil;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.ReflectionException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class ContextStartupListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = Logger.getLogger(ContextStartupListener.class);

    @Override
    /*
        we attempt to speed up the access time for the first user by doing the first request
    */
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        //we need to delay the first request a couple of seconds as the context actually will not have finished initialization by the time this event fires
        scheduledExecutorService.schedule(new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    List<String> endpoints = getEndPoints();
                    if (!endpoints.isEmpty()) {

                        HttpGet httpget = new HttpGet(endpoints.get(0));
                        log.info("Requesting "+endpoints.get(0) +" to speed up first access response time.");
                        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                            @Override
                            public String handleResponse(final HttpResponse response) throws IOException {
                                int status = response.getStatusLine().getStatusCode();
                                if (status >= 200 && status < 300) {
                                    HttpEntity entity = response.getEntity();
                                    return entity != null ? EntityUtils.toString(entity) : null;
                                } else {
                                    log.warn("Unexpected status code: " + status);
                                }
                                return null;
                            }
                        };
                        HttpUtil.getHttpClient().execute(httpget, responseHandler);
                    }
                } catch (IOException ex) {
                    log.warn("Exception while processing answer: " + ex.getMessage());
                }
                return null;
            }
        },
        5,
        TimeUnit.SECONDS);
    }

    List<String> getEndPoints() {
        ArrayList<String> endPoints = new ArrayList<>();
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            Set<ObjectName> objs = mbs.queryNames(new ObjectName("*:type=Connector,*"), Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
            String hostname = InetAddress.getLocalHost().getHostName();
            InetAddress[] addresses = InetAddress.getAllByName(hostname);
            for (ObjectName obj : objs) {
                String scheme = mbs.getAttribute(obj, "scheme").toString();
                String port = obj.getKeyProperty("port");
                for (InetAddress addr : addresses) {
                    if (InetAddressUtils.isIPv4Address(addr.getHostAddress())) {
                        String host = addr.getHostAddress();
                        String ep = scheme + "://" + host + ":" + port;
                        endPoints.add(ep);
                    }
                }
            }
        } catch (MalformedObjectNameException | MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | UnknownHostException ex) {
            log.warn("Error while trying to get servlet container hostname and port: " + ex.getMessage());
        }
        return endPoints;
    }
}
