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
package io.micronaut.data.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.micronaut.context.annotation.DefaultImplementation;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.core.annotation.TypeHint;
import io.micronaut.data.model.Pageable.Mode;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Inspired by the Spring Data's {@code Page} and GORM's {@code PagedResultList}, this models a type that supports
 * pagination operations.
 *
 * <p>A Page is a result set associated with a particular {@link Pageable} that includes a calculation of the total
 * size of page of records.</p>
 *
 * @param <T> The generic type
 * @author graemerocher
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@TypeHint(Page.class)
@JsonDeserialize(as = DefaultPage.class)
@Serdeable
@DefaultImplementation(DefaultPage.class)
public interface Page<T> extends Slice<T> {

    Page<?> EMPTY = new DefaultPage<>(Collections.emptyList(), Pageable.unpaged(), null);

    /**
     * @return Whether this {@link Page} contains the total count of the records
     * @since 4.8.0
     */
    boolean hasTotalSize();

    /**
     * Get the total count of all the records that can be given by this query.
     * The method may produce a {@link IllegalStateException} if the {@link Pageable} request
     * did not ask for total size.
     *
     * @return The total size of the all records.
     */
    long getTotalSize();

    /**
     * Get the total count of pages that can be given by this query.
     * The method may produce a {@link IllegalStateException} if the {@link Pageable} request
     * did not ask for total size.
     *
     * @return The total page of pages
     */
    default int getTotalPages() {
        int size = getSize();
        return size == 0 ? 1 : (int) Math.ceil((double) getTotalSize() / (double) size);
    }

    /**
     * Determines whether there is a next page.
     *
     * @since 4.8.0
     * @return Whether there exist a next page.
     */
    default boolean hasNext() {
        if (getPageable().getMode() == Mode.OFFSET) {
            return hasTotalSize()
                ? getOffset() + getSize() < getTotalSize()
                : getContent().size() == getSize();
        }
        return getPageable().hasNext();
    }

    /**
     * Determines whether there is a previous page.
     *
     * @since 4.8.0
     * @return Whether there exist a previous page.
     */
    default boolean hasPrevious() {
        if (getPageable() instanceof CursoredPageable cursoredPageable) {
            return cursoredPageable.hasPrevious();
        }
        return getOffset() > 0;
    }

    /**
     * Maps the content with the given function.
     *
     * @param function The function to apply to each element in the content.
     * @param <T2> The type returned by the function
     * @return A new slice with the mapped content
     */
    @Override
    default @NonNull <T2> Page<T2> map(Function<T, T2> function) {
        List<T2> content = getContent().stream().map(function).collect(Collectors.toList());
        return new DefaultPage<>(content, getPageable(), getTotalSize());
    }

    /**
     * Creates a slice from the given content and pageable.
     * @param content The content
     * @param pageable The pageable
     * @param totalSize The total size
     * @param <T> The generic type
     * @return The slice
     */
    @JsonCreator
    @ReflectiveAccess
    static @NonNull <T> Page<T> of(
            @JsonProperty("content") @NonNull List<T> content,
            @JsonProperty("pageable") @NonNull Pageable pageable,
            @JsonProperty("totalSize") @Nullable Long totalSize) {
        return new DefaultPage<>(content, pageable, totalSize);
    }

    /**
     * Creates an empty page object.
     * @param <T2> The generic type
     * @return The slice
     */
    @SuppressWarnings("unchecked")
    static @NonNull <T2> Page<T2> empty() {
        return (Page<T2>) EMPTY;
    }
}
