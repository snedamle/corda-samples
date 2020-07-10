package net.corda.database.examples;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.database.examples.schema.ProductSchemaV1;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@InitiatingFlow
@StartableByRPC
public class QueryDatabaseEntityManagerFlow extends FlowLogic<String> {

    private Integer sku;
    private String name;
    private Integer detail_id;
    private String product_details;

    public QueryDatabaseEntityManagerFlow(Integer sku, String name, Integer detail_id, String product_details) {
        this.sku = sku;
        this.name = name;
        this.detail_id = detail_id;
        this.product_details = product_details;
    }

    @Suspendable
    @Override
    public String call() {

        String result = "Product Details : ";

        try {
            //to create new products in product table
            getServiceHub().withEntityManager(entityManager -> {
                ProductSchemaV1.PersistentProductDetail persistentProductDetail = new ProductSchemaV1.PersistentProductDetail(product_details, detail_id);
                ProductSchemaV1.PersistentProduct ob = new ProductSchemaV1.PersistentProduct(sku, name, persistentProductDetail);
                entityManager.persist(ob);

            });

            //query the product table to get the records
            List<ProductSchemaV1.PersistentProduct> list = getServiceHub().withEntityManager(entityManager -> {
                CriteriaQuery<ProductSchemaV1.PersistentProduct> query = entityManager.getCriteriaBuilder().createQuery(ProductSchemaV1.PersistentProduct.class);
                Root<ProductSchemaV1.PersistentProduct> type = query.from(ProductSchemaV1.PersistentProduct.class);
                query.select(type);
                return entityManager.createQuery(query).getResultList();

            });

            for (ProductSchemaV1.PersistentProduct product : list) {
                result += product.toString() + "\n";
            }

        } catch (Exception e) {
            result = "There is an exception" + e.getMessage();
        }
        return result;
    }
}
