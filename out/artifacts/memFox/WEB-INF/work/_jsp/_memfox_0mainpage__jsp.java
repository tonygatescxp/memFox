/*
 * JSP generated by Resin-3.1.12 (built Mon, 29 Aug 2011 03:22:08 PDT)
 */

package _jsp;
import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;

public class _memfox_0mainpage__jsp extends com.caucho.jsp.JavaPage
{
  private static final java.util.HashMap<String,java.lang.reflect.Method> _jsp_functionMap = new java.util.HashMap<String,java.lang.reflect.Method>();
  private boolean _caucho_isDead;
  
  public void
  _jspService(javax.servlet.http.HttpServletRequest request,
              javax.servlet.http.HttpServletResponse response)
    throws java.io.IOException, javax.servlet.ServletException
  {
    com.caucho.server.webapp.WebApp _jsp_application = _caucho_getApplication();
    javax.servlet.ServletContext application = _jsp_application;
    com.caucho.jsp.PageContextImpl pageContext = _jsp_application.getJspApplicationContext().allocatePageContext(this, _jsp_application, request, response, null, null, 8192, true, false);
    javax.servlet.jsp.PageContext _jsp_parentContext = pageContext;
    javax.servlet.jsp.JspWriter out = pageContext.getOut();
    final javax.el.ELContext _jsp_env = pageContext.getELContext();
    javax.servlet.ServletConfig config = getServletConfig();
    javax.servlet.Servlet page = this;
    response.setContentType("text/html;charset=UTF-8");
    request.setCharacterEncoding("UTF-8");
    try {
      out.write(_jsp_string0, 0, _jsp_string0.length);
    } catch (java.lang.Throwable _jsp_e) {
      pageContext.handlePageException(_jsp_e);
    } finally {
      _jsp_application.getJspApplicationContext().freePageContext(pageContext);
    }
  }

  private java.util.ArrayList _caucho_depends = new java.util.ArrayList();

  public java.util.ArrayList _caucho_getDependList()
  {
    return _caucho_depends;
  }

  public void _caucho_addDepend(com.caucho.vfs.PersistentDependency depend)
  {
    super._caucho_addDepend(depend);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
  }

  public boolean _caucho_isModified()
  {
    if (_caucho_isDead)
      return true;
    if (com.caucho.server.util.CauchoSystem.getVersionId() != 7170261747151080670L)
      return true;
    for (int i = _caucho_depends.size() - 1; i >= 0; i--) {
      com.caucho.vfs.Dependency depend;
      depend = (com.caucho.vfs.Dependency) _caucho_depends.get(i);
      if (depend.isModified())
        return true;
    }
    return false;
  }

  public long _caucho_lastModified()
  {
    return 0;
  }

  public java.util.HashMap<String,java.lang.reflect.Method> _caucho_getFunctionMap()
  {
    return _jsp_functionMap;
  }

  public void init(ServletConfig config)
    throws ServletException
  {
    com.caucho.server.webapp.WebApp webApp
      = (com.caucho.server.webapp.WebApp) config.getServletContext();
    super.init(config);
    com.caucho.jsp.TaglibManager manager = webApp.getJspApplicationContext().getTaglibManager();
    com.caucho.jsp.PageContextImpl pageContext = new com.caucho.jsp.PageContextImpl(webApp, this);
  }

  public void destroy()
  {
      _caucho_isDead = true;
      super.destroy();
  }

