package JDBC;

import java.sql.Connection;

public class DbController {
	private Connection conn;

    // Constructor that takes a SqlConnection object
    public DbController( Connection connection ) {
        this.conn = connection ;
    }
}
