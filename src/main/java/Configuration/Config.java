package Configuration;

import com.example.GraphicalUserInterface.Main;
import com.example.GraphicalUserInterface.ManagementMain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private Properties config;
    private String fileName;
    public Config() throws Exception {
        this.config = new Properties();
        this.fileName = Main.class.getResource("/config").getPath() + "development-duc.properties";
        InputStream is = new FileInputStream(this.fileName);
        this.config.load(is);
    }
    public Config(String configName) throws Exception {
        this.config = new Properties();
        this.fileName = Main.class.getResource("/config").getPath() + configName;
        InputStream is = new FileInputStream(this.fileName);
        this.config.load(is);
    }
    public String get(String key) {
        return this.config.getProperty(key);
    }

}

