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
package io.micronaut.data.hibernate.reactive;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import io.micronaut.data.tck.entities.Shipment;
import io.micronaut.data.tck.entities.ShipmentId;
import reactor.core.publisher.Mono;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.function.Consumer;

@Repository
public interface JpaShipmentRepository extends ReactorCrudRepository<Shipment, ShipmentId> {

    Mono<Shipment> findByShipmentIdCountry(String country);

    Mono<Shipment> findByShipmentIdCountryAndShipmentIdCity(String country, String city);

    @Transactional
    default Mono<Void> findAndUpdate(ShipmentId id, Consumer<Shipment> consumer) {
        return findById(id).map(shipment -> {
            consumer.accept(shipment);
            return shipment;
        }).then();
    }

    @Transactional
    default Mono<Void> findAndDelete(ShipmentId id) {
        return findById(id).flatMap(this::delete).then();
    }

    @Transactional
    default Mono<Void> findAndDeleteAll(ShipmentId id) {
        return findById(id).flatMap(entity -> deleteAll(Collections.singleton(entity))).then();
    }

}
