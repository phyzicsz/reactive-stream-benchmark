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
import com.phyzicsz.reactor.benchmark.data.KryoManager;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.function.Function;
import org.slf4j.LoggerFactory;

/**
 *
 * @author phyzicsz <phyzics.z@gmail.com>
 */
public class Processor implements Function<Entry<byte[],byte[]>,DataRecord>{

    org.slf4j.Logger logger = LoggerFactory.getLogger(Processor.class);
    
    @Override
    public DataRecord apply(Entry<byte[],byte[]> entry) {
        byte[] msg = entry.getValue();
        DataRecord record = null;
        try {
            record =  KryoManager.deserialize(msg);
            logger.info("record: {}", record.getRecord());
        } catch (IOException ex) {
           logger.error("error processing record",ex);
        }
       return record;
    }
    
}
