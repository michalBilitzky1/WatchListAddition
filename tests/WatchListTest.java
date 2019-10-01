import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.LinkedHashMap;


public class WatchListTest {
private WatchList watchList;


    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Test
    public void FileNotFound() throws Exception {
        watchList = new WatchList("not exist");
        thrown.expectMessage("File does not exist");
        watchList.ReadFile();
    }

    @Test
    public void FileIsEmpty() throws Exception {
        watchList = new WatchList("tv shows and ratings2.txt");
        LinkedHashMap SeriesAndRatings = watchList.ReadFile();
        watchList.addToWatchList(SeriesAndRatings);

    }

      @Test
      // checks that the addition to the watchlist is coreect
    public void RegularAddition() throws Exception {
        watchList = new WatchList("tv Shows and ratings.txt");
        LinkedHashMap SeriesAndRatings = watchList.ReadFile();
        watchList.addToWatchList(SeriesAndRatings);

    }
}