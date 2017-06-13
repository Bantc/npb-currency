package pl.parser.nbp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class CurrencyXMLTable {
    private URL yearURL;
    private LocalDate startDate;
    private LocalDate endDate;
    private ArrayList<String> currrencyXMLYearTable;    //tablica zawierajaca liste nazw plikow XML z okreslonych lat
    private ArrayList<String> chosenDataTable;          //tablica zawierajace liste nazw plikow z okreslonego przedzialu
    private int startIndex;
    private int endIndex;

    public CurrencyXMLTable(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        ToCurrencyTable();
    }

    //Pobiera nazwy plikow z kursem z odpowiednich lat i zapisuje w tabeli
    public void ToCurrencyTable(){
        currrencyXMLYearTable = new ArrayList<>();
        try {
            for(int i = startDate.getYear(); i <= endDate.getYear(); i++) {
                yearURL = new URL("http://www.nbp.pl/kursy/xml/dir" + i + ".txt");
                BufferedReader in = new BufferedReader(new InputStreamReader(yearURL.openStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.charAt(0) == 'c')
                        currrencyXMLYearTable.add(line);
                }
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Z tabeli zawierajacej nazwy plikow z kursem z odpowiednich lat pobiera i zapisuje do drugiej tabeli nazwy
    //plikow z wybranego przedzialu czasowego
    public void ToChosenCurrencyTable() {
        LocalDate sDate = startDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuMMdd");
        String startDate = sDate.format(formatter);

        LocalDate eDate = endDate;
        String endDate = eDate.format(formatter);

        Scanner in = new Scanner(System.in);

        System.out.println("\n");
        for (String element : currrencyXMLYearTable) {
            if (element.endsWith(String.valueOf(startDate))) {
                startIndex = (currrencyXMLYearTable.indexOf(element));
                //System.out.println(startIndex);
            }

            if (element.endsWith(String.valueOf(endDate))) {
                endIndex = (currrencyXMLYearTable.indexOf(element));
                //System.out.println(endIndex);
            }
        }
            chosenDataTable = new ArrayList<>(currrencyXMLYearTable.subList(startIndex, endIndex+1));
    }

    public void showCurrencyXMLYearTable() {
        for (String element : currrencyXMLYearTable)
            System.out.println(element);
    }

    public void showChosenDataTable() {
        for (String element : chosenDataTable)
            System.out.println(element);
    }

    //public LocalDate getStartDate() { return startDate; }
    //public LocalDate getEndDate() { return endDate; }
    public ArrayList<String> getCurrencyXMLYearTable() { return currrencyXMLYearTable; }
    public ArrayList<String> getChosenDataTable() { return chosenDataTable; }
}
