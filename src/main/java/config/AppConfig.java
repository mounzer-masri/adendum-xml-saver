package config;

import gev.xml.processor.GevXmlProcessorAbstract;
import gev.xml.processor.manual.ManualGevXmlProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Imona Andoid on 24.11.2017.
 */
@Configuration
public class AppConfig {

    @Bean(name="GevXmlProcessor")
    public GevXmlProcessorAbstract gevXmlProcessor() {
        return new ManualGevXmlProcessor();
    }
}
