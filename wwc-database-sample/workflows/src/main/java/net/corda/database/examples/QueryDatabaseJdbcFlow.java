package net.corda.database.examples;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@InitiatingFlow
@StartableByRPC
public class QueryDatabaseJdbcFlow extends FlowLogic<String> {

        @Suspendable
        @Override
        public String call() {

            String result = "Product Details : ";

            try {
                Connection connection = getServiceHub().jdbcSession();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT sku, property FROM product");
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    Integer sku = rs.getInt("sku");
                    String property = rs.getString("property");
                    result += "property : " + property + "sku : " + sku+ "name : "  + "\n";
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return result;
        }
}
