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
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author phyzicsz <phyzics.z@gmail.com>
 */
public class KryoManager {

    private static final Kryo kryo = new Kryo();
    
    static{
         kryo.register(DataRecord.class);
    }
    
    public static byte[] serialize(final DataRecord record) throws IOException {
        byte[] bytes;
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            Output output = new Output(stream);
            kryo.writeObject(output, record);
            bytes = output.toBytes();
        }
        return bytes;
    }

    public static DataRecord deserialize(final byte[] bytes) throws IOException {
        DataRecord record;
        try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes)) {
            Input input = new Input(stream);
            record = kryo.readObject(input, DataRecord.class);
        }
        
        return record;
    }

}
