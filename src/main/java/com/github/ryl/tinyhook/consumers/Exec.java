package com.github.ryl.tinyhook.consumers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@AllArgsConstructor
public class Exec implements Consumer<String>  {

    private static final Logger logger = LoggerFactory.getLogger(Exec.class);

    private final String workdirPath;

    private final List<String> args;

    @Override
    public void accept(String s) {
        var builder = new ProcessBuilder(args);
        var workdir = new File(workdirPath);

        if (!workdir.exists())
            throw new IllegalArgumentException(workdir + " does not exist.");
        if (!workdir.isDirectory())
            throw new IllegalArgumentException(workdir + " is not a directory.");

        builder.directory(workdir);

        try {
            logger.info("Executing {}", String.join(" ", args));
            builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            Process process = builder.start();
            // TODO: Make this configurable.
            process.wait();
            int returnValue = process.exitValue();
            logger.info("Process completed with status code {}", returnValue);
            Assert.isTrue(0 == returnValue, "Expected a return value of 0, but was " + returnValue);
        } catch (Exception e) {
            throw new ProcessException(e);
        }
    }

}
