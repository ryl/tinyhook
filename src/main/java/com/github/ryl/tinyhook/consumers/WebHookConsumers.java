package com.github.ryl.tinyhook.consumers;

import java.util.Arrays;

public class WebHookConsumers {

    public static Exec exec(String workdir, String... args) {
        return new Exec(workdir, Arrays.asList(args));
    }

}
