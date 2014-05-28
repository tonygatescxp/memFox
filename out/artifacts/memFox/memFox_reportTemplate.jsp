<%--
  Created by IntelliJ IDEA.
  User: chenxp
  Date: 14-5-19
  Time: 上午10:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head><title></title>
      <script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="js/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="js/bootstrap/css/bootstrap.min.css">
      <script type="text/javascript" src="js/highcharts/highcharts.js"></script>
      <script type="text/javascript" src="js/public-function.js"></script>

      <script type="text/javascript">
          $(document).ready(function(){
              $('#pieChart').highcharts({
                      chart: {
                            plotBackgroundColor: null,
                            plotBorderWidth: null,
                            plotShadow: false,
                            height: 360
                        },
                        title: {
                            text: ''
                        },
                        tooltip: {
                            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                        },
                        plotOptions: {
                            pie: {
                                allowPointSelect: true,
                                cursor: 'pointer',
                                dataLabels: {
                                    enabled: true,
                                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                                    style: {
                                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                                    }
                                }
                            }
                        },
                        series: [{
                            type: 'pie',
                            name: 'SpaceUsed',
                            data: [{name :'null', y:100, color: '#5B9BD5'}]
                        }
                        ]
                      });

              initPage();

          });

          function initPage()
          {
               var groupName=getQueryString("groupName");
               var hostName=getQueryString("hostName");
               var hostIP=getQueryString("hostIP");
               var hostPort=getQueryString("hostPort");
               showMemcachedInfo(groupName, hostName, hostIP, hostPort);
          }

          function showMemcachedInfo(vGroupName, vHostName, vHostIP, vHostPort)
          {
              var gName=vGroupName;
              var hIP=vHostIP;
              var hPort=vHostPort;

              $.ajax({
                   type: 'get',
                   url: "/memFox/servlet/getStatus?val=type=getHostData;group=" + gName + ";hostIP=" + hIP + ";hostPort=" + hPort ,
                   success: function(data, status){
                       var hostData = eval(data);
                        tbl_info_chunkssize.innerHTML = strFormatNumber(hostData[0].chunk_size,0);
                        tbl_info_chunksperpage.innerHTML = strFormatNumber(hostData[0].chunks_per_page, 0);
                        tbl_info_chunksinfo.innerHTML = strFormatNumber(hostData[0].used_chunks,0) + " / " + strFormatNumber(hostData[0].free_chunks,0) + " / " + strFormatNumber(hostData[0].total_chunks, 0);
                        tbl_info_items.innerHTML = strFormatNumber(hostData[0].total_items,0);
                        tbl_info_pages.innerHTML = strFormatNumber(hostData[0].total_pages, 0);
                        tbl_info_eviction.innerHTML = strFormatNumber(hostData[0].evictions, 0);
                        tbl_info_cmd_get.innerHTML = strFormatNumber(hostData[0].cmd_get, 0);
                        tbl_info_cmd_set.innerHTML = strFormatNumber(hostData[0].cmd_set, 0);
                        tbl_info_threads.innerHTML = strFormatNumber(hostData[0].threads, 0);
                        tbl_info_connections.innerHTML = strFormatNumber(hostData[0].curr_connections,0);
                        tbl_info_get_hits.innerHTML = strFormatNumber(hostData[0].get_hits, 0);
                        tbl_info_get_misses.innerHTML = strFormatNumber(hostData[0].get_misses, 0);
                        tbl_info_total_size.innerHTML = strFormatNumber(hostData[0].limit_maxbytes/1024/1024/1024, 2) + " GB";
                        tbl_info_total_malloced.innerHTML = strFormatNumber(hostData[0].bytes/1024/1024/1024, 2) + " GB";

                       reloadPieChart(hostData[0].bytes, hostData[0].limit_maxbytes - hostData[0].bytes,
                                        hostData[0].cmd_set, hostData[0].cmd_get,
                                        hostData[0].get_hits, hostData[0].get_misses );
                   }
              });
          }

          function reloadPieChart(pUsed, pFree, pSet, pGet, pHits, pMisses)
          {
              var vPieChart = $('#pieChart').highcharts();
              vPieChart.showLoading('数据加载中');
              while(vPieChart.series.length){
                  vPieChart.series[0].remove();
              }

              var SpaceValue=eval("[{name:'Used', y:"+pUsed+", color:'#5B9BD5'},{name:'Free', y:"+pFree+", color: '#ED1C24'}]");
              var OperatValue=eval("[{name:'Set', y:"+pSet+", color:'#70AD47'},{name:'Get', y:"+pGet+", color: '#ED7D31'}]");
              var CachedValue=eval("[{name:'Hits', y:"+pHits+", color:'#7FFF00'},{name:'Misses', y:"+pMisses+", color: '#808000'}]");

              vPieChart.addSeries({name: '空间使用率',type: 'pie', data: SpaceValue });
              vPieChart.addSeries({name: '操作使用率',type: 'pie',size: 180, data: OperatValue });
              vPieChart.addSeries({name: '缓存命中率',type: 'pie',size: 90, data: CachedValue });

              vPieChart.hideLoading();
          }
      </script>
  </head>

<body >
    <div width="100%">
        <table width="100%" align="center">
            <tr>
                <td>
                    <div class="container theme-showcase" align="center" style="width:100%; height:100%">
                        <div class="panel">
                                <table class="table table-striped table-bordered text-center">
                                   <tr>
                                       <td colspan="4" width="820px" bgcolor=""><span class="glyphicon glyphicon-exclamation-sign"></span><strong>Memcached信息</strong></td>
                                       <td width="450px"><span class="glyphicon glyphicon-indent-left"></span><strong>分析图</strong></td>
                                   </tr>
                                   <tr>
                                       <td>chunks大小</td><td><div id="tbl_info_chunkssize" ></div></td>
                                       <td>每页chunks数</td><td><div id="tbl_info_chunksperpage" ></div></td>
                                       <td rowspan="10">
                                            <div id="pieChart"></div>
                                       </td>
                                   </tr>
                                   <tr>
                                       <td colspan="2">chunks 已用数/可用数/总数</td>
                                       <td colspan="2"><div><div id="tbl_info_chunksinfo"></div></div></td>
                                   </tr>
                                   <tr>
                                       <td>数据量（items）</td><td><div id="tbl_info_items"></div></td>
                                       <td>页面数（pages）</td><td><div id="tbl_info_pages"></div></td>
                                   </tr>
                                   <tr>
                                       <td colspan="2">淘汰量（evictions）</td><td colspan="2"><div id="tbl_info_eviction"></div></td>
                                   </tr>
                                   <tr>
                                       <td>线程（Threads）</td><td><div id="tbl_info_threads"></div></td>
                                       <td>连接数（Connections）</td><td><div id="tbl_info_connections"></div></td>
                                   </tr>
                                   <tr>
                                       <td colspan="4"><font color="white">-</font></td>
                                   </tr>
                                   <tr>
                                       <td>get次数</td><td><div id="tbl_info_cmd_get"></div></td>
                                       <td>set次数</td><td><div id="tbl_info_cmd_set"></div></td>
                                   </tr>
                                   <tr>
                                       <td>get命中次数</td><td><div id="tbl_info_get_hits"></div></td>
                                       <td>get未命中次数</td><td><div id="tbl_info_get_misses"></div></td>
                                   </tr>
                                   <tr>
                                       <td>总空间</td><td><div id="tbl_info_total_size"></div></td>
                                       <td>空间已分配</td><td><div id="tbl_info_total_malloced"></div></td>
                                   </tr>
                               </table>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
    </div>

</body>
</html>