package pl.parser.nbp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class CurrencyXMLReader {
    private ArrayList<String> tableOfXML;
    private ArrayList<Double> buyTable;
    private ArrayList<Double> sellTable;
    private String currency;
    private LocalDate startDate;
    private LocalDate endDate;

    public CurrencyXMLReader(String currency, CurrencyXMLTable tableOfXML, LocalDate startDate, LocalDate endDate) {
        this.currency = currency;
        this.tableOfXML = tableOfXML.getChosenDataTable();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void parseXMLFile() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            buyTable = new ArrayList<>();
            sellTable = new ArrayList<>();

            for (String element : tableOfXML) {
                Document doc = builder.parse("http://www.nbp.pl/kursy/xml/" + element + ".xml");
                doc.getDocumentElement().normalize();
                readXMLFile(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readXMLFile(Document doc) {
        NodeList nodeList = doc.getElementsByTagName("pozycja");
        for(int i = 0; i<nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)node;
                if(currency.equals(element.getElementsByTagName("kod_waluty").item(0).getTextContent())) {
                    try {
                        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);

                        buyTable.add(numberFormat.parse(element.getElementsByTagName("kurs_kupna").item(0).getTextContent()).doubleValue());
                        sellTable.add(numberFormat.parse(element.getElementsByTagName("kurs_sprzedazy").item(0).getTextContent()).doubleValue());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    public double averageBuyRate() {
        double sum = 0;

        for (double element : buyTable)
            sum += element;

        return sum/buyTable.size();
    }

    public double averageSellRate() {
        double sum = 0;

        for (double element : sellTable)
            sum += element;

        return sum/sellTable.size();
    }

    public double sellStandardDeviation() {
        double avg = averageSellRate();
        double standardDeviation;
        double elementsPow = 0;

        for(double element : sellTable)
            elementsPow += (Math.pow(element - avg, 2));
        standardDeviation = Math.sqrt((elementsPow/3));

        return standardDeviation;
    }

    public void showResults() {
        System.out.println("Wyniki dla danych od dnia " + startDate + " do " + endDate + " sa nastepujace:");
        System.out.println("Waluta: " + currency);
        System.out.printf("Sredni kurs kupna: %.4f\n", averageBuyRate());
        System.out.printf("Odchylenie standardowe kursu sprzedazy: %.4f\n", sellStandardDeviation());
    }

    public void showTableOfXML() {
        System.out.println("Tabela z nazwa XML");
        for (String element : tableOfXML)
            System.out.println(element);
    }

    public void showBuyTable() {
        System.out.println("Tabela z kursem kupna");
        for (Double element : buyTable)
            System.out.println(element);
    }

    public void showSellTable() {
        System.out.println("Tabela z kursem sprzedazy");
        for (Double element : sellTable)
            System.out.println(element);
    }
}
