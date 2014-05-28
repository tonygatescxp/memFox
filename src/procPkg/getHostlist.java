package procPkg;

import DB.DBClass;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: chenxp
 * Date: 14-5-8
 * Time: 下午2:27
 * To change this template use File | Settings | File Templates.
 */
//@javax.servlet.annotation.WebServlet(name = "getHostlist")
public class getHostlist extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        String getType="", groupName="", newGroupName="", pageNumber="";
        //for add host
        String  hostName="", hostIP="", hostPort="", dataRequire="";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String curTime = df.format(new Date());

        String strRet="";
        while (true){
            try{
                // 获取各个参数
                String valParam = request.getParameter("val").toString();
                String ArrayParam[] = valParam.split(";");
                for(int iAP = 0 ; iAP < ArrayParam.length; iAP++ ){
                    String singleParam[] = ArrayParam[iAP].split("=");
                    if(singleParam.length < 1){
                        continue;
                    }
                    if( singleParam[0].equals("type")){
                        getType = singleParam[1].toString();
                    }else if( singleParam[0].equals("newgroup") ){
                        newGroupName = singleParam[1].toString();
                    }else if( singleParam[0].equals("group") ){
                        groupName = singleParam[1].toString();
                    }else if( singleParam[0].equals("host") ){
                        hostName = singleParam[1].toString();
                    }else if( singleParam[0].equals("hostIP") ){
                        hostIP = singleParam[1].toString();
                    }else if( singleParam[0].equals("hostPort") ){
                        hostPort = singleParam[1].toString();
                    }else if( singleParam[0].equals("dataRequire") ){
                        dataRequire=singleParam[1].toString();
                    }else if( singleParam[0].equals("page") ){
                        pageNumber=singleParam[1].toString();
                    }
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
                break;
            }

            // 根据 Type 转向不同的处理函数
            if( getType.equals("gl")){
                strRet = getGroup(dataRequire);
                System.out.println("[ " + curTime + " ] " + "get all host info from "+  request.getRemoteAddr());
            }else if( getType.equals("addGroup") ){
                if( groupName.isEmpty() )
                    strRet = "False";
                else{
                    strRet = addGroup(groupName);
                    System.out.println("[ " + curTime + " ] " + "Add new group " +  groupName + " from " + request.getRemoteAddr());
                }
            }else if( getType.equals("delGroup") ){
                if( groupName.isEmpty() )
                    strRet = "False";
                else{
                    strRet = delGroup(groupName);
                    System.out.println("[ " + curTime + " ] " + "Delete group " +  groupName + " from " + request.getRemoteAddr());
                }
            }else if( getType.equals("modifyGroup") ){
                if(newGroupName.isEmpty() || groupName.isEmpty()){
                    strRet = "False";
                }else{
                    strRet = ModifyGroup(groupName, newGroupName);
                    System.out.println("[ " + curTime + " ] " + "Modify group name from" +  groupName + " to " + newGroupName + " from " + request.getRemoteAddr() );
                }
            }else if( getType.equals("addHost") ){
                if( groupName.isEmpty() || hostName.isEmpty() || hostIP.isEmpty() || hostPort.isEmpty() ){
                    strRet="False";
                    break;
                }
                for(int i=0; i<hostPort.length(); i++){
                    if( !Character.isDigit(hostPort.charAt(i))){
                        strRet="False";
                        break;
                    }
                }
                if(strRet.isEmpty()){
                    System.out.println("[ " + curTime + " ] " + "Add host " +  hostName + " from " + request.getRemoteAddr());
                    strRet=addHost(groupName,hostName,hostIP,hostPort);
                }
            }else if( getType.equals("getHost") ){
                strRet = getHost(groupName, pageNumber);
                System.out.println("[ " + curTime + " ] " + "Get host info " +  hostName + " host with page " + pageNumber + " from " + request.getRemoteAddr());
            }else if( getType.equals("delHost") ){
                strRet = delHost(hostIP, hostPort);
                System.out.println("[ " + curTime + " ] " + "Delete host " +  hostName + " from " + request.getRemoteAddr());
            }

            break;
        }

        if(strRet.isEmpty()){
            strRet="{}";
        }
        PrintWriter out = response.getWriter();
        out.print(strRet);
    }

    protected String addHost(String GroupName, String hostName, String hostIP, String hostPort){

        boolean isSuccess = false;
        if( GroupName.isEmpty() )
             return "False";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String curTime = df.format(new Date());

        GroupName.replaceAll("--", "\\-\\-");
        String strSQL = "select count(*) from memHostList where hostIP=\"" +
                hostIP + "\" and hostPort=" + hostPort;
        //查询数据是否存在
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
				rs = DB.select(strSQL);
			}catch(Exception e){
				break;
			}

            try {
				rs.first();
                if ( rs.getString(1).equals("0") ){
                    // 如果数据不存在，添加它
                    strSQL = "insert into memHostList( groupID, hostName, hostIP, hostPort, lastUpdate ) values " +
                            "( (select groupID from memGroup where groupName=\"" + GroupName + "\"), " +
                            "\"" + hostName +"\",\"" + hostIP + "\", " + hostPort + ",\"" + curTime + "\");";
                    DB.update(strSQL);
                    isSuccess=true;
                }
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

    protected String delHost(String hostIP, String hostPort )
    {
        boolean isSuccess = false;
        if( hostIP.isEmpty() || hostPort.isEmpty() )
             return "False";

        String Condtion=" where hostIP=\"" + hostIP + "\" and hostPort=" + hostPort;
        String strSQL = "select count(*) from memHostList"  + Condtion;
        //查询数据是否存在
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
				rs = DB.select(strSQL);
			}catch(Exception e){
				break;
			}

            try {
				rs.first();
                if ( ! rs.getString(1).equals("0") ){
                    // 如果数据存在，删除它
                    strSQL = "DELETE FROM memHistory where hostID=(select hostID from memHostList " + Condtion + ");";
                    DB.update(strSQL);
                    strSQL = "DELETE FROM memData where hostID=(SELECT hostID from memHostList " + Condtion + ");";
                    DB.update(strSQL);
                    strSQL = "DELETE FROM memHostList " + Condtion;
                    DB.update(strSQL);
                    isSuccess=true;
                }
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

    protected String getHost(String GroupName, String pageNumber)
    {
        if( GroupName.isEmpty() )
             return "False";
        if( ! pageNumber.isEmpty() ){
            //页数不是整型数值的，直接置空无视之
            if( ! pageNumber.matches("[0-9]*") )
                pageNumber="";
        }
        List hostList = new ArrayList();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String curTime = df.format(new Date());

        GroupName.replaceAll("--", "\\-\\-");
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

            ResultSet rs = null;
            //计算总页数
            String pageCount="";
            if( ! pageNumber.isEmpty() ){
                strSQL="select count(*) from memHostList";
                if( ! (GroupName.equals("全部") || GroupName.equals("") || GroupName.equals("all")) ){
                    strSQL += " where groupID=(select groupID from memGroup where groupName=\"" + GroupName + "\")";
                }
                try {
                    rs=DB.select(strSQL);
                    rs.next();
                    pageCount=Integer.toString((int)Math.ceil(Double.parseDouble(rs.getString(1)) / 20 ));
                    rs.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Map map = new HashMap();
                map.put("pageCount", pageCount);
                hostList.add(map);

                if( Integer.parseInt(pageNumber) > Integer.parseInt(pageCount) && Integer.parseInt(pageCount)>0 ){
                    pageNumber = pageCount;
                }
            }

            //获取主机数据
            strSQL = "select groupName, hostName, hostIP, hostPort " +
                "from memHostList hl, memGroup gl where hl.groupID=gl.groupID";
            if( ! (GroupName.equals("全部") || GroupName.equals("") || GroupName.equals("all")) ){
                 strSQL += " and gl.groupID=(select groupID from memGroup where groupName=\"" + GroupName + "\")";
            }
            strSQL += " ORDER BY groupName, hostName";
            if( ! pageNumber.isEmpty() )
            {
                strSQL += " limit " + ((Integer.parseInt(pageNumber)-1)*20) + ",20";
            }
            strSQL += ";";
			try{
				rs = DB.select(strSQL);
                while(rs.next()){
                    Map map = new HashMap();
                    map.put("gName", rs.getString(1));
                    map.put("hName", rs.getString(2));
                    map.put("hIP", rs.getString(3));
                    map.put("hPort", rs.getString(4));
					hostList.add(map);
				}
				rs.close();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if( hostList.isEmpty()){
					System.out.println("table list is empty");
				}
			}


            DB.closedb();
            break;
        }

        String strHostList = new Gson().toJson(hostList);
        return strHostList;
    }

    protected String addGroup(String GroupName)
    {
        boolean isSuccess = false;
        if( GroupName.isEmpty() )
            return "False";

        GroupName.replaceAll("--", "\\-\\-");
        String strCondition = " where groupName=\"" + GroupName + "\";";
        String strSQL = "select count(*) from memGroup" + strCondition;

        //查询数据是否存在
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
				rs = DB.select(strSQL);
			}catch(Exception e){
				break;
			}

            try {
				rs.first();
                if ( rs.getString(1).equals("0") ){
                    // 如果数据不存在则添加它，否则返回失败
                    strSQL = "insert into memGroup( groupName ) values(\"" + GroupName + "\");";
                    DB.update(strSQL);
                    isSuccess=true;
                }
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

    protected String delGroup(String GroupName)
    {
        boolean isSuccess = false;
        if( GroupName.isEmpty() )
            return "False";

        GroupName.replaceAll("--", "\\-\\-");
        String strCondition = " where groupName=\"" + GroupName + "\"";
        String strSQL = "select count(*) from memGroup" + strCondition + ";";

        //查询数据是否存在
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
				rs = DB.select(strSQL);
			}catch(Exception e){
				break;
			}

            try {
				rs.first();
                if ( ! rs.getString(1).equals("0") ){
                    // 如果数据不存在，添加它
                    strSQL = "DELETE FROM memHistory where hostID in (select hostID from memHostList where groupID=(select groupID from memGroup" + strCondition + "));";
                    DB.update(strSQL);
                    strSQL = "DELETE FROM memData where groupID=(select groupID from memGroup" + strCondition + ");";
                    DB.update(strSQL);
                    strSQL = "DELETE FROM memHostList where groupID=(select groupID from memGroup" + strCondition + ");";
                    DB.update(strSQL);
                    strSQL = "DELETE FROM memGroup" + strCondition + ";";
                    DB.update(strSQL);
                    isSuccess=true;
                }
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

    protected String ModifyGroup(String GroupName, String newGroupName)
    {
        boolean isSuccess = false;
        if( GroupName.isEmpty() || newGroupName.isEmpty() )
            return "False";

        GroupName.replaceAll("--", "\\-\\-");
        String strCondition = " where groupName=\"" + GroupName + "\";";
        String strSQL = "select count(*) from memGroup" + strCondition;

        //查询数据是否存在
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
				rs = DB.select(strSQL);
			}catch(Exception e){
				break;
			}

            try {
				rs.first();
                if ( ! rs.getString(1).equals("0") ){
                    // 如果数据存在则修改它
                    strSQL = "UPDATE memGroup set GroupName='" + newGroupName + "'" + strCondition;
                    DB.update(strSQL);
                    isSuccess=true;
                }
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

    protected String getGroup(String dataRequire)
    {
        List groupList = new ArrayList();
        Map map2 = new HashMap();
        map2.put("text", "全部");
        map2.put("value", "all");
        groupList.add(map2);

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

            String strSQL="select groupName from memGroup order by groupName;";
            if( dataRequire.equals("yes") ){
                strSQL = "select groupName from memGroup where groupID in ( select distinct(groupID) from memData);";
            }

            ResultSet rs = null;
			try{
				rs = DB.select(strSQL);
			}catch(Exception e){
				break;
			}

            try {
				while(rs.next()){
                    Map map = new HashMap();
                    map.put("text", rs.getString(1));
                    map.put("value", rs.getString(1));
					groupList.add(map);
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if( groupList.isEmpty()){
					System.out.println("table list is empty");
					break ;
				}
			}

            DB.closedb();
            break;
        }

        String strGroupList = new Gson().toJson(groupList);
        return strGroupList;
    }
}
