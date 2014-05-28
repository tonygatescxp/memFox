package procPkg;

import DB.DBClass;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: chenxp
 * Date: 14-5-9
 * Time: 下午6:22
 * To change this template use File | Settings | File Templates.
 */
public class getStatus extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String getType="", groupName="", hostName="", hostIP="", hostPort="";
        String paramName="", paramValue="", paramList="";
        String strRet="";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String curTime = df.format(new Date());

        while( true ){
            try{
                // 获取各个参数
                String valParam = request.getParameter("val").toString();
                String ArrayParam[] = valParam.split(";");
                for(int iAP = 0 ; iAP < ArrayParam.length; iAP++ ){
                    String singleParam[] = ArrayParam[iAP].split("=");
                    if( singleParam.length < 2 ){
                        continue;
                    }
                    if( singleParam[0].equals("type")){
                        getType = singleParam[1].toString();
                    }else if( singleParam[0].equals("group") ){
                        groupName = singleParam[1].toString();
                    }else if( singleParam[0].equals("host") ){
                        hostName = singleParam[1].toString();
                    }else if( singleParam[0].equals("paramName") ){
                        paramName = singleParam[1].toString();
                    }else if( singleParam[0].equals("paramValue") ){
                        paramValue = singleParam[1].toString();
                    }else if( singleParam[0].equals("paramList") ){
                        paramList = singleParam[1].toString();
                    }else if( singleParam[0].equals("hostIP") ){
                        hostIP = singleParam[1].toString();
                    }else if( singleParam[0].equals("hostPort") ){
                            hostPort = singleParam[1].toString();
                    }
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
                break;
            }

            if( getType.equals("updateData")){

                strRet = "Success";
                String paramListArray[] = paramList.split("'");
                for(int i=0; i< paramListArray.length; i++)
                {
                    String pName="", pValue="";
                    if( paramListArray[i].split(":").length > 1 ){
                        pName = paramListArray[i].split(":")[0];
                        pValue = paramListArray[i].split(":")[1];
                    }

                    if( pName.isEmpty() || pValue.isEmpty() )
                        continue;

                    if( ! updateData(hostIP, hostPort, pName, pValue).equals("Success") )
                        strRet="False";
                    //AddHistory(hostIP, hostPort, pName, pValue);
                    System.out.println("[ " + curTime + " ] " + "update memcached info for " + hostName + "[" + hostIP + ":" + hostPort + "] host from " + request.getRemoteAddr() + " | MSG : [" + paramList + "]");
                }
            }else if( getType.equals("getHostData") ){
                strRet = getHostData(groupName, hostIP, hostPort);
                System.out.println("[ " + curTime + " ] " + "get memcached info for " + hostName + "[" + hostIP + ":" + hostPort + "] host from " + request.getRemoteAddr());
            }else if ( getType.equals("getHostListByGroup")){
                strRet = getHostListByGroup(groupName);
                System.out.println("[ " + curTime + " ] " + "get memcached info for " + groupName + " group from " + request.getRemoteAddr());
            }

            break;
        }

        if(strRet.isEmpty()){
            strRet="{}";
        }
        PrintWriter out = response.getWriter();
        out.print(strRet);

    }

    protected String updateData(String hostIP, String hostPort, String paramName, String paramValue)
    {
        if(hostIP.isEmpty() || hostPort.isEmpty() || paramName.isEmpty() || paramValue.isEmpty() ){
            return "False";
        }
        boolean isSuccess = false;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String curTime = df.format(new Date());

        String strSQL = "";

        while(true){
            DBClass DB = null;
            try{
                DB = new DBClass();
            }catch(Exception err){
                if(DB != null)
                    DB.closedb();
                System.out.println("Connect DB failed");
                break;
            }

            // 判断主机是否存在
            strSQL = "SELECT COUNT(*) FROM memHostList where hostIP=\"" + hostIP + "\" and hostPort=" + hostPort + ";";
            ResultSet rs = null;
            try{
                rs = DB.select(strSQL);
                rs.first();
                if( rs.getString(1).equals("0") ){
                    rs.close();
                    DB.closedb();
                    break;
                }
            }catch(Exception e){
                DB.closedb();
				break;
			}

            String strCondition = " where hostID=(select hostid from memHostList where hostIP=\"" + hostIP + "\" and hostPort=" + hostPort + ")";
            strCondition = strCondition + " and paramName=\"" + paramName + "\";";
            strSQL = "SELECT count(*) from memData" + strCondition + ";";
            rs = null;
			try{
				rs = DB.select(strSQL);
			}catch(Exception e){
                DB.closedb();
				break;
			}

            try {
				rs.first();
                if ( rs.getString(1).equals("0") ){
                    // 如果数据不存在，添加它
                    strSQL = "insert into memData(groupID, hostID, paramName, paramValue) values(";
                    strSQL = strSQL + "(select groupID from memHostList where hostIP=\"" + hostIP + "\" and hostPort=" + hostPort + "),";
                    strSQL = strSQL + "(select hostID from memHostList where hostIP=\"" + hostIP + "\" and hostPort=" + hostPort + "),";
                    strSQL = strSQL + "'" + paramName + "','" + paramValue + "'";
                    strSQL = strSQL + ");";
                    DB.update(strSQL);
                    isSuccess=true;
                }else{
                    strSQL = "update memData set paramValue='" + paramValue + "' " + strCondition + ";";
                    DB.update(strSQL);
                    isSuccess=true;
                }
                strSQL = "update memHostList set lastUpdate=\"" + curTime + "\" where hostIP=\"" + hostIP + "\" and hostPort=" + hostPort + ";";
                DB.update(strSQL);
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
			}

            DB.closedb();
            break;
        }

        if( isSuccess ){
            return "Success";
        }
        return "False";
    }

    protected void AddHistory(String hostIP, String hostPort, String paramName, String paramValue)
    {
        String strNeedToAdd = ",get_hits,get_misses,cmd_set,cmd_get,";
        if( strNeedToAdd.indexOf("," + paramName + ",") == -1 )
            return;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String curTime = df.format(new Date());

        String strSQL="insert into memHistory(hostID, paramName, paramValue, updateTime) values(";
        strSQL += "(select hostID from memHostList where hostIP=\"" + hostIP + "\" and hostPort=\"" + hostPort + "\"),";
        strSQL += "'" + paramName + "'," + paramValue + ",'" + curTime + "');";

        DBClass DB = null;
        try{
            DB = new DBClass();
            DB.update(strSQL);
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
		}finally{
            DB.closedb();
		}

        return ;
    }
    protected String getHostData(String groupName, String hostIP, String hostPort)
    {
        List dataList = new ArrayList();
        Map map = new HashMap();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        rightNow.add(Calendar.DAY_OF_YEAR, -3);
        String pass3dayTime = df.format(rightNow.getTime());

        String strCondition="";
        if( ! (hostIP.isEmpty() && hostPort.isEmpty()) ){
            // 某单机数据，无时间限制
            strCondition = "WHERE hostid=(SELECT hostid FROM memHostList WHERE hostIP='" + hostIP + "' AND hostPort=" + hostPort + ")";
        }else if( ! groupName.isEmpty() ){
            // 三天内某分组的数据
            strCondition = "WHERE hostid in (select hostid from memHostList where groupid=(SELECT groupid from memGroup WHERE groupName='" + groupName + "')";
            strCondition = strCondition + " and lastUpdate>='" + pass3dayTime + "')";
        }else{
            // 三天内所有数据
            strCondition = "WHERE hostid in (select hostid from memHostList where lastUpdate>='" + pass3dayTime + "')";
        }



        while(true){
            DBClass DB = null;
            try{
                DB = new DBClass();
            }catch(Exception err){
                if(DB != null)
                    DB.closedb();
                System.out.println("Connect DB failed");
                break;
            }

            String strSQL = "SELECT paramName pName, SUM(paramValue) pValue FROM memData ";
            strSQL += strCondition + " GROUP BY pName";
            ResultSet rs = null;
			try{
				rs = DB.select(strSQL);
			}catch(Exception e){
				break;
			}

            try {
				while(rs.next()){
                    map.put(rs.getString(1), rs.getString(2));
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if( map.isEmpty()){
					System.out.println("table list is empty");
                    DB.closedb();
					break ;
				}
			}

            DB.closedb();
            break;
        }
        dataList.add(map);

        String strDataList = new Gson().toJson(dataList);
        return strDataList;
    }

    protected String getHostListByGroup(String groupName)
    {
        List dataList = new ArrayList();
        Map map = null;

        if( groupName.isEmpty() ){
             return new Gson().toJson(dataList);
        }
        String strSQL = "SELECT h.`hostid`, h.`hostIP`, h.`hostPort`, h.`hostName`, d.paramName, d.paramValue, DATE_FORMAT(h.`lastUpdate`, '%Y-%m-%d %T') lastUpdate FROM memData d, memHostList h ";
        String strCondition = "WHERE d.groupID=(SELECT groupID FROM memGroup WHERE groupName='" + groupName + "') AND (paramName LIKE 'cmd_%' OR paramName LIKE 'get_%' OR paramName LIKE '%bytes') AND d.`hostID`=h.`hostid` ORDER BY hostid";

        while(true){
            DBClass DB = null;
            try{
                DB = new DBClass();
            }catch(Exception err){
                if(DB != null)
                    DB.closedb();
                System.out.println("Connect DB failed");
                break;
            }

            ResultSet rs = null;
			try{
				rs = DB.select(strSQL + strCondition + ";");
			}catch(Exception e){
				break;
			}

            try {
                String curHostID="";
				while(rs.next()){
                    if( ! curHostID.equals(rs.getString("hostid")) ){
                        //下一条数据的主机名与当前正在收集的主机名不相同时处理
                        if( map != null )
                            //若上次主机名不为空则将上次数据压入列表中
                            dataList.add(map);
                        map = new HashMap();
                        curHostID = rs.getString("hostid");
                        map.put("hostName", rs.getString("hostName"));
                        map.put("hostIP", rs.getString("hostIP"));
                        map.put("hostPort", rs.getString("hostPort"));
                        map.put("lastUpdate", rs.getString("lastUpdate"));
                    }
                    map.put(rs.getString("paramName"), rs.getString("paramValue"));
				}
                dataList.add(map);
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if( map.isEmpty()){
					System.out.println("table list is empty");
                    DB.closedb();
					break ;
				}
			}

            DB.closedb();
            break;
        }

        String strDataList = new Gson().toJson(dataList);
        System.out.print(strDataList);
        return strDataList;
    }
}
