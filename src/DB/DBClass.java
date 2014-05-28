package DB;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBClass {
	//锟斤拷锟斤拷锟斤拷锟接讹拷锟斤拷conn
	private Connection conn = null;
	//锟斤拷锟斤拷PreparedStatement锟斤拷锟斤拷pStmt
	private PreparedStatement pStmt = null;
	//锟斤拷锟斤拷锟斤拷菁锟斤拷锟斤拷锟絩s
	private ResultSet rs = null;
	
	public DBClass(){
	try {
		//锟斤拷锟斤拷Context锟斤拷锟斤拷env
        Context env = new InitialContext();
		//锟斤拷锟斤拷锟斤拷锟皆达拷锟斤拷锟�
        DataSource pool = (DataSource) env.lookup("java:comp/env/jdbc/memAdminDB");
        if (pool == null)
		
		//锟斤拷锟接诧拷锟缴癸拷锟斤拷锟阶筹拷锟届常锟斤拷息锟斤拷示
        throw new Exception("jdbc/mysql is an unknown DataSource");
		 //锟斤拷锟接ｏ拷锟斤拷锟接筹拷
         conn = pool.getConnection();
     } catch(Exception e){
		 //锟斤拷印锟斤拷锟斤拷斐ｏ拷锟较�
		System.out.println("Err naming:" + e.getMessage());
		}
	finally{
		
	}
}
	
//锟截闭诧拷锟斤拷
	public void closedb(){
		try {
			//锟截憋拷锟斤拷菁锟�
			if (rs != null) {
				rs.close();
				rs = null;
			}
			//锟截憋拷pStmt锟斤拷锟斤拷
			if (pStmt != null) {
				pStmt.close();
				pStmt = null;
			}
			//锟截憋拷锟斤拷锟斤拷锟斤拷映锟�
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			//锟截闭诧拷锟缴癸拷锟斤拷锟阶筹拷锟届常锟斤拷息
			System.out.println("Mysql Close Err: " + e);
		} finally {
			if (conn != null) {
				//锟截憋拷锟斤拷锟斤拷锟斤拷锟�
				try{
					conn.close();
				}catch(SQLException e){
					conn = null;
				}
			}
		}
	}
	// 执锟叫诧拷询锟斤拷锟斤拷
	public ResultSet select(String sql) {
		try {
			System.out.println("SelectSQL: " + sql);
			//执锟斤拷SQL锟斤拷锟�
			pStmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			//执锟叫诧拷询
			rs = pStmt.executeQuery();
		} catch (SQLException e) {
			//锟斤拷锟杰诧拷询锟斤拷锟斤拷印锟斤拷锟届常锟斤拷息
			System.out.println("Mysql select Err: "+e);
			System.out.println("Error SQL: "+sql);
		}
		//锟斤拷锟斤拷锟斤拷菁锟�
		return rs;
	}
	
	public void update(String sql){
		try {
			//System.out.println("UpdateSQL: " + sql);
			//执锟斤拷SQL锟斤拷锟�
			pStmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			//执锟叫诧拷询
			pStmt.executeUpdate(sql);
		} catch (SQLException e) {
			//锟斤拷锟杰诧拷询锟斤拷锟斤拷印锟斤拷锟届常锟斤拷息
			System.out.println("Mysql Update Err: "+e);
			System.out.println("Error SQL: "+sql);
		}
	}
	
}