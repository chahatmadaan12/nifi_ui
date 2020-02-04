package com.applicate.nifiui.configuration.parser;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.applicate.utils.FileUtils;

public abstract class AbstractParser<T> implements Parser<T> {

	private Logger log = LoggerFactory.getLogger(AbstractParser.class);
	
    private final String parserName = getClass().getSimpleName();

    private File source;

    private File destination;

    protected void setSource(File source) {
        this.source = source;
    }

    protected void setSource(String source) {
        this.source = new File(source);
    }

    protected File getDestination() {
        File file;
        if (this.destination != null) {
            file = this.destination;
        } else {
            file = this.source;
        }
        return file;
    }

    private String createLoggerMessage(String message) {
        return  message;
    }

    protected File getSource() {
        return this.source;
    }

    @Override
    public Parser<T> parse() throws Exception {
        String content = "";
        if(source.exists()) {
            try {
                content = FileUtils.readOrFail(source);
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        } else {
           log.error(createLoggerMessage( "File '<{}>' is not present setting empty data"),source);
        }
        convertToSpecificFormat(content);
        return this;
    }

    protected abstract void convertToSpecificFormat(String content) throws Exception;

    protected abstract String getContent() throws Exception;

    @Override
    public String toString() {
        return "Type : " + this.getClass().getSimpleName() + " Source : " + this.source + " destination : " + this.destination;
    }

}
