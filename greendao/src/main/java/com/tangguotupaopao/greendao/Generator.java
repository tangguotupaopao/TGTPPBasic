package com.tangguotupaopao.greendao;

/**
 * Created by gelin on 16/2/8.
 */
import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class Generator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.tangguotupaopao.greendao.dao");

        //addNote(schema);
        //addCustomerOrder(schema);
        addContacts(schema);

        new DaoGenerator().generateAll(schema, "./greendao/src/main/java");
    }

    private static void addContacts(Schema schema){
        Entity contacts = schema.addEntity("Contacts");
        contacts.addIdProperty().autoincrement();
        contacts.addStringProperty("name").notNull();
        contacts.addStringProperty("phone").notNull();
    }

    private static void addNote(Schema schema) {
        Entity note = schema.addEntity("Note");
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
        note.addDateProperty("date");
    }

    private static void addCustomerOrder(Schema schema) {
        Entity customer = schema.addEntity("Customer");
        customer.addIdProperty();
        customer.addStringProperty("name").notNull();

        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
        order.addIdProperty();
        Property orderDate = order.addDateProperty("date").getProperty();
        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
        order.addToOne(customer, customerId);

        ToMany customerToOrders = customer.addToMany(order, customerId);
        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);
    }

}
