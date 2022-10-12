package mylaba.ua.lb2.parser.methods;

import mylaba.ua.lb2.Start;
import mylaba.ua.lb2.model.Entity;
import mylaba.ua.lb2.model.driver.Car;
import mylaba.ua.lb2.model.driver.Driver;
import mylaba.ua.lb2.model.order.Order;
import mylaba.ua.lb2.model.order.Orders;
import mylaba.ua.lb2.model.user.EmailOrPhone;
import mylaba.ua.lb2.model.user.User;
import mylaba.ua.lb2.parser.LINK;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.math.BigDecimal;

public class SaxParserImpl extends DefaultHandler implements LINK {

    private String current;
    private Orders orders;
    private EmailOrPhone emailOrPhone;
    private User user;
    private Driver driver;
    private Car car;
    private Order order;
    private String buffer;

    public Orders startParse(String link) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            Start.isValid(LINK.VALIDATION_SCHEME, link);
            SAXParser parser = factory.newSAXParser();
            parser.parse(link, this);
        } catch (ParserConfigurationException e ) {
            e.printStackTrace();
        }catch (IOException | SAXException e){
            Start.LOGGER.warning(e.getMessage());
        }
        return orders;
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        current = qName;
        if (("ord:orders".equals(current)|| localName.equals("orders")) && uri.equals(ORDERS_NAMESPACE)) {
            orders = new Orders();
        } else if (("ord:order".equals(current)|| localName.equals("order")) && uri.equals(ORDERS_NAMESPACE)) {
            order = new Order();
            orders.getOrder().add(order);
            setAttr(order,attributes);
        } else if (("ord:driver".equals(current)|| localName.equals("driver")) && uri.equals(ORDERS_NAMESPACE)) {
            driver = new Driver();
            order.setDriver(driver);
            setAttr(driver,attributes);
        } else if (("driv:car".equals(current)|| localName.equals("car")) && uri.equals(DRIVER_NAMESPACE)) {
            car = new Car();
            driver.setCar(car);
            setAttr(car,attributes);
        } else if (("ord:user".equals(current)|| localName.equals("user")) && uri.equals(ORDERS_NAMESPACE)) {
            user = new User();
            order.setUser(user);
            setAttr(user,attributes);
        } else if (("user:answer".equals(current)|| localName.equals("answer")) && uri.equals(USER_NAMESPACE)) {
            emailOrPhone = new EmailOrPhone();
            user.setAnswer(emailOrPhone);
        }
    }

    private<T extends Entity> void setAttr(T entity,Attributes attributes){
        entity.setId(Integer.parseInt(attributes.getValue("id")));
        entity.setCode(attributes.getValue("code"));
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        buffer = new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName != null && buffer != null) {

            if ((qName.equals("driv:name") || localName.equals("name")) && uri.equals(DRIVER_NAMESPACE))
                driver.setName(buffer);
            if ((qName.equals("driv:surname")|| localName.equals("surname")) && uri.equals(DRIVER_NAMESPACE) )
                driver.setSurname(buffer);
            if ((qName.equals("driv:age")|| localName.equals("age")) && uri.equals(DRIVER_NAMESPACE))
                driver.setAge(Integer.parseInt(buffer));
            if ((qName.equals("driv:phone")|| localName.equals("phone")) && uri.equals(DRIVER_NAMESPACE))
                driver.setPhone(buffer);
            if ((qName.equals("driv:mark")|| localName.equals("mark")) && uri.equals(DRIVER_NAMESPACE))
                driver.setMark(BigDecimal.valueOf(Double.parseDouble(buffer)));

            if ((qName.equals("driv:brand")|| localName.equals("brand")) && uri.equals(DRIVER_NAMESPACE))
                car.setBrand(buffer);
            if ((qName.equals("driv:c_name")|| localName.equals("c_name")) && uri.equals(DRIVER_NAMESPACE))
                car.setName(buffer);
            if ((qName.equals("driv:yearOfProduction")|| localName.equals("yearOfProduction")) && uri.equals(DRIVER_NAMESPACE))
                car.setYearOfProduction(Integer.parseInt(buffer));
            if ((qName.equals("driv:number")|| localName.equals("number")) && uri.equals(DRIVER_NAMESPACE))
                car.setNumber(buffer);
            if ((qName.equals("driv:vinNumber")|| localName.equals("vinNumber")) && uri.equals(DRIVER_NAMESPACE))
                car.setVinNumber(buffer);
            if ((qName.equals("driv:c_class")|| localName.equals("c_class")) && uri.equals(DRIVER_NAMESPACE))
                car.setCClass(buffer);
            if ((qName.equals("driv:color")|| localName.equals("color")) && uri.equals(DRIVER_NAMESPACE))
                car.setColor(buffer);

            if ((qName.equals("user:name")|| localName.equals("name")) && uri.equals(USER_NAMESPACE))
                user.setName(buffer);
            if ((qName.equals("user:surname")|| localName.equals("surname")) && uri.equals(USER_NAMESPACE))
                user.setSurname(buffer);
            if ((qName.equals("user:addressFrom")|| localName.equals("addressFrom")) && uri.equals(USER_NAMESPACE))
                user.setAddressFrom(buffer);
            if ((qName.equals("user:addressTo")|| localName.equals("addressTo")) && uri.equals(USER_NAMESPACE))
                user.setAddressTo(buffer);
            if ((qName.equals("user:phone")|| localName.equals("phone")) && uri.equals(USER_NAMESPACE))
                emailOrPhone.setPhone(buffer);
            if ((qName.equals("user:email")|| localName.equals("email")) && uri.equals(USER_NAMESPACE))
                emailOrPhone.setEmail(buffer);

            if ((qName.equals("ord:price")|| localName.equals("price")) && uri.equals(ORDERS_NAMESPACE))
                order.setPrice(BigDecimal.valueOf(Double.parseDouble(buffer)));
            if ((qName.equals("ord:mark")|| localName.equals("mark")) && uri.equals(ORDERS_NAMESPACE))
                order.setMark(Integer.parseInt(buffer));

        }
    }
}


