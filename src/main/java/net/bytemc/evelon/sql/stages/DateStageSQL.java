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

package net.bytemc.evelon.sql.stages;

import net.bytemc.evelon.DatabaseProtocol;
import net.bytemc.evelon.Evelon;
import net.bytemc.evelon.misc.Pair;
import net.bytemc.evelon.repository.RepositoryClass;
import net.bytemc.evelon.sql.*;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;

public final class DateStageSQL implements SQLElementStage<Date> {

    @Override
    public String elementRowData(@Nullable Field field, RepositoryClass<Date> repository) {
        // we need this because h2 is so bruch
        if(Evelon.getCradinates().databaseProtocol() == DatabaseProtocol.H2) {
            return SQLType.BIGINT.toString();
        }
        return SQLType.DATE.toString();
    }

    @Override
    public Pair<String, String> elementEntryData(RepositoryClass<?> repositoryClass, @Nullable Field field, Date date) {
        var id = SQLHelper.getRowName(field);
        // we need this because h2 is so bruch
        if(Evelon.getCradinates().databaseProtocol() == DatabaseProtocol.H2) {
            return new Pair<>(id, Schema.encloseSchema(date.getTime()));
        }
        if (date instanceof Timestamp) {
            return new Pair<>(id, Schema.encloseSchema(date));
        }
        return new Pair<>(id, Schema.encloseSchema(new java.sql.Date(date.getTime())));
    }

    @Override
    public Date createObject(RepositoryClass<Date> clazz, String id, SQLResultSet.Table table) {
        // we need this because h2 is so bruch
        if(Evelon.getCradinates().databaseProtocol() == DatabaseProtocol.H2) {
            return new Date(table.get(id, Long.class));
        }
        return new Date(table.get(id, java.sql.Date.class).getTime());
    }

    @Override
    public boolean isElement(Class<?> type) {
        return type.equals(Date.class);
    }
}