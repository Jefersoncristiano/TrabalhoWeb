package Infraestructure;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String RESOURCE_NAME = "postgresql";

    public Connection getConnection() throws NamingException , SQLException{
       return getDatasource().getConnection();

    }
    private DataSource getDatasource() throws NamingException {
        Context context = new InitialContext();

        return (DataSource) context.lookup(RESOURCE_NAME);
    }

}
