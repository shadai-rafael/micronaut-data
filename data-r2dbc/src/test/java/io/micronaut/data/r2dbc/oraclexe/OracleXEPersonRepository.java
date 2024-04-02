/*
 * Copyright 2017-2020 original authors
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
package io.micronaut.data.r2dbc.oraclexe;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.tck.entities.Person;
import io.micronaut.data.tck.repositories.PersonRepository;

import java.util.List;

@R2dbcRepository(dialect = Dialect.ORACLE)
public interface OracleXEPersonRepository extends PersonRepository {

    @Override
    Person save(String name, int age);

    @Override
    @Query("INSERT INTO person(id, name, age, enabled) VALUES (\"PERSON_SEQ\".nextval, :name, :age, 1)")
    int saveCustom(String name, int age);

    @Override
    @Query("INSERT INTO person(id, name, age, enabled) VALUES (\"PERSON_SEQ\".nextval, :name, :age, 1)")
    int saveCustom(List<Person> people);

    @Override
    @Query("INSERT INTO person(id, name, age, enabled) VALUES (\"PERSON_SEQ\".nextval, :name, :age, 1)")
    int saveCustomSingle(Person people);

}
