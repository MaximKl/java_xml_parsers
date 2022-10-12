package mylaba.ua.lb2.parser;

public interface LINK {

    String VALIDATION_SCHEME = "src/xml/completeOrders.xsd";

    String HTML_SCHEMA = "src/xml/browserOutput.xsl";

    String PATH = "src/xml/";

    Class<?> OBJECT_FACTORY = mylaba.ua.lb2.model.order.ObjectFactory.class;

    String ORDERS_NAMESPACE = "http://model.lb2.ua.mylaba/order/";
    String DRIVER_NAMESPACE = "http://model.lb2.ua.mylaba/driver/";
    String USER_NAMESPACE = "http://model.lb2.ua.mylaba/user/";
}
