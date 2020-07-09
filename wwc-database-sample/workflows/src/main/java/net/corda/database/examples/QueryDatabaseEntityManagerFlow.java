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

        @Suspendable
        @Override
        public String call() {

            String result = "Product Details : ";

            List<ProductSchemaV1.PersistentProduct> list = getServiceHub().withEntityManager(entityManager -> {

                CriteriaQuery<ProductSchemaV1.PersistentProduct> query = entityManager.getCriteriaBuilder().createQuery(ProductSchemaV1.PersistentProduct.class);
                Root<ProductSchemaV1.PersistentProduct> type = query.from(ProductSchemaV1.PersistentProduct.class);
                query.select(type);
                return entityManager.createQuery(query).getResultList();

            });

            for(ProductSchemaV1.PersistentProduct product : list) {
                result += product.toString() + "\n";
            }

            return result;
        }
}
