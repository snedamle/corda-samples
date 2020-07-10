package net.corda.database.examples.states;

import net.corda.core.serialization.CordaSerializable;

@CordaSerializable
public class ProductDetail {
    private final String detail;
    private final Integer detail_id;

    public ProductDetail(String detail, Integer detail_id) {
        this.detail = detail;
        this.detail_id = detail_id;
    }

    public String getDetail() {
        return detail;
    }

    public Integer getDetail_id() {
        return detail_id;
    }
}