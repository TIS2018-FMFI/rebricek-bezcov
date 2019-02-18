package GeneratingModule;
import assigningModule.*;
import configurationModule.ConfigurationFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class HtmlResultGenerator {
    private Map<String, Map<String, RunnerOverall>> results;
    private List<String> events;
    private List<String> dates;
    private List<String> categories = new ArrayList<>();
    private int countOfEvents;
    private String numOfResults = "";
    private String scoring;
    private static String mainTemplate;
    private static String navTemplate;
    private static String categoryTemplate;
    private static String runnerTemplate;
    private ConfigurationFile configurationFile = new ConfigurationFile();
    private Map<String, Map<Integer, List<RunnerOverall>>> previous = new HashMap<>();


    public HtmlResultGenerator(Map<String,  Map<String, RunnerOverall>> results, List<String> events, List<String> dates) {
        scoring = setScoring();
        numOfResults = "" + configurationFile.getRankedRounds();
        this.results = results;
        this.events = events;
        this.dates = dates;
        this.countOfEvents = events.size();

        Scanner scanner = null;
        try { scanner = new Scanner( new File("mainTemplate.txt"), "UTF-8" );
        } catch (FileNotFoundException e) { e.printStackTrace(); }
        mainTemplate = scanner.useDelimiter("\\A").next();
        scanner.close();

        try { scanner = new Scanner( new File("navTemplate.txt"), "UTF-8" );
        } catch (FileNotFoundException e) { e.printStackTrace(); }
        navTemplate = scanner.useDelimiter("\\A").next();
        scanner.close();

        try { scanner = new Scanner( new File("categTemplate.txt"), "UTF-8" );
        } catch (FileNotFoundException e) { e.printStackTrace(); }
        categoryTemplate = scanner.useDelimiter("\\A").next();
        scanner.close();

        try { scanner = new Scanner( new File("runnerTemplate.txt"), "UTF-8" );
        } catch (FileNotFoundException e) { e.printStackTrace(); }
        runnerTemplate = scanner.useDelimiter("\\A").next();
        scanner.close();
        //mainTemplate = readStringFromFile("mainTemplate.txt");
        //navTemplate = readStringFromFile("navTemplate.txt");
        //categoryTemplate = readStringFromFile("categTemplate.txt");
        //runnerTemplate = readStringFromFile("runnerTemplate.txt");
        generateFile();
    }

    private String fillMainTemplate(){
        List<String> categs = new ArrayList<>();
        categs.addAll(results.keySet());
        for(String key : categs){
            String key2 = key.replaceAll(" ", "");
            results.put(key2, results.get(key));
            if(!key.equals(key2)) {
                results.remove(key);
            }
        }
        fillPrevious(results);
        return mainTemplate
                .replaceAll("@bodovanie", scoring)
                .replaceAll("@pocetVysledkov", numOfResults)
                .replaceAll("@navigacia", fillNavTemplate())
                .replaceAll("@poradieKol", eventsOrder())
                .replaceAll("@datumyKol", eventsDates())
                .replaceAll("@nazvyKol", eventsNamesToTable())
                .replaceAll("@vysledky", allCategoriesTables());
    }

    private String fillNavTemplate(){
        StringBuilder result = new StringBuilder();
        categories.addAll(results.keySet());
        Collections.sort(categories);
        for (String categoryName: categories) {
            result.append(navTemplate.replaceAll("@kategoria", categoryName));
        }
        return result.toString();
    }

    private String fillCategoryTemplate(String categoryName){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < countOfEvents; i++){
            s.append("\t\t\t\t<td></td>\n");
        }
        String color = "green";
        if(categoryName.charAt(0) == 'M'){
            color = "blue";
        }else if(categoryName.charAt(0) =='W'){
            color = "pink";
        }
        return categoryTemplate
                .replaceAll("@podfarbenie", color)
                .replaceAll("@kategoria", categoryName)
                .replaceAll("@prazdneStlpceKol", s.toString())
                .replaceAll("@vysledkyBezcov", allRunnersCategoryTable(categoryName));
    }

    private String fillRunnerTemplate(RunnerOverall runner, int order, String categoryName){//volat pre kazdeho bezca
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < countOfEvents; i++){
            int points = runner.getRounds().get(i);
            if (points == 0) {
                s.append("\t\t\t\t<td class = \"width5\"></td>\n");
            }else if (points > 0) {
                s.append("\t\t\t\t<td class = \"width5\">").append(points).append("</td>\n");
            }else  {
                s.append("\t\t\t\t<td class = \"width5\">0</td>\n");
            }
        }
        String orderString = Integer.toString(order);
        if (runner.getPoints() == 0){
            orderString = "";
        }
        String change = getChange(runner, categoryName, order);
        String color = "blueText";
        if(change.charAt(0) == '↓'){
            color = "redText";
        }else if(change.charAt(0) == '↑'){
            color = "greenText";
        }
        return runnerTemplate
                .replaceAll("@poradie", orderString)
                .replaceAll("@farba", color)
                .replaceAll("@zmena", change)
                .replaceAll("@meno", runner.getGivenName())
                .replaceAll("@priezvisko", runner.getFamilyName())
                .replaceAll("@id", runner.getIdentificator())
                .replaceAll("@bodyKol", s.toString())
                .replaceAll("@spolu", Integer.toString(runner.getPoints()));
    }

    private String eventsNamesToTable(){
        StringBuilder result = new StringBuilder();
        for (String eventName: events) {
            result.append("\t\t\t\t<td class=\"rotate\"><div><span><b>").append(eventName).append("</b></span></div></td>\n");
        }
        return result.toString();
    }

    private String eventsOrder(){
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= countOfEvents; i++){
            result.append("\t\t\t\t<td>").append(i).append(".kolo").append("</td>\n");
        }
        return result.toString();
    }

    private String eventsDates(){
        StringBuilder result = new StringBuilder();
        for (String date : dates){
            result.append("\t\t\t\t<td><b>").append(date).append("</b></td>\n");
        }
        return result.toString();
    }

    private String allCategoriesTables(){
        boolean notFirst = false;
        StringBuilder result = new StringBuilder();
        for (String categoryName : categories) {
            if(notFirst){
                result.append("\t\t<table>\n");
                //result.append("\t\t<tr><td></td></tr>\n");
            }
            result.append(fillCategoryTemplate(categoryName));
            result.append("\t\t</table>\n");
            result.append("<table class = \"noDisplay\"><tr>\n" +
                    "\t\t\t<td><a href=\"#top\">↑</a></td>\n" +
                    "\t\t</tr></table>");
            notFirst = true;
        }
        return result.toString();
    }

    private String allRunnersCategoryTable(String categoryName){
        StringBuilder result = new StringBuilder();
        int i = 0;
        List<RunnerOverall> runners = new ArrayList<>();
        for (String id : results.get(categoryName).keySet()){
            runners.add(results.get(categoryName).get(id));
        }
        Collections.sort(runners);
        int previousRunnersPoints = 100000000;
        int num = 0;
        for (RunnerOverall runner : runners){
            num++;
            if (previousRunnersPoints > runner.getPoints()){
                i = num;
            }
            previousRunnersPoints = runner.getPoints();
            result.append(fillRunnerTemplate(runner, i, categoryName));

        }
        return result.toString();
    }

    private boolean onlyInLast(RunnerOverall runner){
        for (int i = 0; i < runner.getRounds().size()-1; i++){
            if(runner.getRounds().get(i) != 0){
                return false;
            }
        }
        return true;
    }

    private String getChange(RunnerOverall runner, String category, int order){
        if(events.size() == 1) {
            return "";
        }
        int previousOrder = 0;
        if (onlyInLast(runner)){
            return "N";
        }

        int prevOrder = results.get(category).keySet().size()+1;
        for (RunnerOverall r : results.get(category).values()){
            if (onlyInLast(r)){
                prevOrder--;
            }
        }

        for(Integer key : previous.get(category).keySet()){
            prevOrder-= previous.get(category).get(key).size();
            if (previous.get(category).get(key).contains(runner)){
                previousOrder = prevOrder;
                break;
            }
        }if(previousOrder-order < 0){
            return "↓"+(order-previousOrder);
        }else if(previousOrder-order>0){
            return "↑"+(previousOrder-order);
        }else{
            return "↔";
        }
    }
    private String readStringFromFile(String filePath){
        String fileContent = null;
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    private String setScoring(){
        String sol = "";
        switch(configurationFile.getPointingType()) {
            case(1):
                int i = 0;
                for(int p : configurationFile.getPointingList()) {
                    if (i != 0) { sol += "-"; }
                    sol += p;
                    i++;
                }
                sol += " DISK = 0";
                break;
            case(2):
                sol = "N = počet pretekárov; N-(N-1)-(N-2)-...3-2-1 DISK = 0";
                break;
            case(3):
                sol = "čas víťaza/čas pretekára * " + configurationFile.getConstant();
        }
        return sol;
    }

    private void fillPrevious(Map<String, Map<String, RunnerOverall>> results){
        for(String key : results.keySet()){
            Map<Integer, List<RunnerOverall>> points = new TreeMap<>();
            previous.put(key, points);
            for(RunnerOverall runner : results.get(key).values()){
                int lastIndex = runner.getRounds().size()-1;
                int previousPoints = runner.getPoints()-runner.getRounds().get(lastIndex);
                if(!onlyInLast(runner)) {
                    if (!previous.get(key).containsKey(previousPoints)) {
                        List<RunnerOverall> runners = new ArrayList<>();
                        runners.add(runner);
                        previous.get(key).put(previousPoints, runners);
                    } else {
                        previous.get(key).get(previousPoints).add(runner);
                    }
                }
            }
        }
    }

    public void generateFile() {
        PrintWriter resultFile = null;
        String time = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        try {
            resultFile = new PrintWriter("vystup"+time+".html", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assert resultFile != null;

        resultFile.print(fillMainTemplate());
        resultFile.close();
    }
}
