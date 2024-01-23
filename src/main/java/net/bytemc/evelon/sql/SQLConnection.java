/*
 * Copyright 2019-2023 ByteMC team & contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.bytemc.evelon.sql;

import net.bytemc.evelon.DatabaseProtocol;
import net.bytemc.evelon.Debugger;
import net.bytemc.evelon.Evelon;
import net.bytemc.evelon.cradinates.DatabaseCradinates;
import net.bytemc.evelon.sql.connection.HikariDatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class SQLConnection {

    private static SQLConnection INSTANCE;

    private final HikariDatabaseConnector pool;

    private SQLConnection(DatabaseProtocol protocol, DatabaseCradinates cradinates) {
        if (INSTANCE == null) {
            INSTANCE = this;
        }

        this.pool = new HikariDatabaseConnector().createConnection(protocol, cradinates);

        Debugger.log("Established connection to " + protocol + " server");
    }

    public static SQLConnection init(DatabaseProtocol databaseProtocol) {
        return new SQLConnection(databaseProtocol, Evelon.getCradinates());
    }

    public static SQLConnection init(DatabaseCradinates cradinates) {
        return new SQLConnection(cradinates.databaseProtocol(), cradinates);
    }

    public static SQLConnection getInstance() {
        return INSTANCE;
    }

    public <T> T executeQuery(String query, SQLFunction<ResultSet, T> function, T defaultValue) {
        try (var connection = this.pool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (var resultSet = preparedStatement.executeQuery()) {
                return function.apply(resultSet);
            } catch (Exception throwable) {
                return defaultValue;
            }
        } catch (SQLException exception) {
            System.err.println("Error while execute update: " + exception.getMessage() + " with " + exception.getCause().toString());
        } finally {
            Debugger.log(query);
        }
        return defaultValue;
    }

    public int executeUpdate(String query) {
        try (var connection = this.pool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            return preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            System.err.println("Error while executing update: " + query);
            return -1;
        } finally {
            Debugger.log(query);
        }
    }
}