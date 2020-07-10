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

    private Integer sku;
    private String name;
    private Integer detail_id;
    private String product_details;

    public QueryDatabaseJdbcFlow(Integer sku, String name, Integer detail_id, String product_details) {
        this.sku = sku;
        this.name = name;
        this.detail_id = detail_id;
        this.product_details = product_details;
    }

    @Suspendable
    @Override
    public String call() {


        String result = "Product Details : ";
        String SQL_INSERT_PRODUCT_DETAIL = "INSERT INTO product_detail VALUES (?,?)";
        String SQL_INSERT_PRODUCT = "INSERT INTO product VALUES (?,?,?)";


        //insert
        try {

            Connection connection = getServiceHub().jdbcSession();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_PRODUCT_DETAIL);
            preparedStatement.setInt(1, detail_id);
            preparedStatement.setString(2, product_details);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(SQL_INSERT_PRODUCT);
            preparedStatement.setInt(1, sku);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, detail_id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            result = "There is an exception" + e.getMessage();
        }

        //retrieve
        try {
            Connection connection = getServiceHub().jdbcSession();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT sku, name , p.detail_id, detail FROM product_detail pd , product p where pd.detail_id = p.detail_id");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Integer sku = rs.getInt("sku");
                String name = rs.getString("name");
                Integer detail_id = rs.getInt("detail_id");
                String detail = rs.getString("detail");
                result += "sku : " + sku+ " name : "  + name + " detail_id : "  + detail_id + " detail : "  + detail +"\n";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            result = "There is an exception" + e.getMessage();
        }

        return result;
    }
}
