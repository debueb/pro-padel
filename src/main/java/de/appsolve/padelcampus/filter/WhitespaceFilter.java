/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * This filter class removes any whitespace from the response. It actually trims
 * all leading and trailing spaces or tabs and newlines before writing to the
 * response stream. This will greatly save the network bandwith, but this will
 * make the source of the response more hard to read.
 * <p>
 * This filter should be configured in the web.xml as follows:
 * <pre>
 * &lt;filter&gt;
 *     &lt;description&gt;
 *         This filter class removes any whitespace from the response. It actually trims all
 *         leading and trailing spaces or tabs and newlines before writing to the response stream.
 *         This will greatly save the network bandwith, but this will make the source of the
 *         response more hard to read.
 *     &lt;/description&gt;
 *     &lt;filter-name&gt;whitespaceFilter&lt;/filter-name&gt;
 *     &lt;filter-class&gt;net.balusc.webapp.WhitespaceFilter&lt;/filter-class&gt;
 * &lt;/filter&gt;
 * &lt;filter-mapping&gt;
 *     &lt;filter-name&gt;whitespaceFilter&lt;/filter-name&gt;
 *     &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 *
 * @author BalusC
 * @author mat - modifications
 * @link http://balusc.blogspot.com/2007/12/whitespacefilter.html
 */
public class WhitespaceFilter implements Filter {

    // Constants ----------------------------------------------------------------------------------
    // Specify here where you'd like to start/stop the trimming.
    // You may want to replace this by init-param and initialize in init() instead.
    static final String[] START_TRIM_AFTER = {"<html", "</textarea", "</pre"};
    static final String[] STOP_TRIM_AFTER = {"</html", "<textarea", "<pre"};
    private final static Logger LOGGER = Logger.getLogger(WhitespaceFilter.class.getName());

    // Actions ------------------------------------------------------------------------------------

    /**
     * @param config
     * @throws javax.servlet.ServletException
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        //
    }

    /**
     * @param request
     * @param response
     * @param chain
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            WhitespaceFilterWrapper wrapper = new WhitespaceFilterWrapper(httpResponse);
            chain.doFilter(request, wrapper);
            httpResponse.getWriter().write(wrapper.getCaptureAsString());
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
        //
    }

}