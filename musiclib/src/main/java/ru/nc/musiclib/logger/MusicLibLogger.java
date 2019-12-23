package ru.nc.musiclib.logger;
/**
 * Адаптер над логером
 *  при смене баблиотеки логирования меняем только этот файл
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MusicLibLogger {
    private Logger logger;

    public MusicLibLogger(Class<?> clazz) {
        logger = LogManager.getLogger(clazz);
    }

    public void info(String var1){
        logger.info(var1);
    }
    public void error(String var1){
        logger.error(var1);
    }
    public void debug(String var1){
        logger.debug(var1);
    }
    public void fatal(String var1){
        logger.fatal(var1);
    }
}
