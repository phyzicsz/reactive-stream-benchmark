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

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.Map.Entry;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author phyzicsz <phyzics.z@gmail.com>
 */
public class DataStore implements Iterable<Entry<byte[], byte[]>> {

    private static final Logger logger = LoggerFactory.getLogger(DataStore.class);
    private DB db;
    DBIterator iter;

    private final String DB_KEY = "DB_Key";

    public DataStore() {

    }

    public void load(int testSize) throws IOException {

        Instant start = Instant.now();

        DB dblocal;
        Options options = new Options();
        dblocal = factory.open(new File("levelDBStore"), options);

        for (Integer i = 0; i < testSize; i++) {
            DataRecord record = new DataRecord();
            record.setCreationTime(Instant.now().toEpochMilli());
            record.setRecord(1);
            record.setMessage("ping");

            byte[] bytes = KryoManager.serialize(record);
            dblocal.put(ByteBuffer.allocate(4).putInt(i).array(), bytes);
        }

        dblocal.close();

        Instant finish = Instant.now();
        long elapsedTime = Duration.between(start, finish).toMillis();
        logger.info("finished loading data store: {}records, {} ms", testSize, elapsedTime);
    }

    public DataStore open() throws IOException {
        db = factory.open(new File("levelDBStore"), new Options());
        iter = db.iterator();
        
        return this;
    }
    
    public void close() throws IOException{
        if(iter != null){
            iter.close();
        }
        if(db!=null){
            db.close();
        }
    }

    @Override
    public Iterator<Entry<byte[], byte[]>> iterator() {
        return iter;
    }
}
