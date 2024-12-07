// package com.example.config;
// import java.sql.SQLException;
// import javax.sql.DataSource;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.jdbc.datasource.SingleConnectionDataSource;
// import org.sqlite.SQLiteConfig;



// @Configuration
// public class SQLiteDataSourceConfig {

//     @Bean
//     public DataSource dataSource() throws SQLException {
//         SQLiteConfig config = new SQLiteConfig();
//         config.setReadOnly(false); // Ensure this is set before the connection
//         String dbUrl = "jdbc:sqlite:/home/kooduser/Desktop/CinemaArchive/MoviesDataBase.db"; // Update path to your database
//         return new SingleConnectionDataSource(config.createConnection(dbUrl), true);
//     }
// }


package com.example.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:/home/kooduser/Desktop/CinemaArchive/MoviesDataBase.db");
        dataSource.setUsername(""); // SQLite does not use a username
        dataSource.setPassword(""); // SQLite does not use a password
        return dataSource;
    }
}
