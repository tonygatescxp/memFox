<%--
  Created by IntelliJ IDEA.
  User: chenxp
  Date: 14-5-19
  Time: 下午2:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="text/javascript">

        $(document).ready(function(){
                initPage();
        });

        function initPage()
        {
            var groupName=getQueryString("group");
            var hostName=getQueryString("hostName");
            var hostIP=getQueryString("hostIP");
            var hostPort=getQueryString("hostPort");

            loadChartReport(groupName, hostName, hostIP, hostPort);

            if( groupName != "" && hostName=="" ){
                loadHostList(groupName);
            }
        }

        function loadChartReport(groupName, hostName, hostIP, hostPort)
        {
            var styleName=new Array("panel-primary", "panel-success", "panel-info", "panel-warning", "panel-danger");
            $.ajax({
                  type: 'get',
                  url: '/memFox/servlet/getHostlist?val=type=gl;dataRequire=yes',
                  success: function(data, status){
                      var gData = eval(data);

                      var rowInd=0;
                      while( gData[rowInd] != null )
                      {
                          if( hostName == "" && groupName != "" && gData[rowInd].value == "all" ){
                              //如果组名是all，则
                               gData[rowInd].value = groupName;
                               gData[rowInd].text=groupName;
                          }else if( hostName != "" ){
                              gData[rowInd].value = hostName;
                              gData[rowInd].text = hostName;
                          }
                          var strUrl = "memFox_reportTemplate.jsp?hostName=" + hostName + "&hostIP=" + hostIP + "&hostPort=" + hostPort + "&groupName=";
                          if( gData[rowInd].value != "all" && hostName == ""){
                              strUrl = strUrl + gData[rowInd].value;
                          }
                          var childContainerDiv=$('<div class="container theme-showcase" style="width: 90%" align="center"></div>');
                          var childPanelDiv = $('<div></div>');
                          var childHeaderDiv = $('<div><span class="glyphicon glyphicon-barcode"></span> <h4>' + gData[rowInd].text + '</h4></div>');
                          var childContentDiv = $('<div><iframe scrolling="no" marginwidth="1px" marginheight="1px" width="98%" height="420px" frameborder="0" src="' + strUrl + '"></ifream></div>');
                          childPanelDiv.addClass("panel " + styleName[rowInd % 4] );
                          childHeaderDiv.addClass("panel-heading");
                          childHeaderDiv.appendTo(childPanelDiv);
                          childContentDiv.appendTo(childPanelDiv);
                          childPanelDiv.appendTo(childContainerDiv);
                          childContainerDiv.appendTo('body');
                          rowInd ++ ;
                          if( groupName != "" || hostName != "" )
                            break;
                      }
                  }
            });
        }

        function loadHostList( vGroupName )
        {
            $.ajax({
                  type: 'get',
                  url: '/memFox/servlet/getStatus?val=type=getHostListByGroup;group=' + vGroupName,
                  success: function(data, status){
                      var hData = eval(data);
                      var rowInd=0;
                      var childContainerDiv=$('<div class="container theme-showcase" style="width: 90%" align="center"></div>');
                      var childPanelDiv = $('<div ></div>');
                      childPanelDiv.addClass("panel panel-success");
                      var childHeaderDiv = $('<div class=\"panel-heading\" style="background-color: #660066"><font color="#FFFFFF"><span class="glyphicon glyphicon-list-alt"></span>  <strong>' + vGroupName + '主机列表</strong></font></div>');
                      var strHtml="<table class=\"table table-striped text-center\"><tr><td>编号</td><td>主机名</td><td>总空间</td><td>已使用空间</td><td>命中次数（万）</td><td>未命中次数（万）</td><td>更新时间</td><td>操作</td></tr>";

                      while( hData[rowInd] != null ){
                          strHtml = strHtml + "<tr>";
                          strHtml = strHtml + "<td>" + (rowInd+1) + "</td>";
                          strHtml = strHtml + "<td>" + hData[rowInd].hostName + "【" + hData[rowInd].hostIP + ":" + hData[rowInd].hostPort + "】</td>";
                          strHtml = strHtml + "<td>" + strFormatNumber(hData[rowInd].limit_maxbytes/1024/1024/1024, 2) + " GB</td>";
                          strHtml = strHtml + "<td>" + strFormatNumber(hData[rowInd].bytes/1024/1024/1024, 2) + " GB</td>";
                          strHtml = strHtml + "<td>" + strFormatNumber(hData[rowInd].get_hits/10000,0) + "</td>";
                          strHtml = strHtml + "<td>" + strFormatNumber(hData[rowInd].get_misses/10000,0) + "</td>";
                          strHtml = strHtml + "<td>" + hData[rowInd].lastUpdate + "</td>";
                          strHtml = strHtml + "<td><input class='btn btn-success' type=\"button\" value=\"查看\" ";
                          strHtml = strHtml + " onclick=\"window.location.href='index.jsp?hostName=" + hData[rowInd].hostName + "&";
                          strHtml = strHtml + "hostIP=" + hData[rowInd].hostIP + "&hostPort="  + hData[rowInd].hostPort + "'\" /></td>" ;
                          strHtml = strHtml + "</tr>";
                          rowInd++;
                      }
                      strHtml = strHtml + "</table>"
                      var childContentDiv = $('<div>' + strHtml + '</div>');
                      childHeaderDiv.appendTo( childPanelDiv);
                      childContentDiv.appendTo( childPanelDiv);
                      childPanelDiv.appendTo( childContainerDiv);
                      childContainerDiv.appendTo('body');
                  }
            });
        }

    </script>
</head>
<body>

</body>
</html>