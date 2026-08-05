/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyDatabaseIdProvider
implements DatabaseIdProvider {
    private static Logger logger = LoggerFactory.getLogger(MyDatabaseIdProvider.class);

    public void setProperties(Properties p) {
        logger.info(p.getProperty("MySQL"));
    }

    public String getDatabaseId(DataSource dataSource) throws SQLException {
        Connection conn = dataSource.getConnection();
        String dbName = conn.getMetaData().getDatabaseProductName();
        String dbAlias = "";
        switch (dbName) {
            case "MySQL": {
                dbAlias = "mysql";
                break;
            }
            case "PostgreSQL": {
                dbAlias = "postgresql";
                break;
            }
            case "MariaDB": {
                dbAlias = "mysql";
                break;
            }
            case "Oracle": {
                dbAlias = "oracle";
                break;
            }
            case "Microsoft SQL Server": {
                dbAlias = "SQLServer";
                break;
            }
            case "DM DBMS": {
                dbAlias = "oracle";
                break;
            }
        }
        return dbAlias;
    }
}

