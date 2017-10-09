package de.appsolve.padelcampus.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class Utf8CharacterEncodingFilter extends CharacterEncodingFilter {

  private static Pattern[] NO_UTF8_HEADER_PATTERNS = new Pattern[]{
    Pattern.compile("^/images/image.*$"),
    Pattern.compile("^/pro/images.*$"),
    Pattern.compile("^/pro/video.*$"),
    Pattern.compile(".*\\.eot"),
    Pattern.compile(".*\\.svg"),
    Pattern.compile(".*\\.ttf"),
    Pattern.compile(".*\\.woff"),
    Pattern.compile(".*\\.png"),
    Pattern.compile(".*\\.jpg"),
    Pattern.compile(".*\\.jpeg"),
    Pattern.compile(".*\\.gif"),
  };


  public Utf8CharacterEncodingFilter() {
    super(StandardCharsets.UTF_8.name(), true, true);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String url = request.getRequestURI();
    if (!StringUtils.isEmpty(url)) {
      for (Pattern pattern : NO_UTF8_HEADER_PATTERNS) {
        if (pattern.matcher(url).matches()) {
          return true;
        }
      }
    }
    return super.shouldNotFilter(request);
  }
}
