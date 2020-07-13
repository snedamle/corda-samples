package net.corda.database.examples;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.database.examples.states.ProductState;

import java.util.Arrays;

/**
 * This flow is used to query the Product state data from the ledger
 */
@InitiatingFlow
@StartableByRPC
public class QueryDatabaseVaultQueryFlow extends FlowLogic<String> {

    @Suspendable
        @Override
        public String call() {

            //create the queryCriteria, by passing in the required params. Unconsumed parameter will give you all the states which are not consumed till now
            //and are available for consumption
            QueryCriteria queryCriteria = new QueryCriteria.VaultQueryCriteria(Vault.StateStatus.UNCONSUMED);

            //use vaultservice to get one of the product state by specifying the query criteria
            ProductState productState = getServiceHub().getVaultService().queryBy(ProductState.class,queryCriteria).getStates().get(0).getState().getData();

            return productState.toString();
        }
}
