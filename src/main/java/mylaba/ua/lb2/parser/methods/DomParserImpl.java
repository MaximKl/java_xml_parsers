package mylaba.ua.lb2.parser.methods;

import mylaba.ua.lb2.model.Entity;
import mylaba.ua.lb2.model.driver.Car;
import mylaba.ua.lb2.model.driver.Driver;
import mylaba.ua.lb2.model.order.Order;
import mylaba.ua.lb2.model.user.EmailOrPhone;
import mylaba.ua.lb2.model.user.User;
import mylaba.ua.lb2.parser.LINK;
import org.w3c.dom.*;

import java.math.BigDecimal;

public class DomParserImpl {

    public Order parseOrder(Node node) {
        Order order = new Order();
        checkAttrs(order, node);
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            if ("driver".equals(item.getLocalName()))
                order.setDriver(parseDriver(item));
            if ("user".equals(item.getLocalName()))
                order.setUser(parseUser(item));
            if ("price".equals(item.getLocalName()))
                order.setPrice(BigDecimal.valueOf(Double.parseDouble(item.getTextContent())));
            if ("mark".equals(item.getLocalName()))
                order.setMark(Integer.parseInt(item.getTextContent()));
        }
        return order;
    }

    public Node createOrder(Document doc, Order order) {
        Element orderEl = doc.createElementNS(LINK.ORDERS_NAMESPACE, "ord:order");
        createAttrs(order, orderEl);

        Element driverEl = doc.createElement("ord:driver");
        createAttrs(order.getDriver(), driverEl);
        orderEl.appendChild(createDriver(doc, driverEl, order.getDriver()));

        Element userEl = doc.createElement("ord:user");
        createAttrs(order.getUser(), userEl);
        orderEl.appendChild(createUser(doc, userEl, order.getUser()));

        Element priceEl = doc.createElement("ord:price");
        orderEl.appendChild(priceEl);
        priceEl.appendChild(doc.createTextNode(order.getPrice().toString()));

        Element markEl = doc.createElement("ord:mark");
        orderEl.appendChild(markEl);
        markEl.appendChild(doc.createTextNode(Integer.toString(order.getMark())));

        return orderEl;
    }

    private <T extends Entity> void createAttrs(T ent, Element el) {
        el.setAttribute("id", Integer.toString(ent.getId()));
        if (ent.getCode() != null) {
            if (!ent.getCode().isBlank())
                el.setAttribute("code", ent.getCode());
        }
    }

    private <T extends Entity> void checkAttrs(T ent, Node node) {
        if (node.hasAttributes()) {
            NamedNodeMap attrs = node.getAttributes();
            Node item = attrs.getNamedItem("id");
            ent.setId(Integer.parseInt(item.getTextContent()));
            item = attrs.getNamedItem("code");
            if (item != null)
                ent.setCode(item.getTextContent());
        }
    }

    private Driver parseDriver(Node node) {
        Driver driver = new Driver();
        checkAttrs(driver, node);
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            if (item.getLocalName() != null) {
                if (item.getLocalName().equals("name"))
                    driver.setName(item.getTextContent());
                if (item.getLocalName().equals("surname"))
                    driver.setSurname(item.getTextContent());
                if (item.getLocalName().equals("age"))
                    driver.setAge(Integer.parseInt(item.getTextContent()));
                if (item.getLocalName().equals("phone"))
                    driver.setPhone(item.getTextContent());
                if (item.getLocalName().equals("mark"))
                    driver.setMark(BigDecimal.valueOf(Double.parseDouble(item.getTextContent())));
                if (item.getLocalName().equals("car"))
                    driver.setCar(parseCar(item));
            }
        }
        return driver;
    }

    private User parseUser(Node node) {
        User user = new User();
        checkAttrs(user, node);
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            if (item.getLocalName() != null) {
                if (item.getLocalName().equals("name"))
                    user.setName(item.getTextContent());
                if (item.getLocalName().equals("surname"))
                    user.setSurname(item.getTextContent());
                if (item.getLocalName().equals("addressFrom"))
                    user.setAddressFrom(item.getTextContent());
                if (item.getLocalName().equals("addressTo"))
                    user.setAddressTo(item.getTextContent());
                if (item.getLocalName().equals("answer")) {
                    user.setAnswer(new EmailOrPhone());
                    if (item.getChildNodes().item(1).getLocalName().equals("phone"))
                        user.getAnswer().setPhone(item.getChildNodes().item(1).getTextContent());
                    if (item.getChildNodes().item(1).getLocalName().equals("email"))
                        user.getAnswer().setEmail(item.getChildNodes().item(1).getTextContent());
                }
            }
        }
        return user;
    }

    private Car parseCar(Node node) {
        Car car = new Car();
        checkAttrs(car, node);
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            if (item.getLocalName() != null) {
                if (item.getLocalName().equals("brand"))
                    car.setBrand(item.getTextContent());
                if (item.getLocalName().equals("c_name"))
                    car.setName(item.getTextContent());
                if (item.getLocalName().equals("yearOfProduction"))
                    car.setYearOfProduction(Integer.parseInt(item.getTextContent()));
                if (item.getLocalName().equals("number"))
                    car.setNumber(item.getTextContent());
                if (item.getLocalName().equals("vinNumber"))
                    car.setVinNumber(item.getTextContent());
                if (item.getLocalName().equals("c_class"))
                    car.setCClass(item.getTextContent());
                if (item.getLocalName().equals("color"))
                    car.setColor(item.getTextContent());
            }
        }
        return car;
    }


    private Node createDriver(Document document, Element element, Driver driver) {
        Element nameEl = document.createElement("driv:name");
        element.appendChild(nameEl);
        nameEl.appendChild(document.createTextNode(driver.getName()));

        Element surnameEl = document.createElement("driv:surname");
        element.appendChild(surnameEl);
        surnameEl.appendChild(document.createTextNode(driver.getSurname()));

        Element ageEl = document.createElement("driv:age");
        element.appendChild(ageEl);
        ageEl.appendChild(document.createTextNode(Integer.toString(driver.getAge())));

        Element phoneEl = document.createElement("driv:phone");
        element.appendChild(phoneEl);
        phoneEl.appendChild(document.createTextNode(driver.getPhone()));

        Element markEl = document.createElement("driv:mark");
        element.appendChild(markEl);
        markEl.appendChild(document.createTextNode(driver.getMark().toString()));


        Element carEl = document.createElement("driv:car");
        createAttrs(driver.getCar(), carEl);
        element.appendChild(carEl);

        Element brandEl = document.createElement("driv:brand");
        carEl.appendChild(brandEl);
        brandEl.appendChild(document.createTextNode(driver.getCar().getBrand()));


        Element carNameEl = document.createElement("driv:c_name");
        carEl.appendChild(carNameEl);
        carNameEl.appendChild(document.createTextNode(driver.getCar().getName()));

        Element yearEl = document.createElement("driv:yearOfProduction");
        carEl.appendChild(yearEl);
        yearEl.appendChild(document.createTextNode(Integer.toString(driver.getCar().getYearOfProduction())));

        Element numberEl = document.createElement("driv:number");
        carEl.appendChild(numberEl);
        numberEl.appendChild(document.createTextNode(driver.getCar().getNumber()));

        Element vinNumberEl = document.createElement("driv:vinNumber");
        carEl.appendChild(vinNumberEl);
        vinNumberEl.appendChild(document.createTextNode(driver.getCar().getVinNumber()));

        Element carClassEl = document.createElement("driv:c_class");
        carEl.appendChild(carClassEl);
        carClassEl.appendChild(document.createTextNode(driver.getCar().getCClass()));

        Element colorEl = document.createElement("driv:color");
        carEl.appendChild(colorEl);
        colorEl.appendChild(document.createTextNode(driver.getCar().getColor()));

        return element;
    }

    private Node createUser(Document document, Element element, User user) {
        if (user.getName() != null) {
            Element nameEl = document.createElement("user:name");
            element.appendChild(nameEl);
            nameEl.appendChild(document.createTextNode(user.getName()));
        }
        if (user.getSurname() != null) {
            Element surnameEl = document.createElement("user:surname");
            element.appendChild(surnameEl);
            surnameEl.appendChild(document.createTextNode(user.getSurname()));
        }

        Element addressFromEl = document.createElement("user:addressFrom");
        element.appendChild(addressFromEl);
        addressFromEl.appendChild(document.createTextNode(user.getAddressFrom()));

        Element addressToEl = document.createElement("user:addressTo");
        element.appendChild(addressToEl);
        addressToEl.appendChild(document.createTextNode(user.getAddressTo()));

        Element answerEl = document.createElement("user:answer");
        element.appendChild(answerEl);

        if (user.getAnswer().getEmail() != null) {
            Element byEmail = document.createElement("user:email");
            answerEl.appendChild(byEmail);
            byEmail.appendChild(document.createTextNode(user.getAnswer().getEmail()));
        } else {
            Element byPhone = document.createElement("user:phone");
            answerEl.appendChild(byPhone);
            byPhone.appendChild(document.createTextNode(user.getAnswer().getPhone()));
        }

        return element;
    }


}
