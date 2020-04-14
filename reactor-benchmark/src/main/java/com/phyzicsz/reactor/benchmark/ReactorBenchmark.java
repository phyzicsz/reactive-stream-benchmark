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
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
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
        
        List<Integer> iterable = Arrays.asList(1, 2, 3, 4);
        Flux.fromIterable(data)
                .log()
                .map(new Processor())
                .subscribe(new Subscriber<DataRecord>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }
                    
                    @Override
                    public void onNext(DataRecord integer) {
                        
                    }
                    
                    @Override
                    public void onError(Throwable t) {
                    }
                    
                    @Override
                    public void onComplete() {
                    }

            
                });
    }   
}