  public void init(com.caucho.vfs.Path appDir)
    throws javax.servlet.ServletException
  {
    com.caucho.vfs.Path resinHome = com.caucho.server.util.CauchoSystem.getResinHome();
    com.caucho.vfs.MergePath mergePath = new com.caucho.vfs.MergePath();
    mergePath.addMergePath(appDir);
    mergePath.addMergePath(resinHome);
    com.caucho.loader.DynamicClassLoader loader;
    loader = (com.caucho.loader.DynamicClassLoader) getClass().getClassLoader();
    String resourcePath = loader.getResourcePathSpecificFirst();
    mergePath.addClassPath(resourcePath);
    com.caucho.vfs.Depend depend;
    depend = new com.caucho.vfs.Depend(appDir.lookup("memFox_MainPage.jsp"), 6081296481849708900L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
  }

  private final static char []_jsp_string0;
  static {
    _jsp_string0 = "\r\n\r\n<html>\r\n<head>\r\n    <script type=\"text/javascript\">\r\n\r\n        $(document).ready(function(){\r\n                initPage();\r\n        });\r\n\r\n        function initPage()\r\n        {\r\n            var groupName=getQueryString(\"group\");\r\n            var hostName=getQueryString(\"hostName\");\r\n            var hostIP=getQueryString(\"hostIP\");\r\n            var hostPort=getQueryString(\"hostPort\");\r\n\r\n            loadChartReport(groupName, hostName, hostIP, hostPort);\r\n\r\n            if( groupName != \"\" && hostName==\"\" ){\r\n                loadHostList(groupName);\r\n            }\r\n        }\r\n\r\n        function loadChartReport(groupName, hostName, hostIP, hostPort)\r\n        {\r\n            var styleName=new Array(\"panel-primary\", \"panel-success\", \"panel-info\", \"panel-warning\", \"panel-danger\");\r\n            $.ajax({\r\n                  type: 'get',\r\n                  url: '/memFox/servlet/getHostlist?val=type=gl;dataRequire=yes',\r\n                  success: function(data, status){\r\n                      var gData = eval(data);\r\n\r\n                      var rowInd=0;\r\n                      while( gData[rowInd] != null )\r\n                      {\r\n                          if( hostName == \"\" && groupName != \"\" && gData[rowInd].value == \"all\" ){\r\n                              //\u5982\u679c\u7ec4\u540d\u662fall\uff0c\u5219\r\n                               gData[rowInd].value = groupName;\r\n                               gData[rowInd].text=groupName;\r\n                          }else if( hostName != \"\" ){\r\n                              gData[rowInd].value = hostName;\r\n                              gData[rowInd].text = hostName;\r\n                          }\r\n                          var strUrl = \"memFox_reportTemplate.jsp?hostName=\" + hostName + \"&hostIP=\" + hostIP + \"&hostPort=\" + hostPort + \"&groupName=\";\r\n                          if( gData[rowInd].value != \"all\" && hostName == \"\"){\r\n                              strUrl = strUrl + gData[rowInd].value;\r\n                          }\r\n                          var childContainerDiv=$('<div class=\"container theme-showcase\" style=\"width: 90%\" align=\"center\"></div>');\r\n                          var childPanelDiv = $('<div></div>');\r\n                          var childHeaderDiv = $('<div><span class=\"glyphicon glyphicon-barcode\"></span> <h4>' + gData[rowInd].text + '</h4></div>');\r\n                          var childContentDiv = $('<div><iframe scrolling=\"no\" marginwidth=\"1px\" marginheight=\"1px\" width=\"98%\" height=\"420px\" frameborder=\"0\" src=\"' + strUrl + '\"></ifream></div>');\r\n                          childPanelDiv.addClass(\"panel \" + styleName[rowInd % 4] );\r\n                          childHeaderDiv.addClass(\"panel-heading\");\r\n                          childHeaderDiv.appendTo(childPanelDiv);\r\n                          childContentDiv.appendTo(childPanelDiv);\r\n                          childPanelDiv.appendTo(childContainerDiv);\r\n                          childContainerDiv.appendTo('body');\r\n                          rowInd ++ ;\r\n                          if( groupName != \"\" || hostName != \"\" )\r\n                            break;\r\n                      }\r\n                  }\r\n            });\r\n        }\r\n\r\n        function loadHostList( vGroupName )\r\n        {\r\n            $.ajax({\r\n                  type: 'get',\r\n                  url: '/memFox/servlet/getStatus?val=type=getHostListByGroup;group=' + vGroupName,\r\n                  success: function(data, status){\r\n                      var hData = eval(data);\r\n                      var rowInd=0;\r\n                      var childContainerDiv=$('<div class=\"container theme-showcase\" style=\"width: 90%\" align=\"center\"></div>');\r\n                      var childPanelDiv = $('<div ></div>');\r\n                      childPanelDiv.addClass(\"panel panel-success\");\r\n                      var childHeaderDiv = $('<div class=\\\"panel-heading\\\" style=\"background-color: #660066\"><font color=\"#FFFFFF\"><span class=\"glyphicon glyphicon-list-alt\"></span>  <strong>' + vGroupName + '\u4e3b\u673a\u5217\u8868</strong></font></div>');\r\n                      var strHtml=\"<table class=\\\"table table-striped text-center\\\"><tr><td>\u7f16\u53f7</td><td>\u4e3b\u673a\u540d</td><td>\u603b\u7a7a\u95f4</td><td>\u5df2\u4f7f\u7528\u7a7a\u95f4</td><td>\u547d\u4e2d\u6b21\u6570\uff08\u4e07\uff09</td><td>\u672a\u547d\u4e2d\u6b21\u6570\uff08\u4e07\uff09</td><td>\u66f4\u65b0\u65f6\u95f4</td><td>\u64cd\u4f5c</td></tr>\";\r\n\r\n                      while( hData[rowInd] != null ){\r\n                          strHtml = strHtml + \"<tr>\";\r\n                          strHtml = strHtml + \"<td>\" + (rowInd+1) + \"</td>\";\r\n                          strHtml = strHtml + \"<td>\" + hData[rowInd].hostName + \"\u3010\" + hData[rowInd].hostIP + \":\" + hData[rowInd].hostPort + \"\u3011</td>\";\r\n                          strHtml = strHtml + \"<td>\" + strFormatNumber(hData[rowInd].limit_maxbytes/1024/1024/1024, 2) + \" GB</td>\";\r\n                          strHtml = strHtml + \"<td>\" + strFormatNumber(hData[rowInd].bytes/1024/1024/1024, 2) + \" GB</td>\";\r\n                          strHtml = strHtml + \"<td>\" + strFormatNumber(hData[rowInd].get_hits/10000,0) + \"</td>\";\r\n                          strHtml = strHtml + \"<td>\" + strFormatNumber(hData[rowInd].get_misses/10000,0) + \"</td>\";\r\n                          strHtml = strHtml + \"<td>\" + hData[rowInd].lastUpdate + \"</td>\";\r\n                          strHtml = strHtml + \"<td><input class='btn btn-success' type=\\\"button\\\" value=\\\"\u67e5\u770b\\\" \";\r\n                          strHtml = strHtml + \" onclick=\\\"window.location.href='index.jsp?hostName=\" + hData[rowInd].hostName + \"&\";\r\n                          strHtml = strHtml + \"hostIP=\" + hData[rowInd].hostIP + \"&hostPort=\"  + hData[rowInd].hostPort + \"'\\\" /></td>\" ;\r\n                          strHtml = strHtml + \"</tr>\";\r\n                          rowInd++;\r\n                      }\r\n                      strHtml = strHtml + \"</table>\"\r\n                      var childContentDiv = $('<div>' + strHtml + '</div>');\r\n                      childHeaderDiv.appendTo( childPanelDiv);\r\n                      childContentDiv.appendTo( childPanelDiv);\r\n                      childPanelDiv.appendTo( childContainerDiv);\r\n                      childContainerDiv.appendTo('body');\r\n                  }\r\n            });\r\n        }\r\n\r\n    </script>\r\n</head>\r\n<body>\r\n\r\n</body>\r\n</html>".toCharArray();
  }
}
