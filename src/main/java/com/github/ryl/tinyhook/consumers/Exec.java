package com.github.ryl.tinyhook.consumers;

import lombok.AllArgsConstructor;
import org.springframework.util.Assert;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@AllArgsConstructor
public class Exec implements Consumer<String>  {

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
            Process process = builder.start();
            process.waitFor(30, TimeUnit.SECONDS);
            int returnValue = process.exitValue();
            Assert.isTrue(0 == returnValue, "Expected a return value of 0, but was " + returnValue);
        } catch (Exception e) {
            throw new ProcessException(e);
        }
    }

}
