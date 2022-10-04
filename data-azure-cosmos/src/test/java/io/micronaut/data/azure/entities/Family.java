package io.micronaut.data.azure.entities;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.cosmos.annotation.Container;
import io.micronaut.data.cosmos.annotation.PartitionKey;

import java.util.ArrayList;
import java.util.List;

@Container
@MappedEntity
public class Family {

    @Id
    private String id;

    @PartitionKey
    private String lastName;

    private Address address;

    private List<Child> children = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }
}