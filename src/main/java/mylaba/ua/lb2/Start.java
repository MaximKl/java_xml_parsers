package mylaba.ua.lb2;

import mylaba.ua.lb2.model.driver.Driver;
import mylaba.ua.lb2.model.order.Order;
import mylaba.ua.lb2.model.order.Orders;
import mylaba.ua.lb2.model.user.User;
import mylaba.ua.lb2.parser.PARSER;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.logging.Logger;

public class Start {

    public static final Logger LOGGER = Logger.getLogger(Start.class.getName());

    public static void isValid(String xsd, String xml) throws SAXException, IOException {
        Validator validator = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(xsd)).newValidator();
        Source source = new StreamSource(xml);
        try {
            validator.validate(source);
        } catch (IOException | SAXException ex) {
            throw ex;
        }
    }

    public static void toHTML(String xsl, String xml, String htmlSavePlace) throws TransformerException {
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer(new StreamSource(new File(xsl)));
            t.transform(new StreamSource(new File(xml)), new StreamResult(new File(htmlSavePlace)));
        } catch (TransformerException e) {
            throw e;
        }
    }

    public static void main(String[] args) {
        Orders orders = new Orders();
        Order order = new Order
                (1, "RWAER", new Driver(1, "Олег", "Петренко", 33, "+380660166700", new BigDecimal("8.34"),
                        "Dewoo", "Nexia", 1999, "AX9854CB", "SDFDSFSDFRGKBSQYU", "Економ", "Червоний"),
                        User.userWithSurname(1, "DFSFE", "Короленко", "Лопанська набережна", "Вулиця Скрипника", "dsfdfds@gmail.com"),
                        new BigDecimal("50.33"), 7);

        Order order2 = new Order
                (2, "DSFJH", new Driver(2, "Петро", "Потрошенко", 56, "0660170066", new BigDecimal("9.14"),
                        "Toyota", "Prius", 2012, "AX7561MH", "L8FDSFCVFRGHGSQ4F", "Преміум", "Білий"),
                        User.userWithNameAndSurname(2, "DSFBS","Дмитро", "Пилипко", "станція метро Наукова", "вулиця Плеханівська 12", "+380657166879"),
                        new BigDecimal("120"), 9);

        Order order3 = new Order
                (3, "SDFNB", new Driver(3,"HGJFB", "Микита", "Стерненко", 25, "0681245759", new BigDecimal("7.34"),
                        "Dewoo", "Lanos", 2005, "AX8763OX", "KDFDSFC35RG4FSQ43", "Середній", "Зелений"),
                        User.userAnonim(3, "NGFJR", "станція метро Ботанічний сад", "станція метро Холодна гора", "mykyta@gmail.com"),
                        new BigDecimal("180.56"), 4);

        Order order4 = new Order
                (4, "NDMFJ", new Driver(4, "Микола", "Гончаренко", 48, "+380995498678", new BigDecimal("5.98"),
                        "VW", "Caddy", 2003, "AX5475PH", "4FFDKC3D5RG4HJQDF", "Вантажний", "Сірий"),
                        User.userWithName(4,null, "Роман", "вулиця Соборна 43", "вулиця Золочівська", "0680477300"),
                        new BigDecimal("470"), 6);

        orders.getOrder().add(order);
        orders.getOrder().add(order2);
        orders.getOrder().add(order3);
        orders.getOrder().add(order4);

        String toContinue = "";
        while (toContinue.isBlank()) {
            System.out.print("Уведіть бажану назву файлу: ");
            String name = new Scanner(System.in).next();
            System.out.println("\nОберіть парсер для створення XML-файлу з джава класів (маршалінг) з викликами таксі\n1. DOM-парсер\n2. JAXB-парсер");
            int pars = new Scanner(System.in).nextInt();
            switch (pars) {
                case 1 -> PARSER.DOM.marshal(name, orders);
                case 2 -> PARSER.JAXB.marshal(name, orders);
                default -> System.out.println("Помилка, надано невірне значення");
            }
            System.out.print("\nУведіть файл, який потрібно запарсити: ");
            name = new Scanner(System.in).next();
            System.out.println("\nОберіть парсер для запису інформації з XML-файлів у джава класи (демаршалінг)\n1. DOM-парсер\n2. JAXB-парсер\n3. SAX-парсер");
            pars = new Scanner(System.in).nextInt();
            switch (pars) {
                case 1 -> PARSER.DOM.deMarshal(name);
                case 2 -> PARSER.JAXB.deMarshal(name);
                case 3 -> PARSER.SAX.deMarshal(name);
                default -> System.out.println("\nПомилка, надано невірне значення");
            }
            System.out.println("\nЩо б продовжити, натисніть ENTER. Для виходу натисніть будь яку літеру");
            toContinue = new Scanner(System.in).nextLine();
        }
    }
}