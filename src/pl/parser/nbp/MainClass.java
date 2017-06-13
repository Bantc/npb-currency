package pl.parser.nbp;

import java.time.LocalDate;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Podaj walute: ");
        String currency = in.nextLine().toUpperCase();
        System.out.print("Podaj date poczatkowa: ");
        String sDate = in.nextLine();
        System.out.print("Podaj date koncowa: ");
        String eDate = in.nextLine();

        LocalDate startDate = LocalDate.parse(sDate);
        LocalDate endDate = LocalDate.parse(eDate);

        CurrencyXMLTable yearTable = new CurrencyXMLTable(startDate, endDate);
        //yearTable.showCurrencyXMLYearTable();
        yearTable.ToChosenCurrencyTable();
        //yearTable.showChosenDataTable();
        CurrencyXMLReader currencyReader = new CurrencyXMLReader(currency, yearTable, startDate, endDate);
        currencyReader.parseXMLFile();
        currencyReader.showResults();
        //currencyReader.showTableOfXML();
        //currencyReader.showBuyTable();
        //currencyReader.showSellTable();
    }
}
