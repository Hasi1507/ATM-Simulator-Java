package atm.simulator.system;

import java.sql.*;

public class Conn {
    
    Connection c;
    Statement s;
    public Conn() {
        try {
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/atmsimulatorsystem"
              , "root", "Hasini@2007");
            s=c.createStatement();
            
        } catch (Exception e){
           e.printStackTrace();
        }
    }
    
}
