/*
 * Copyright 2020 phyzicsz <phyzics.z@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phyzicsz.reactor.benchmark;

import com.phyzicsz.reactor.benchmark.data.DataRecord;
import com.phyzicsz.reactor.benchmark.data.DataStore;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 *
 * @author phyzicsz <phyzics.z@gmail.com>
 */
public class ReactorBenchmark {

    Logger logger = LoggerFactory.getLogger(ReactorBenchmark.class);

    public void benchmark() throws IOException {
        DataStore data = new DataStore();
        data.open();

        int count = 0;

        File file = new File("reactor-benchmark.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        
        
        logger.info("starting benchmark");
        
        Instant start = Instant.now();
        FileWriter fw = new FileWriter(file);
        try (BufferedWriter writer = new BufferedWriter(fw)) {
            Flux.fromIterable(data)
//                    .log()
                    .map(new Processor(writer))
                    .subscribe();
        }
        
        Instant finish = Instant.now();
        long elapsedTime = Duration.between(start, finish).toMillis();
        logger.info("finished running benchmark: {} ms", elapsedTime);
    }
}
