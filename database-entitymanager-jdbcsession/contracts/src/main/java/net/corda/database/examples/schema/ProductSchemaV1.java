package net.corda.database.examples.schema;

import net.corda.core.schemas.MappedSchema;
import net.corda.core.serialization.CordaSerializable;

import javax.persistence.*;
import java.util.Arrays;

//schemas will be stored off ledger
public class ProductSchemaV1 extends MappedSchema {
    public ProductSchemaV1() {
        super(ProductSchema.class, 1, Arrays.asList(PersistentProduct.class, PersistentProductDetail.class));
    }

    // add the entity and table annotations
    @Entity
    @Table(name = "product")
    public static class PersistentProduct {
        @Id @Column(name = "sku") private final Integer sku;
        @Column(name = "name") private final String name;

        // you could also use many to one, one to many joins
        @OneToOne(cascade = CascadeType.PERSIST)
        @JoinColumns({
                @JoinColumn(name = "detail_id", referencedColumnName = "detail_id"),
        })
        private final PersistentProductDetail productDetail;

        public PersistentProduct(Integer sku, String name, PersistentProductDetail productDetail) {
            this.sku = sku;
            this.name = name;
            this.productDetail = productDetail;
        }

        //Hibernate requires you to add a default constructor by default
        public PersistentProduct() {
            productDetail = null;
            sku = null;
            name = null;
        }

        public Integer getSku() {
            return sku;
        }

        public String getName() {
            return name;
        }

        public PersistentProductDetail getProductDetail() {
            return productDetail;
        }

        @Override
        public String toString() {
            return " sku : " + sku+ " name : " + name + " detail_id : " + productDetail.getDetail_id() + " product_detail : " + productDetail.getDetail();
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