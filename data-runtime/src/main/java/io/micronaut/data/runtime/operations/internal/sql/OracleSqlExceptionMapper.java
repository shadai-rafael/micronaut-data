/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.data.runtime.operations.internal.sql;

import io.micronaut.core.annotation.Internal;
import io.micronaut.data.exceptions.OptimisticLockException;
import io.micronaut.data.model.query.builder.sql.Dialect;
import jakarta.inject.Singleton;

import java.sql.SQLException;

/**
 * The {@link SqlExceptionMapper} for {@link Dialect#ORACLE}.
 */
@Singleton
@Internal
final class OracleSqlExceptionMapper implements SqlExceptionMapper {

    private static final int JSON_VIEW_ETAG_NOT_MATCHING_ERROR = 42699;

    @Override
    public Dialect getDialect() {
        return Dialect.ORACLE;
    }

    /**
     * Handles SQL exception for Oracle dialect, used in context of update but could be used elsewhere.
     * It can throw custom exception based on the {@link SQLException}.
     * Basically throws {@link OptimisticLockException} if error thrown is matching expected error code
     * that is used to represent ETAG not matching when updating Json View.
     *
     * @param sqlException the SQL exception
     * @return custom exception based on {@link SQLException} that was thrown or that same
     * exception if nothing specific was about it
     */
    @Override
    public Exception mapSqlException(SQLException sqlException) {
        if (sqlException.getErrorCode() == JSON_VIEW_ETAG_NOT_MATCHING_ERROR) {
            return new OptimisticLockException("ETAG did not match when updating record: " + sqlException.getMessage(), sqlException);
        }
        return sqlException;
    }

}
