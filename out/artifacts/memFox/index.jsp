<%--
  Created by IntelliJ IDEA.
  User: chenxp
  Date: 14-5-7
  Time: 下午6:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head><title></title>
      <script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
      <script type="text/javascript" src="js/bootstrap/js/bootstrap.min.js"></script>
      <link rel="stylesheet" href="js/bootstrap/css/bootstrap.min.css">
      <script type="text/javascript" src="js/public-function.js"></script>

      <script type="text/javascript">

          $(document).ready(function(){
              var groupName=getQueryString("group");
              reloadMainInfo(groupName, "" , "", "");
              reloadHostInfo();
          });

          function reloadHostInfo()
          {
              $.ajax({
                  type: 'get',
                  url: '/memFox/servlet/getHostlist?val=type=getHost;group=all',
                  success: function(data, status){
                      loadHostMenu(data);
                  }
              })
          }

          function reloadMainInfo(vGroupName, vHostName, vHostIP, vHostPort)
          {
              var strUrl="memFox_MainPage.jsp?groupName=" + vGroupName + "&hostName=" + vHostName + "&hostIP=" + vHostIP + "&hostPort=" + vHostPort;
              $('#ReportPage').load(strUrl);

              var strBtnVal = "";
              if( vGroupName.length > 0)
                   strBtnVal = "分组 " + vGroupName;
              else if( vHostName.length > 0 )
                   strBtnVal = vHostName + "[" + vHostIP + ":" + vHostPort + "]";
              else
                   strBtnVal = "全集群";
              strBtnVal = strBtnVal + "▼";
              $('#btnSelector').val(strBtnVal) ;
          }

          function loadHostMenu(data){
              var ButtonValue="全集群";
              var groupName=getQueryString("group");
              var hostName=getQueryString("hostName");
              if( hostName!="" )
                  ButtonValue=hostName;
              else if(groupName != "")
                  ButtonValue=groupName;

              var hostData = eval(data);
              var strHtml = "<input type=\"button\" id=\"btnSelector\" class=\"btn btn-success dropdown-toggle\" data-toggle=\"dropdown\" value=\"" + ButtonValue +" ▼\" ></input>";
              strHtml = strHtml + "<ul class=\"dropdown-menu\" role=\"menu\">";
              strHtml = strHtml + "<li><a href='index.jsp'>全集群</a></li>";
              var rowInd = 0;
              var curGroupName="";
              while( hostData[rowInd] != null ){
                  if( curGroupName != hostData[rowInd].gName ){
                      strHtml = strHtml + "<li class=\"divider\"></li>";
                      strHtml = strHtml + "<li><a href='index.jsp?group=" + hostData[rowInd].gName + "'>分组：" + hostData[rowInd].gName + "</a></li>"
                      curGroupName = hostData[rowInd].gName;
                  }
                  //strHtml = strHtml + "<li><a href='javascript:void(0)' onclick=\"showMemcachedInfo('','" + hostData[rowInd].hName + "','" + hostData[rowInd].hIP + "','" + hostData[rowInd].hPort + "')\">&nbsp;&nbsp;&nbsp;" +  hostData[rowInd].hName + " [" + hostData[rowInd].hIP + ":" + hostData[rowInd].hPort + "]</a></li>";
                  rowInd++;
              }
              strHtml = strHtml + "</ul>";
              HostSelector.innerHTML = strHtml;
          }
      </script>
  </head>
  <body>

    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">缓存信息系统</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="index.jsp">缓存信息汇总</a></li>
            <li><a href="hostlist.jsp">主机配置</a></li>
            <li><a href="/">返回数据展现系统</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li>
              <div id="HostSelector" class="btn-group" style="margin-top: 8px;">
                  <input id="btnSelector" type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown" value="全集群 ▼" />
                  <ul class="dropdown-menu" role="menu">
                      <li></li>
                  </ul>
              </div>
            </li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>
    <div><br /><br /><br /><br /></div>
    <div>
        <table align="center">
            <tr>
                <td>
                    <div id="ReportPage"></div>
                </td>
            </tr>
        </table>
    </div>

  </body>
</html>