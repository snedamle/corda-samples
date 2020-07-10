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

@InitiatingFlow
@StartableByRPC
public class QueryDatabaseVaultQueryFlow extends FlowLogic<String> {

    @Suspendable
        @Override
        public String call() {

            QueryCriteria queryCriteria = new QueryCriteria.VaultQueryCriteria(Vault.StateStatus.UNCONSUMED);

            StateAndRef<ProductState> stateAndRef = getServiceHub().getVaultService().queryBy(ProductState.class,queryCriteria).getStates().get(0);

            ProductState productState = stateAndRef.getState().getData();

            return productState.toString();
        }
}
