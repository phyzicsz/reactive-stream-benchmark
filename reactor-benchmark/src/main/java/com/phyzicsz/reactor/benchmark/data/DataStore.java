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
package com.phyzicsz.reactor.benchmark.data;

import com.esotericsoftware.kryo.Kryo;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author phyzicsz <phyzics.z@gmail.com>
 */
public class DataStore {

     Logger logger = LoggerFactory.getLogger(DataStore.class);

    private final Kryo kryo = new Kryo();
    private final String DB_KEY = "DB_Key";

    public DataStore() {
        kryo.register(DataRecord.class);
    }

    public void load(int testSize) throws IOException {

        Instant start = Instant.now();
        
        DB db;
        Options options = new Options();
        db = factory.open(new File("levelDBStore"), options);

        for (int i = 0; i < testSize; i++) {
            DataRecord record = new DataRecord();
            record.setCreationTime(Instant.now().toEpochMilli());
            record.setRecord(1);
            record.setMessage("ping");

            byte[] bytes = KryoManager.serialize(kryo, record);
            db.put(DB_KEY.getBytes(), bytes);
        }
        
        db.close();
        
        Instant finish = Instant.now();
        long elapsedTime = Duration.between(start, finish).toMillis();
        logger.info("finished loading data store: {} ms", elapsedTime);

    }
}
