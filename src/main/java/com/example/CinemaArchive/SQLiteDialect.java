package com.example.CinemaArchive;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;


public class SQLiteDialect extends Dialect {

    @SuppressWarnings("deprecation")
    public SQLiteDialect() {
        super();
    }

    // Override to customize table creation syntax for SQLite
    @Override
    public String getCreateTableString() {
        return "create table if not exists";
    }

    // Provide identity column support implementation
    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new IdentityColumnSupportImpl();
    }

    // Override other methods if necessary for your specific needs
}





