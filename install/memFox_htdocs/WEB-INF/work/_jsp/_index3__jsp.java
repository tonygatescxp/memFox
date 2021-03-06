/*
 * JSP generated by Resin-3.1.12 (built Mon, 29 Aug 2011 03:22:08 PDT)
 */

package _jsp;
import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;

public class _index3__jsp extends com.caucho.jsp.JavaPage
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
    depend = new com.caucho.vfs.Depend(appDir.lookup("index3.jsp"), -2705107356172549666L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
  }

  private final static char []_jsp_string0;
  static {
    _jsp_string0 = "\n\n<html>\n  <head><title></title>\n      <script type=\"text/javascript\" src=\"js/jquery-1.7.2.min.js\"></script>\n      <script type=\"text/javascript\" src=\"js/operamasks-ui.min.js\"></script>\n      <link rel=\"stylesheet\" href=\"css/default/om-default.css\">\n      <script type=\"text/javascript\" src=\"js/bootstrap/js/bootstrap.min.js\"></script>\n      <link rel=\"stylesheet\" href=\"js/bootstrap/css/bootstrap.min.css\">\n      <script type=\"text/javascript\" src=\"js/highcharts/highcharts.js\"></script>\n\n      <script type=\"text/javascript\">\n          $(document).ready(function(){\n              $('#pieChart').highcharts({\n                      chart: {\n                            plotBackgroundColor: null,\n                            plotBorderWidth: null,\n                            plotShadow: false,\n                            height: 360\n                        },\n                        title: {\n                            text: ''\n                        },\n                        tooltip: {\n                            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'\n                        },\n                        plotOptions: {\n                            pie: {\n                                allowPointSelect: true,\n                                cursor: 'pointer',\n                                dataLabels: {\n                                    enabled: true,\n                                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',\n                                    style: {\n                                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'\n                                    }\n                                }\n                            }\n                        },\n                        series: [{\n                            type: 'pie',\n                            name: 'SpaceUsed',\n                            data: [{name :'null', y:100, color: '#5B9BD5'}]\n                        },{\n                            type: 'pie',\n                            name: 'Operat',\n                            size: 150,\n                            data: [{name: 'null', y:5300, color: '#70AD47'}]\n                        },{\n                            type: 'pie',\n                            name: 'Cached',\n                            size: 80,\n                            data: [{name: 'null', y:100, color: '#ED7D31'}]\n                        }\n                        ]\n                      });\n\n              showMemcachedInfo(\"\", \"\" , \"\", \"\");\n              reloadHostMenu();\n          });\n\n          function strFormatNumber(s, n) {\n              n2=n;\n              n = n > 0 && n <= 20 ? n : 1;\n              s = parseFloat((s + \"\").replace(/[^\\d\\.-]/g, \"\")).toFixed(n) + \"\";\n              var l = s.split(\".\")[0].split(\"\").reverse(), r = s.split(\".\")[1];\n              t = \"\";\n              for (i = 0; i < l.length; i++) {\n                  t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? \",\" : \"\");\n              }\n              var ret= t.split(\"\").reverse().join(\"\") ;\n              if(n2>0)\n                  ret = ret + \".\" + r;\n              return ret;\n          }\n\n          function showMemcachedInfo(vGroupName, vHostName, vHostIP, vHostPort)\n          {\n              var gName=vGroupName;\n              var hIP=vHostIP;\n              var hPort=vHostPort;\n\n              $.ajax({\n                   type: 'get',\n                   url: \"/memFox/servlet/getStatus?val=type=getHostData;group=\" + gName + \";hostIP=\" + hIP + \";hostPort=\" + hPort ,\n                   success: function(data, status){\n                       var hostData = eval(data);\n                        tbl_info_chunkssize.innerHTML = strFormatNumber(hostData[0].chunk_size,0);\n                        tbl_info_chunksperpage.innerHTML = strFormatNumber(hostData[0].chunks_per_page, 0);\n                        tbl_info_chunksinfo.innerHTML = strFormatNumber(hostData[0].used_chunks,0) + \" / \" + strFormatNumber(hostData[0].free_chunks,0) + \" / \" + strFormatNumber(hostData[0].total_chunks, 0);\n                        tbl_info_items.innerHTML = strFormatNumber(hostData[0].total_items,0);\n                        tbl_info_pages.innerHTML = strFormatNumber(hostData[0].total_pages, 0);\n                        tbl_info_eviction.innerHTML = strFormatNumber(hostData[0].evictions, 0);\n                        tbl_info_cmd_get.innerHTML = strFormatNumber(hostData[0].cmd_get, 0);\n                        tbl_info_cmd_set.innerHTML = strFormatNumber(hostData[0].cmd_set, 0);\n                        tbl_info_threads.innerHTML = strFormatNumber(hostData[0].threads, 0);\n                        tbl_info_connections.innerHTML = strFormatNumber(hostData[0].curr_connections,0);\n                        tbl_info_get_hits.innerHTML = strFormatNumber(hostData[0].get_hits, 0);\n                        tbl_info_get_misses.innerHTML = strFormatNumber(hostData[0].get_misses, 0);\n                        tbl_info_total_size.innerHTML = strFormatNumber(hostData[0].limit_maxbytes/1024/1024/1024, 2) + \" GB\";\n                        tbl_info_total_malloced.innerHTML = strFormatNumber(hostData[0].bytes/1024/1024/1024, 2) + \" GB\";\n\n                       reloadPieChart(hostData[0].bytes, hostData[0].limit_maxbytes - hostData[0].bytes,\n                                        hostData[0].cmd_set, hostData[0].cmd_get,\n                                        hostData[0].get_hits, hostData[0].get_misses );\n\n                       var strBtnVal = \"\";\n                       if( vGroupName.length > 0)\n                            strBtnVal = \"\u5206\u7ec4 \" + vGroupName;\n                       else if( vHostName.length > 0 )\n                            strBtnVal = vHostName + \"[\" + vHostIP + \":\" + vHostPort + \"]\";\n                       else\n                            strBtnVal = \"\u5168\u96c6\u7fa4\";\n                       strBtnVal = strBtnVal + \"\u25bc\";\n                       $('#btnSelector').val(strBtnVal) ;\n                   }\n               });\n          }\n\n          function reloadPieChart(pUsed, pFree, pSet, pGet, pHits, pMisses)\n          {\n              var vPieChart = $('#pieChart').highcharts();\n              vPieChart.showLoading('\u6570\u636e\u52a0\u8f7d\u4e2d');\n              while(vPieChart.series.length){\n                  vPieChart.series[0].remove();\n              }\n\n              var SpaceValue=eval(\"[{name:'Used', y:\"+pUsed+\", color:'#5B9BD5'},{name:'Free', y:\"+pFree+\", color: '#ED1C24'}]\");\n              var OperatValue=eval(\"[{name:'Set', y:\"+pSet+\", color:'#70AD47'},{name:'Get', y:\"+pGet+\", color: '#ED7D31'}]\");\n              var CachedValue=eval(\"[{name:'Hits', y:\"+pHits+\", color:'#7FFF00'},{name:'Misses', y:\"+pMisses+\", color: '#808000'}]\");\n\n              vPieChart.addSeries({name: '\u7a7a\u95f4\u4f7f\u7528\u7387',type: 'pie', data: SpaceValue });\n              vPieChart.addSeries({name: '\u64cd\u4f5c\u4f7f\u7528\u7387',type: 'pie',size: 180, data: OperatValue });\n              vPieChart.addSeries({name: '\u7f13\u5b58\u547d\u4e2d\u7387',type: 'pie',size: 90, data: CachedValue });\n\n              vPieChart.hideLoading();\n          }\n\n          function reloadHostMenu()\n          {\n              $.ajax({\n                  type: 'get',\n                  url: '/memFox/servlet/getHostlist?val=type=getHost;group=all',\n                  success: function(data, status){\n                      var hostData = eval(data);\n                      var strHtml = \"<input type=\\\"button\\\" id=\\\"btnSelector\\\" class=\\\"btn btn-success dropdown-toggle\\\" data-toggle=\\\"dropdown\\\" value=\\\"\u5168\u96c6\u7fa4 \u25bc\\\" ></input>\";\n                      strHtml = strHtml + \"<ul class=\\\"dropdown-menu\\\" role=\\\"menu\\\">\";\n                      strHtml = strHtml + \"<li><a href='javascript:void(0)' onclick=\\\"showMemcachedInfo('','','','')\\\">\u5168\u96c6\u7fa4</a></li>\";\n                      var rowInd = 0;\n                      var curGroupName=\"\";\n                      while( hostData[rowInd] != null ){\n                          if( curGroupName != hostData[rowInd].gName ){\n                              strHtml = strHtml + \"<li class=\\\"divider\\\"></li>\";\n                              strHtml = strHtml + \"<li><a href='javascript:void(0)' onclick=\\\"showMemcachedInfo('\" + hostData[rowInd].gName + \"','','','')\\\">\u5206\u7ec4\uff1a\" + hostData[rowInd].gName + \"</a></li>\"\n                              curGroupName = hostData[rowInd].gName;\n                          }\n                          strHtml = strHtml + \"<li><a href='javascript:void(0)' onclick=\\\"showMemcachedInfo('','\" + hostData[rowInd].hName + \"','\" + hostData[rowInd].hIP + \"','\" + hostData[rowInd].hPort + \"')\\\">&nbsp;&nbsp;&nbsp;\" +  hostData[rowInd].hName + \" [\" + hostData[rowInd].hIP + \":\" + hostData[rowInd].hPort + \"]</a></li>\";\n                          rowInd++;\n                      }\n                      strHtml = strHtml + \"</ul>\"\n                      HostSelector.innerHTML = strHtml;\n                  }\n              })\n          }\n      </script>\n  </head>\n  <body>\n\n    <div class=\"navbar navbar-default navbar-static-top\" role=\"navigation\">\n      <div class=\"container\">\n\n        <div class=\"navbar-header\">\n          <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\".navbar-collapse\">\n            <span class=\"sr-only\">Toggle navigation</span>\n            <span class=\"icon-bar\"></span>\n            <span class=\"icon-bar\"></span>\n            <span class=\"icon-bar\"></span>\n          </button>\n          <a class=\"navbar-brand\" href=\"#\">\u7f13\u5b58\u4fe1\u606f\u7cfb\u7edf</a>\n        </div>\n        <div class=\"navbar-collapse collapse\">\n          <ul class=\"nav navbar-nav\">\n            <li class=\"active\"><a href=\"index3.jsp\">\u7f13\u5b58\u7ba1\u7406</a></li>\n            <li><a href=\"hostlist.jsp\">\u4e3b\u673a\u914d\u7f6e</a></li>\n            <li><a href=\"/\">\u8fd4\u56de\u6570\u636e\u5c55\u73b0\u7cfb\u7edf</a></li>\n          </ul>\n          <ul class=\"nav navbar-nav navbar-right\">\n            <li>\n              <div id=\"HostSelector\" class=\"btn-group\" style=\"margin-top: 8px;\">\n                  <input id=\"btnSelector\" type=\"button\" class=\"btn btn-success dropdown-toggle\" data-toggle=\"dropdown\" value=\"\u5168\u96c6\u7fa4 \u25bc\" />\n                  <ul class=\"dropdown-menu\" role=\"menu\">\n                      <li></li>\n                  </ul>\n              </div>\n            </li>\n          </ul>\n        </div><!--/.nav-collapse -->\n      </div>\n    </div>\n        <table width=\"80%\" align=\"center\">\n            <tr>\n                <td>\n                    <div class=\"container theme-showcase\" style=\"width:620px\" align=\"center\">\n                        <div class=\"panel panel-primary\">\n                            <div class=\"panel-heading\"><span class=\"glyphicon glyphicon-exclamation-sign\"></span><strong>  Memcached\u4fe1\u606f</strong></div>\n                            <table class=\"table table-striped table-bordered text-center\">\n                               <tr>\n                                   <td></td>\n                               </tr>\n                               <tr>\n                                   <td>chunks\u5927\u5c0f</td><td><div id=\"tbl_info_chunkssize\" ></div></td>\n                                   <td>\u6bcf\u9875chunks\u6570</td><td><div id=\"tbl_info_chunksperpage\" ></div></td>\n                               </tr>\n                               <tr>\n                                   <td colspan=\"2\">chunks \u5df2\u7528\u6570/\u53ef\u7528\u6570/\u603b\u6570</td>\n                                   <td colspan=\"2\"><div><div id=\"tbl_info_chunksinfo\"></div></div></td>\n                               </tr>\n                               <tr>\n                                   <td>\u6570\u636e\u91cf\uff08items\uff09</td><td><div id=\"tbl_info_items\"></div></td>\n                                   <td>\u9875\u9762\u6570\uff08pages\uff09</td><td><div id=\"tbl_info_pages\"></div></td>\n                               </tr>\n                               <tr>\n                                   <td colspan=\"2\">\u6dd8\u6c70\u91cf\uff08evictions\uff09</td><td colspan=\"2\"><div id=\"tbl_info_eviction\"></div></td>\n                               </tr>\n                               <tr>\n                                   <td>\u7ebf\u7a0b\uff08Threads\uff09</td><td><div id=\"tbl_info_threads\"></div></td>\n                                   <td>\u8fde\u63a5\u6570\uff08Connections\uff09</td><td><div id=\"tbl_info_connections\"></div></td>\n                               </tr>\n                               <tr>\n                                   <td colspan=\"4\"><font color=\"white\">-</font></td>\n                               </tr>\n                               <tr>\n                                   <td>get\u6b21\u6570</td><td><div id=\"tbl_info_cmd_get\"></div></td>\n                                   <td>set\u6b21\u6570</td><td><div id=\"tbl_info_cmd_set\"></div></td>\n                               </tr>\n                               <tr>\n                                   <td>get\u547d\u4e2d\u6b21\u6570</td><td><div id=\"tbl_info_get_hits\"></div></td>\n                                   <td>get\u672a\u547d\u4e2d\u6b21\u6570</td><td><div id=\"tbl_info_get_misses\"></div></td>\n                               </tr>\n                               <tr>\n                                   <td>\u603b\u7a7a\u95f4</td><td><div id=\"tbl_info_total_size\"></div></td>\n                                   <td>\u7a7a\u95f4\u5df2\u5206\u914d</td><td><div id=\"tbl_info_total_malloced\"></div></td>\n                               </tr>\n                           </table>\n                        </div>\n                    </div>\n                </td>\n                <td width=\"30%\">\n                    <div class=\"container theme-showcase\" style=\"width:480px\" align=\"center\">\n                        <div class=\"panel panel-danger\">\n                            <div class=\"panel-heading\"><span class=\"glyphicon glyphicon-indent-left\"></span><strong> \u5206\u6790\u56fe</strong></div>\n                            <div id=\"pieChart\"></div>\n                        </div>\n                    </div>\n                </td>\n            </tr>\n        </table>\n    </div>\n    <div>\n\n    </div>\n  </body>\n</html>".toCharArray();
  }
}
