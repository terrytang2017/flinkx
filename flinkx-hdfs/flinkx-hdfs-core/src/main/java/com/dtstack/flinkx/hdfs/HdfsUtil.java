/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dtstack.flinkx.hdfs;

import com.dtstack.flinkx.common.ColumnType;
import com.dtstack.flinkx.util.DateUtil;
import org.apache.flink.util.Preconditions;
import org.apache.hadoop.hive.serde2.io.DateWritable;
import org.apache.hadoop.hive.serde2.io.HiveDecimalWritable;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.io.*;
import static com.dtstack.flinkx.common.ColumnType.*;

/**
 * Utilities for HdfsReader and HdfsWriter
 *
 * Company: www.dtstack.com
 * @author huyifan.zju@163.com
 */
public class HdfsUtil {

    public static Object string2col(String str, String type) {

        Preconditions.checkNotNull(type);
        ColumnType columnType = valueOf(type.toUpperCase());
        Object ret;
        switch(columnType) {
            case TINYINT:
                ret = Byte.valueOf(str);
                break;
            case SMALLINT:
                ret = Short.valueOf(str);
                break;
            case INT:
                ret = Integer.valueOf(str);
                break;
            case BIGINT:
                ret = Long.valueOf(str);
                break;
            case FLOAT:
                ret = Float.valueOf(str);
                break;
            case DOUBLE:
                ret = Double.valueOf(str);
                break;
            case STRING:
            case VARCHAR:
            case CHAR:
                ret = str;
                break;
            case BOOLEAN:
                ret = Boolean.valueOf(str.toLowerCase());
                break;
            case DATE:
                ret = DateUtil.columnToDate(str);
                break;
            case TIMESTAMP:
                ret = DateUtil.columnToTimestamp(str);
                break;
            default:
                throw new IllegalArgumentException();
        }

        return ret;
    }

    public static Object getWritableValue(Object writable) {
        Class<?> clz = writable.getClass();
        Object ret = null;

        if(clz == IntWritable.class) {
            ret = ((IntWritable) writable).get();
        } else if(clz == Text.class) {
            ret = ((Text) writable).toString();
        } else if(clz == LongWritable.class) {
            ret = ((LongWritable) writable).get();
        } else if(clz == ByteWritable.class) {
            ret = ((ByteWritable) writable).get();
        } else if(clz == DateWritable.class) {
            ret = ((DateWritable) writable).get();
        } else if(writable instanceof Writable) {
            ret = writable.toString();
        }

        return ret;
    }

    public static ObjectInspector columnTypeToObjectInspetor(ColumnType columnType) {
        ObjectInspector objectInspector = null;
        switch(columnType) {
            case TINYINT:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(Byte.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case SMALLINT:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(Short.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case INT:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(Integer.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case BIGINT:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(Long.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case FLOAT:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(Float.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case DOUBLE:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(Double.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case DECIMAL:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(HiveDecimalWritable.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case TIMESTAMP:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(java.sql.Timestamp.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case DATE:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(java.sql.Date.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case STRING:
            case VARCHAR:
            case CHAR:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(String.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case BOOLEAN:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(Boolean.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            default:
                throw new IllegalArgumentException("You should not be here");
        }
        return objectInspector;
    }

}
