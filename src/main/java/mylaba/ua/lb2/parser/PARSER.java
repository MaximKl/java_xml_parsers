package mylaba.ua.lb2.parser;

import jakarta.xml.bind.*;
import mylaba.ua.lb2.Start;
import mylaba.ua.lb2.model.order.Orders;
import mylaba.ua.lb2.parser.methods.DomParserImpl;
import mylaba.ua.lb2.parser.methods.SaxParserImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.SchemaFactory;
import java.io.*;

public enum PARSER implements LINK {
    DOM,
    SAX,
    JAXB;


    public void deMarshal(String xmlFileName) {
        switch (this) {
            case DOM -> {
                System.out.println("\n\t\t=====ПРАЦЮЄ DOM-ПАРСЕР (демаршалер)=====");
                Orders ords = domDemarshaller(PATH + xmlFileName + ".xml");
                if (ords != null)
                    ords.getOrder().forEach(System.out::println);
                System.out.println("\n\t\t=====DOM-ПАРСЕР ЗАВЕРШИВ РОБОТУ=====");
            }
            case SAX -> {
                SaxParserImpl spi = new SaxParserImpl();
                System.out.println("\n\t\t=====ПРАЦЮЄ SAX-ПАРСЕР (демаршалер)=====");
                Orders ords = spi.startParse(PATH + xmlFileName + ".xml");
                if (ords != null)
                    ords.getOrder().forEach(System.out::println);
                System.out.println("\n\t\t=====SAX-ПАРСЕР ЗАВЕРШИВ РОБОТУ=====\n");
            }
            case JAXB -> {
                System.out.println("\n\t\t=====ПРАЦЮЄ JAXB-ПАРСЕР (демаршалер)=====");
                Orders ords = jaxbDemarshaller(PATH + xmlFileName + ".xml");
                if (ords != null)
                    ords.getOrder().forEach(System.out::println);
                System.out.println("\n\t\t=====JAXB-ПАРСЕР ЗАВЕРШИВ РОБОТУ=====\n");
            }
        }
    }

    public void marshal(String xmlFileName, Orders orders) {
        switch (this) {
            case DOM -> {
                System.out.println("\n\t\t=====ПРАЦЮЄ DOM-ПАРСЕР (маршалер)=====");
                domMarshaller(PATH + xmlFileName, orders);
                System.out.println("\n\t\t=====DOM-ПАРСЕР ЗАВЕРШИВ РОБОТУ=====\n");
            }
            case JAXB -> {
                System.out.println("\n\t\t=====ПРАЦЮЄ JAXB-ПАРСЕР (маршалер)=====");
                jaxbMarshaller(PATH + xmlFileName, orders);
                System.out.println("\n\t\t=====JAXB-ПАРСЕР ЗАВЕРШИВ РОБОТУ=====\n");
            }
        }
    }

    private static Orders domDemarshaller(String link) {
        DomParserImpl dpi = new DomParserImpl();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Orders orders = new Orders();
        try {
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setSchema(SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(VALIDATION_SCHEME)));
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new DefaultHandler() {
                @Override
                public void error(SAXParseException e) throws SAXException {
                    throw new ObjectNotValidException(e.getMessage());
                }
            });
            NodeList nodes = db.parse(new FileInputStream(link)).getDocumentElement().getElementsByTagNameNS(ORDERS_NAMESPACE, "order");
            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(i).getLocalName().equals("order"))
                    orders.getOrder().add(dpi.parseOrder(nodes.item(i)));
            }
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        } catch (IOException | ObjectNotValidException  e) {
            Start.LOGGER.warning(e.getMessage());
        }
        return orders;
    }


    private Orders jaxbDemarshaller(String link) {
        try {
            Start.isValid(VALIDATION_SCHEME, link);
            Unmarshaller um = JAXBContext.newInstance(OBJECT_FACTORY).createUnmarshaller();
            return (Orders) um.unmarshal(new File(link));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } catch (IOException |SAXException e) {
            Start.LOGGER.warning(e.getMessage());
            return null;
        }
    }

    private void domMarshaller(String link, Orders orders) {
        DomParserImpl dpi = new DomParserImpl();
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = doc.createElementNS(ORDERS_NAMESPACE, "orders");
            doc.appendChild(root);
            root.setPrefix("ord");
            root.setAttribute("xmlns:driv", DRIVER_NAMESPACE);
            root.setAttribute("xmlns:user", USER_NAMESPACE);

            for (int i = 0; i < orders.getOrder().size(); i++) {
                root.appendChild(dpi.createOrder(doc, orders.getOrder().get(i)));
            }

            Transformer transf = TransformerFactory.newInstance().newTransformer();

            transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transf.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);

            File myFile = new File(link+".xml");

            StreamResult console = new StreamResult(System.out);
            StreamResult file = new StreamResult(myFile);

            transf.transform(source, file);
            Start.isValid(VALIDATION_SCHEME, link+".xml");
            transf.transform(source, console);

            System.out.println("\n\t\tСтворення XML-файлу завершено, можете знайти його по наступному шляху -> "+link+".xml");
            Start.toHTML(HTML_SCHEMA,link + ".xml",link+".html");
            System.out.println("\t\tПереглянути файл у форматі HTML можна за посиланням -> http://localhost:63342/LB2/"+link+".html\n");
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException| IOException | SAXException e) {
            Start.LOGGER.warning(e.getMessage());
        }
    }


    private void jaxbMarshaller(String link, Orders orders) {
        try {
            Marshaller marshaller = JAXBContext.newInstance(OBJECT_FACTORY).createMarshaller();
            JAXBElement<Orders> jorder = new JAXBElement<>(
                    new QName(ORDERS_NAMESPACE, "orders"),
                    Orders.class, orders);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(jorder, new File(link+".xml"));
            Start.isValid(VALIDATION_SCHEME, link+".xml");

            System.out.println("\n\t\tСтворення XML-файлу завершено, можете знайти його по наступному шляху -> "+link+".xml");
            Start.toHTML(HTML_SCHEMA,link + ".xml",link+".html");
            System.out.println("\t\tПереглянути файл у форматі HTML можна за посиланням -> http://localhost:63342/LB2/"+link+".html\n");
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } catch (IOException| TransformerException| SAXException e) {
            Start.LOGGER.warning(e.getMessage());
        }
    }
}

