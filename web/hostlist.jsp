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
      <script type="text/javascript" src="js/operamasks-ui.min.js"></script>
      <link rel="stylesheet" href="css/elegant/om-elegant.css">
      <script type="text/javascript" src="js/bootstrap/js/bootstrap.min.js"></script>
      <link rel="stylesheet" href="js/bootstrap/css/bootstrap.min.css">

      <script type="text/javascript">
          $(document).ready(function(){
              $('#cboGroup').omCombo({
                    onValueChange : function(target, newValue, oldValue, event){
                         showHostList();
                    }
              });

              $( "#dialog-addGroup").omDialog({
                    autoOpen: false,
                    modal: true
              });

              $( "#dialog-modifyGroup").omDialog({
                    autoOpen: false,
                    modal: true
              });

              $( "#dialog-addHost").omDialog({
                    autoOpen: false,
                    modal: true
              });
              reloadGroup("");
              showHostList("全部","1");
          });

          function showAddGroup(){
              $( "#dialog-addGroup").omDialog('open');
              $('#AGGroupName').val('');
          }

          function showModifyGroup(){
              var gName = getCurGroupName();
              if( gName == "全部" || gName == "" )
              {
                  $.omMessageBox.alert({
                        content:'请先选择要添加的组，不能是全部或留空'
                  });
                  return false;
              }

              $("#MGGroupName").val(gName);
              $( "#dialog-modifyGroup").omDialog('open');
          }

          function getCurGroupName()
          {
              var gName = $('#btnSelector').val();
              gName = gName.split(" ", 1);
              return gName;
          }

          function showHostList(cmbGroupName, pageNumber)
          {
              if( isNaN(pageNumber) || pageNumber=="")
                    pageNumber = "1";
              pageNumber = parseInt(pageNumber);

              $.ajax({
                   type: 'get',
                   url: "/memFox/servlet/getHostlist?val=type=getHost;group=" + cmbGroupName + ";page=" + pageNumber + ";rnd=" + Math.random(),
                   success: function(data, status){
                       var hostData = eval(data);
                       var strHtml = "<table class=\"table table-striped text-center\">";
                       strHtml = strHtml + "<tr><td>编号</td><td>分组名</td><td>主机名</td><td>服务IP</td><td>服务端口</td><td>操作</td></tr>";
                       var rowInd = 1;
                       while( hostData[rowInd] != null ){
                           //alert(hostData[rowInd].hIP);
                           strHtml = strHtml + "<tr><td>" + ((pageNumber-1)*20+rowInd) + "</td>";
                           strHtml = strHtml + "<td>" + hostData[rowInd].gName + "</td>";
                           strHtml = strHtml + "<td>" + hostData[rowInd].hName + "</td>";
                           strHtml = strHtml + "<td>" + hostData[rowInd].hIP + "</td>";
                           strHtml = strHtml + "<td>" + hostData[rowInd].hPort + "</td>";
                           strHtml = strHtml + "<td><input class=\"btn btn-danger\" type=\"button\" value=\"删除\"";
                           strHtml = strHtml + "onclick=\"showDeleteConfirm('" + hostData[rowInd].hIP + "','" + hostData[rowInd].hPort + "')\"></td>";
                           strHtml = strHtml + "</tr>";
                           rowInd++;
                       }
                       strHtml = strHtml + "</table>";

                       var pCount=hostData[0].pageCount;
                       strHtml = strHtml + "<table class=\"table text-center\">";
                       if( pCount > 1 )
                       {
                           strHtml = strHtml + "<tr><td>";
                           strHtml = strHtml + "<input class='btn btn-success' type=\"button\" value=\"上一页\"";
                           strHtml = strHtml + " onclick=\"showHostList('" + cmbGroupName + "','" + (parseInt(pageNumber)-1) + "')\"";
                           if(pageNumber<2){
                                strHtml = strHtml + " disabled=\"disable\"";
                           }
                           strHtml = strHtml + " /></td>";
                           strHtml = strHtml + "<td>" + pageNumber + " / " + pCount + "</td>";

                           strHtml = strHtml + "<td>" + "<input class='btn btn-success' type=\"button\" value=\"下一页\"";
                           strHtml = strHtml + " onclick=\"showHostList('" + cmbGroupName + "','" + (parseInt(pageNumber)+1) + "')\"";
                           if( pageNumber>=pCount){
                                strHtml = strHtml + " disabled=\"disable\"";
                           }
                           strHtml = strHtml + " /></td>"
                           strHtml = strHtml + "</tr>"
                       }
                       strHtml = strHtml + "</table>";
                       tblHost.innerHTML=strHtml;
                   }
               });

              $('#btnSelector').val(cmbGroupName + " ▼");

              return;
          }

          function showAddHost()
          {
              if( getCurGroupName() == "全部" || getCurGroupName() == "" )
              {
                  $.omMessageBox.alert({
                        content:'请先选择要添加的组，不能是全部或留空'
                  });
                  return false;
              }
              $( "#dialog-addHost").omDialog('open');
              $( "#AHGroupName").val(getCurGroupName());
              $( "#AHHostName").val('');
              $( "#AHHostIP").val('');
              $( "#AHHostPort").val('');
          }

          function showAddMultiHost()
          {
              if( getCurGroupName() == "全部" || getCurGroupName() == "" )
              {
                  showMsgbox("错误", "<p>请先选择要添加的组，不能是<font color=red>全部</font>或<font color=red>留空</font></p>");
                  return false;
              }

              titleGroupName.innerText = "组：" + getCurGroupName();
              $('#tareaHostList').val('');
              $('#addMultliHost').modal('toggle');
          }

          function actDlgBtnClose(sign)
          {
              if( sign == 1)
                $( "#dialog-addHost").omDialog('close');
              else if( sign == 2 )
                $( "#dialog-addGroup").omDialog('close');
              else if( sign == 3 )
                $( "#dialog-modifyGroup").omDialog('close');
              else if( sign == 4 )
                $('#addMultliHost').modal('toggle');
              else if( sign == 5 )
                $('#MsgBox').modal('toggle');
          }

          function showMsgbox(title, content)
          {
              $('#MsgBoxTitle').val(title);
              MsgBoxBody.innerHTML = content;

              $('#MsgBox').modal('toggle');
          }

          function actAGBtnAddGroup()
          {
              var gName =  $('#AGGroupName').val();
              $.ajax({
                   type: 'get',
                   url: "/memFox/servlet/getHostlist?val=type=addGroup;group=" + gName,
                   success: function(data, status){
                        if( data == "Success"){
                            $.omMessageTip.show({
                                   content:'添加成功',
                                   timeout : 3000
                            });
                            reloadGroup(gName);
                            actDlgBtnClose(2);
                            showHostList(gName, "1");
                        }else{
                            $.omMessageBox.alert({
                                   content:'添加失败，请检查组是否已经存在'
                            });
                            actDlgBtnClose(2);
                        }
                   }
               });
          }

          function actAHBtnAddHost()
          {
              var gName = getCurGroupName();
              var hName = $('#AHHostName').val();
              var hIP =  $( "#AHHostIP").val();
              var hPort = $( "#AHHostPort").val();

              if( gName.length == 0 || hName.length==0 || hIP.length==0 || hPort.length==0 ){
                  showMsgbox("错误", "<p>数据不能为空</p>")
                  actDlgBtnClose(1);
                  return false;
              }

              $.ajax({
                   type: 'get',
                   url: "/memFox/servlet/getHostlist?val=type=addHost;group=" + gName + ";host=" + hName + ";hostIP=" + hIP + ";hostPort=" + hPort ,
                   success: function(data, status){
                        if( data == "Success"){
                            $.omMessageTip.show({
                                   content:'添加成功',
                                   timeout : 3000
                            });
                            actDlgBtnClose(1);
                            showHostList(gName, "1");
                        }else{
                            actDlgBtnClose(1);
                            $.omMessageBox.alert({
                                   content:'添加失败，可能主机已存在 或 端口非数字'
                            });
                            return;
                        }
                   }
               });
          }

          function actAHBtnAddMultiHost()
          {
              var gName = getCurGroupName();
              var listInfo = $('#tareaHostList').val();
              var listInfo_line=listInfo.split("\n");
              var retSuccess = new Array();
              var retFailed = new Array();
              for(ind=0; ind<listInfo_line.length; ind++)
              {
                  var singleHost=listInfo_line[ind].split("|");
                  //若无法分解为三个信息，则跳过该行
                  if(singleHost.length < 3)
                    continue;
                  var hName=singleHost[0];
                  var hIP=singleHost[1];
                  var hPort=singleHost[2];
                  $.ajax({
                       async: false,
                       type: 'get',
                       url: "/memFox/servlet/getHostlist?val=type=addHost;group=" + gName + ";host=" + hName + ";hostIP=" + hIP + ";hostPort=" + hPort ,
                       success: function(data, status){
                           if( data == 'Success' ){
                               retSuccess[retSuccess.length]=hName + "(" + hIP + ":" + hPort + ")";
                           }else{
                               retFailed[retFailed.length]= hName + "(" + hIP + ":" + hPort + ")";
                           }
                       }
                  });
              }
              var strHtml="<p>添加成功 <font color='#1A883A'>" + retSuccess.length + "</font> 台主机，添加失败 <font color=red>" + retFailed.length + "</font> 台主机</p>";
              if(retFailed.length > 0){
                  strHtml = strHtml + "<div class=\"panel panel-default\"><table class=\"table table-striped text-center\"><tr><td>失败列表</td></tr>";
                  for(ind=0; ind< retFailed.length ; ind++){
                      strHtml = strHtml + "<tr><td>" + retFailed[ind] + "</td></tr>";
                  }
                  strHtml = strHtml + "</table></div>";
              }

              showMsgbox("结果", strHtml);
              showHostList(gName, "1");
          }

          function actMGBtnModifyGroup()
          {
              var oldGroupName = getCurGroupName();
              var newGroupName = $('#MGnewGroupName').val();

              if( oldGroupName.length == 0 || newGroupName.length == 0 ){
                  return;
              }

              $.ajax({
                   type: 'get',
                   url: "/memFox/servlet/getHostlist?val=type=modifyGroup;group=" + oldGroupName + ";newgroup=" + newGroupName ,
                   success: function(data, status){
                        if( data == "Success"){
                            $.omMessageTip.show({
                                   content:'修改分组名成功',
                                   timeout : 3000
                            });
                            reloadGroup(newGroupName);
                            showHostList(newGroupName, "1");
                            actDlgBtnClose(3);
                        }else{
                            actDlgBtnClose(3);
                            $.omMessageBox.alert({
                                   content:'添加失败，可能分组已存在'
                            });
                            return;
                        }
                   }
               });
          }

          function showDeleteConfirm(hostIP, hostPort)
          {
              if( hostIP.length == 0 || hostPort.length ==  0)
                    return;
              var gName =  getCurGroupName();
              $.omMessageBox.confirm({
                   title:'删除确认',
                   content:'确认要删除该主机吗？数据将不可恢复',
                   onClose:function(v){
                       if(v){
                           $.ajax({
                               type: 'get',
                               url: "/memFox/servlet/getHostlist?val=type=delHost;hostIP=" + hostIP + ";hostPort=" + hostPort ,
                               success: function(data, status){
                                    if( data == "Success"){
                                        $.omMessageTip.show({
                                               content:'删除成功',
                                               timeout : 3000
                                        });
                                        showHostList(gName, "1");
                                    }else{
                                        $.omMessageBox.alert({
                                               content:'删除失败，可能主机已不存在'
                                        });
                                        return;
                                    }
                               }
                           });
                       }
                   }
              });
          }

          function showDeleteGroupConfirm()
          {
              var gName =  getCurGroupName();
              if( gName == "全部" || gName == "" )
              {
                  $.omMessageBox.alert({
                        content:'请先选择要添加的组，不能是全部或留空'
                  });
                  return false;
              }
              $.omMessageBox.confirm({
                   title:'删除确认',
                   content:'确认要删除 ' + gName + ' 分组及其成员吗？数据将不可恢复',
                   onClose:function(v){
                       if(v){
                           $.ajax({
                               type: 'get',
                               url: "/memFox/servlet/getHostlist?val=type=delGroup;group=" + gName  ,
                               success: function(data, status){
                                    if( data == "Success"){
                                        $.omMessageTip.show({
                                               content:'删除成功',
                                               timeout : 3000
                                        });
                                        reloadGroup("");
                                        showHostList("全部", "1");
                                    }else{
                                        $.omMessageBox.alert({
                                               content:'删除失败，可能分组已不存在'
                                        });
                                        return;
                                    }
                               }
                           });
                       }
                   }
              });
          }

          function reloadGroup(groupName)
          {
              if( groupName == ""){
                  groupName="全部"
              }
              $.ajax({
                  type: 'GET',
                  url: '/memFox/servlet/getHostlist?val=type=gl',
                  success: function(data, status){
                      var groupData = eval(data);
                      var strHtml = "";
                      rowInd=0;

                      strHtml = "<input id=\"btnSelector\" type=\"button\" class=\"btn btn-default dropdown-toggle\" data-toggle=\"dropdown\" value=\"" + groupName + " ▼\" />";
                      strHtml = strHtml + "<ul class=\"dropdown-menu\" role=\"menu\">";
                      while(groupData[rowInd] != null ){
                          strHtml = strHtml + "<li><a href='javascript:void(0)' onclick=\"showHostList('" + groupData[rowInd].text + "')\">" + groupData[rowInd].text + "</a></li>"
                          rowInd++;
                      }
                      strHtml = strHtml + "</ul>";
                      GroupSelector.innerHTML = strHtml;
                  }
              })
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
            <li><a href="index.jsp">缓存信息汇总</a></li>
            <li class="active"><a href="hostlist.jsp">主机配置</a></li>
            <li><a href="/">返回数据展现系统</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>
    <div><br /><br /><br /><br /></div>
    <div class="container theme-showcase" align="center" style="width: 85%">
        <div class="panel panel-info">
            <div class="panel-heading"><strong>服务器信息</strong></div>
            <div class="panel-body">
                <table align="left" width="100%">
                    <tr><td>
                    <table align="left">
                        <tr>
                            <td>
                                <div id="GroupSelector" class="btn-group" style="margin-top: 8px;">
                                  <input id="btnSelector" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" value="全部 ▼" />
                                  <ul class="dropdown-menu" role="menu">
                                      <li></li>
                                  </ul>
                              </div>
                            </td>
                            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                            <td><input class="btn btn-primary" type="button" value="添加组" id="btnAddGroup" onclick="showAddGroup()"></td>
                            <td><input class="btn btn-warning" type="button" value="修改组" id="btnModifyGroup" onclick="showModifyGroup()"></td>
                            <td><input class="btn btn-danger" type="button" value="删除组" id="btnDelGroup" onclick="showDeleteGroupConfirm()"></td>
                        </tr>
                    </table>
                    </td></tr><tr><td>
                        <div><table><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td></tr></table></div>
                    </td></tr><tr><td>
                        <div id="tblHost" class="panel panel-default"></div>
                    </td></tr><tr align="center"><td>
                        <div>
                            <input class="btn btn-primary" type="button" value="添加主机" id="btnAddHost" onclick="showAddHost()">
                            <input class="btn btn-warning" type="button" value="批量添加主机" id="btnAddMultiHost" onclick="showAddMultiHost()">
                        </div>
                    </td></tr>
                </table>
            </div>
        </div>
    </div>

    <div id="dialog-addHost" title="添加主机">
            <table>
                <tr><td>
                    <div class="input-group">
                        <span class="input-group-addon">&nbsp;分组名称</span>
                        <input id="AHGroupName" type="text" class="form-control" disabled="disabled" value="分组名" >
                    </div>
                </td></tr><tr><td>
                    <div class="input-group">
                        <span class="input-group-addon"><font color="red">*</font>主机名称</span>
                        <input id="AHHostName" type="text" class="form-control" placeholder="主机名" >
                    </div>
                 </td></tr><tr><td>
                    <div class="input-group">
                        <span class="input-group-addon"><font color="red">*</font> 主机地址</span>
                        <input id="AHHostIP" type="text" class="form-control" placeholder="主机IP" >
                    </div>
                 </td></tr><tr><td>
                    <div class="input-group">
                        <span class="input-group-addon"><font color="red">*</font> 服务端口</span>
                        <input id="AHHostPort" type="text" class="form-control" placeholder="服务端口" >
                    </div>
                 </td></tr><tr align="center"><td>
                    <input class="btn btn-primary" type="button" value="添加" id="btnAHAddHost" onclick="actAHBtnAddHost()">
                    <input class="btn btn-primary" type="button" value="关闭" id="btnAHClose" onclick="actDlgBtnClose(1)">
                 </td></tr>
            </table>
    </div>

    <div id="dialog-addGroup" title="添加分组">
         <table>
             <tr><td>
                 <div class="input-group">
                        <span class="input-group-addon"><font color="red">*</font>分组名称</span>
                        <input id="AGGroupName" type="text" class="form-control" placeholder="分组名称" >
                 </div>
             </td></tr><tr align="center"><td>
                 <input class="btn btn-primary" type="button" value="添加" id="btnAGAddHost" onclick="actAGBtnAddGroup()">
                 <input class="btn btn-primary" type="button" value="关闭" id="btnAGClose" onclick="actDlgBtnClose(2)">
             </td></tr>
         </table>
    </div>

    <div id="dialog-modifyGroup" title="修改分组">
         <table>
             <tr><td>
                 <div class="input-group">
                        <span class="input-group-addon"><font color="red">*</font>原分组名称</span>
                        <input id="MGGroupName" type="text" class="form-control" placeholder="原分组名称" disabled="disabled" >
                 </div>
             </td></tr>
             <tr><td>
                 <div class="input-group">
                     <span class="input-group-addon"><font color="red">*</font>新分组名称</span>
                     <input id="MGnewGroupName" type="text" class="form-control" placeholder="新分组名称" >
                 </div>
             </td></tr>
             <tr align="center"><td>
                 <input class="btn btn-primary" type="button" value="修改" id="btnMGModifyGroup" onclick="actMGBtnModifyGroup()">
                 <input class="btn btn-primary" type="button" value="关闭" id="btnMGClose" onclick="actDlgBtnClose(3)">
             </td></tr>
         </table>
    </div>

    <div class="modal fade" id="addMultliHost" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">批量添加主机</h4><div id="titleGroupName"></div>
                </div>
                <div class="modal-body">
                    <h4><span class="label label-primary">输入要添加的主机列表</span></h4>
                    <textarea id="tareaHostList" class="form-control" rows="10" placeholder="主机名 | 主机IP | 主机端口"></textarea>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-success" data-dismiss="modal" onclick="actAHBtnAddMultiHost()">添加</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="MsgBox" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="MsgBoxTitle"></h4>
                </div>
                <div id="MsgBoxBody" class="modal-body">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-success" data-dismiss="modal" onclick="actDlgBtnClose(5)">关闭</button>
                </div>
            </div>
        </div>
    </div>

  </body>
</html>