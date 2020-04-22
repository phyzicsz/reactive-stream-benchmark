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

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.japi.function.Function;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.phyzicsz.reactor.benchmark.data.DataRecord;
import com.phyzicsz.reactor.benchmark.data.DataStore;
import com.phyzicsz.reactor.benchmark.data.KryoManager;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author phyzicsz <phyzics.z@gmail.com>
 */
public class AkkaStreamsBenchmark {

    Logger logger = LoggerFactory.getLogger(AkkaStreamsBenchmark.class);
    final ActorSystem system;

    public AkkaStreamsBenchmark() {
        system = ActorSystem.create("akka-streams-benchmark");
    }

    public AkkaStreamsBenchmark hotspotWarmup() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        DataStore data = new DataStore();
        data.open();

        logger.info("running hotspot warmup benchmark");

        Instant start = Instant.now();

        Processor processor = new Processor(null,false);
        Source<Map.Entry<byte[], byte[]>, NotUsed> source = Source.fromIterator(() -> data.iterator());
        final CompletionStage<Done> future = source
                .map(entry -> processor.apply(entry))
                .runWith(Sink.ignore(), system);

        Done done = future.toCompletableFuture().get(5, TimeUnit.SECONDS);
        
        Instant finish = Instant.now();
        long elapsedTime = Duration.between(start, finish).toMillis();
        logger.info("finished running benchmark: {} ms", elapsedTime);
        data.close();
        return this;
    }

    public AkkaStreamsBenchmark benchmark() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        DataStore data = new DataStore();
        data.open();

        File file = new File("akka-streams-benchmark.txt");
        if (!file.exists()) {
            file.createNewFile();
        }

        logger.info("starting benchmark");

        Instant start = Instant.now();
        FileWriter fw = new FileWriter(file);
        Iterator iter = data.iterator();


        BufferedWriter writer = new BufferedWriter(fw);
        
        Processor processor = new Processor(writer, true);
        Source<Map.Entry<byte[], byte[]>, NotUsed> source = Source.fromIterator(() -> data.iterator());
        final CompletionStage<Done> future = source
                .map(entry -> processor.apply(entry))
                .runWith(Sink.ignore(), system);
        
        Done done = future.toCompletableFuture().get(5, TimeUnit.SECONDS);
        
        writer.close();
        Instant finish = Instant.now();
        long elapsedTime = Duration.between(start, finish).toMillis();
        logger.info("finished running benchmark: {} ms", elapsedTime);
        data.close();
        return this;
    }

}
