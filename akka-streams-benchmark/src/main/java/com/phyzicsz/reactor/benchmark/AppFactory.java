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

import com.phyzicsz.reactor.benchmark.data.DataStore;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author phyzicsz <phyzics.z@gmail.com>
 */
public class AppFactory {
    
    public AppFactory(){
        
    }
    
    public AppFactory stageData() throws IOException{
        new DataStore()
                .load(1000000);
        return this;
    }
    
    public AppFactory runBenchmark() throws IOException, InterruptedException, ExecutionException, TimeoutException{
        new AkkaStreamsBenchmark()
                .hotspotWarmup()
                .hotspotWarmup()
                .hotspotWarmup()
                .benchmark();
        return this;
    }
}
