package net.corda.database.examples.schema;

import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.serialization.CordaSerializable;

import javax.persistence.*;
import java.util.Arrays;
import java.util.UUID;

public class ProductSchemaV1 extends MappedSchema {
    public ProductSchemaV1() {
        super(ProductSchema.class, 1, Arrays.asList(PersistentProduct.class, PersistentProductDetail.class));
    }

    @Entity
    @Table(name = "product")
    public static class PersistentProduct extends PersistentState {
        @Column(name = "id") private final UUID id;
        @Column(name = "sku") private final Integer sku;
        @Column(name = "name") private final String name;
        @Column(name = "issuer") private final String issuer;
        @Column(name = "property") private final String property;
        @Column(name = "owner") private final String owner;

        @OneToOne(cascade = CascadeType.PERSIST)
        @JoinColumns({
                @JoinColumn(name = "detail_id", referencedColumnName = "detail_id"),
        })
        private final PersistentProductDetail productDetail;

        public PersistentProduct(UUID id, Integer sku, String name, String issuer, String property, String owner, PersistentProductDetail productDetail) {
            this.id = id;
            this.sku = sku;
            this.name = name;
            this.issuer = issuer;
            this.property = property;
            this.owner = owner;
            this.productDetail = productDetail;
        }

        public PersistentProduct() {
            productDetail = null;
            id = null;
            sku = null;
            name = null;
            issuer = null;
            property = null;
            owner = null;
        }

        public UUID getId() {
            return id;
        }

        public Integer getSku() {
            return sku;
        }

        public String getName() {
            return name;
        }

        public String getProperty() {
            return property;
        }

        public String getOwner() {
            return owner;
        }

        public PersistentProductDetail getProductDetail() {
            return productDetail;
        }

        public String getIssuer() {
            return issuer;
        }

        @Override
        public String toString() {
            return "Id : " + id + "sku : " + sku+ "name : " + name+ "issuer : " + issuer+ "property : " + property+ "owner : " + owner;
        }
    }

    @Entity
    @Table(name = "product_detail")
    @CordaSerializable
    public static class PersistentProductDetail {

        @Column(name = "detail") private final String detail;
        @Id
        @Column(name = "detail_id")
        private final Integer detail_id;

        public PersistentProductDetail(String detail, Integer detail_id) {
            this.detail = detail;
            this.detail_id = detail_id;
        }

        public PersistentProductDetail() {
            detail = null;
            detail_id = null;
        }

        public String getDetail() {
            return detail;
        }

        public Integer getDetail_id() {
            return detail_id;
        }
    }

}