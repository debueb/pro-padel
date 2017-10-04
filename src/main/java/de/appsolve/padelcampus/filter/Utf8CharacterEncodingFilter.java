package de.appsolve.padelcampus.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

public class Utf8CharacterEncodingFilter extends CharacterEncodingFilter {

  public Utf8CharacterEncodingFilter() {
    super(StandardCharsets.UTF_8.name(), true, true);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String url = request.getRequestURI();
    if (!StringUtils.isEmpty(url) && (url.startsWith("/images/image") || url.startsWith("/pro/images") || url.startsWith("/pro/video") || url.startsWith("/static/css/fonts"))) {
      return true;
    }
    return super.shouldNotFilter(request);
  }
}
