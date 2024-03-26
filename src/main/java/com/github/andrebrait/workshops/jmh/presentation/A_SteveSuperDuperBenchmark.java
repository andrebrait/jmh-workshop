package com.github.andrebrait.workshops.jmh.presentation;

import static com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework.*;

/**
 * Steve's Super Duper Benchmark
 *
 * <p>Mine has a cool ASCII car. It's obviously the fastest and bestest!
 * //@formatter:off
 *                       ___..............._
 *              __.. ' _'.""""""\\""""""""- .`-._
 *  ______.-'         (_) |      \\           ` \\`-. _
 * /_       --------------'-------\\---....______\\__`.`  -..___
 * | T      _.----._           Xxx|x...           |          _.._`--. _
 * | |    .' ..--.. `.         XXX|XXXXXXXXXxx==  |       .'.---..`.     -._
 * \_j   /  /  __  \  \        XXX|XXXXXXXXXXX==  |      / /  __  \ \        `-.
 *  _|  |  |  /  \  |  |       XXX|""'            |     / |  /  \  | |          |
 * |__\_j  |  \__/  |  L__________|_______________|_____j |  \__/  | L__________J
 *      `'\ \      / ./__________________________________\ \      / /___________\
 *         `.`----'.'   dp                                `.`----'.'
 *           `""""'                                         `""""'
 * //@formatter:on
 */
public final class A_SteveSuperDuperBenchmark {

    public static void main(String[] args) {
        //SystemInfoUtils.printSystemInfo();
        bench("steve", RUN_MILLIS, LOOP, WARMUP, REPEAT, Solutions::steve);
        bench("joe", RUN_MILLIS, LOOP, WARMUP, REPEAT, Solutions::joe);
        bench("bob", RUN_MILLIS, LOOP, WARMUP, REPEAT, Solutions::bob);
    }
}
