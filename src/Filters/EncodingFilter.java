package Filters;

import javax.servlet.*;
import java.io.IOException;


public class EncodingFilter implements Filter {
 
 protected String encoding = null;

 @Override
 public void destroy() {
  this.encoding = null;
 }

 @Override
 public void doFilter(ServletRequest req, ServletResponse res,
   FilterChain fc) throws IOException, ServletException {
  req.setCharacterEncoding(encoding);
  res.setContentType("text/html; charset=" + encoding);
  fc.doFilter(req, res);
 }

 @Override
 public void init(FilterConfig filterConfig) throws ServletException {
  this.encoding = filterConfig.getInitParameter("encoding");
 }
}
