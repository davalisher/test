package uz.pdp.log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.*;

public class Main {
    {

    }

    public static Logger logger = Logger.getLogger("My logger");

    public static void main(String[] args) throws IOException {
//        try {
        LogManager.getLogManager().readConfiguration(new FileInputStream("resources/logging.properties"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        logger.setUseParentHandlers(false);
//        logger.log(Level.INFO,"I'm Fine");
//        logger.setLevel(Level.FINE);
        Handler handler = new FileHandler("/sdsdds", 5000, 5, true);
        logger.log(Level.INFO, "Congfig level");
    }

    public class MyFilter implements Filter {

        @Override
        public boolean isLoggable(LogRecord log) {
            //don't log CONFIG logs in file
            if (log.getLevel() == Level.CONFIG) return false;
            return true;
        }

    }
}

