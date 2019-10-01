import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.util.*;

public class WatchList {

    public String fileName; // the name of the file to be read from

    public WatchList(String fileName){
        this.fileName = fileName;
    }


    /**
     * reads the and creates a map of tv show and minimum rating grade
     **/
    public LinkedHashMap ReadFile () throws Exception {
        LinkedHashMap<String,Float> SeriesAndRatings = new LinkedHashMap<>();
        try {
            File file = new File(this.fileName);
            if (!file.exists()) {
                throw new Exception("File does not exist");
            }
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            String line;
            int counterLine=1;
            String[] series = new String[3];
            float rating =0;

        // every 2 lines represents the series and the rating grade
            while ((line = br.readLine()) != null){
                if(counterLine==1){
                    // each odd line is a line that contains series
                    String newLine = line.replace("tv:","");
                    series = newLine.split(",");
                    counterLine++;

                }
                else {
                    //each even line is a line that contains rating
                    String newLine = line.replace("ratings:", "");
                    rating = Float.parseFloat(newLine);
                    counterLine = 1;

                    for (int i = 0; i < series.length; i++) {
                        SeriesAndRatings.put(series[i], rating);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return SeriesAndRatings;
    }


    /**
     * check if the rating of each series in the map is greater or equal to the matching rating from the map
     **/
    public void addToWatchList(LinkedHashMap SeriesAndRatings){
        // a map of series and ratings
        LinkedHashMap<String, Float> map = SeriesAndRatings;

        System.setProperty("webdriver.chrome.driver","C:\\Users\\user\\IdeaProjects\\WatchListAddition\\chromedriver.exe");
        WebDriver obj = new ChromeDriver();
        obj.get("https://www.imdb.com");
        for (Map.Entry<String, Float> entry : map.entrySet()) {
            //finding the web element using name locator
            WebElement elem = obj.findElement(By.name("q"));
            //look for the wanted series
            elem.sendKeys(new String[]{entry.getKey()});
            // show the results of the search
            elem.submit();
            List<WebElement> rowSet = obj.findElements(By.tagName("tr"));
            // choose the tv series (first result)
            WebElement requiredElem = rowSet.get(0);
            String text = requiredElem.getText();
            // take the title of the series
            String[] output = text.split("\\(");
            String trim  = output[0].trim();
            // go to the link of the series
            obj.findElement(By.linkText(trim)).click();
            //check the rating value
            WebElement element = obj.findElement(By.className("ratingValue"));
            String[] rating = element.getText().split("/");
            float ratingGrade = Float.parseFloat(rating[0]);
            // if the rating value is greater or equal to the match value in the map than add it to the watchlist
            if(ratingGrade>=entry.getValue()){
                obj.findElement(By.cssSelector("#title-overview-widget > div.vital > div.title_block > div > div.titleBar > div.primary_ribbon > div.ribbonize > div")).click();

            }

        }
        obj.close();

    }

}
