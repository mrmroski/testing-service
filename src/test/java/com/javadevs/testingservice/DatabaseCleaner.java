package com.javadevs.testingservice;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static liquibase.database.DatabaseFactory.getInstance;

@Service
@ActiveProfiles("test")
@Value
public class DatabaseCleaner {

    Connection connection;
    Database database;
    Liquibase liquibase;

    public DatabaseCleaner() throws SQLException, DatabaseException {
        this.connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "password");
        this.database = getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        this.liquibase = new Liquibase("liquibase-master-test.xml", new ClassLoaderResourceAccessor(), database);
    }

    public void cleanUp() throws LiquibaseException {
        liquibase.dropAll();
        liquibase.update(new Contexts());
    }
}
