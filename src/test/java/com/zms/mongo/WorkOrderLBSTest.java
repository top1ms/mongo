package com.zms.mongo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.*;
import com.mongodb.client.*;
import com.zms.mongo.model.WorkOrderPosDo;
import com.zms.mongo.utils.GeoGraphicalPointGenerateUtil;
import com.zms.mongo.utils.WorkOrderMockDataUtil;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.mongodb.MongoClient.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class WorkOrderLBSTest {

    @Test
    public void test() {
        JSONObject pos = new JSONObject();
        pos.put("lnt","116.28739245546471");
        pos.put("lat","40.05048608200999");
        WorkOrderPosDo workOrderPosDo = new WorkOrderPosDo();
        workOrderPosDo.setCityId(1);
        workOrderPosDo.setBizType(1);
        workOrderPosDo.setType(5);
        workOrderPosDo.setWoId(2131231312L);
//        workOrderPosDo.setCenterPos(pos);
        workOrderPosDo.setPriority(1);
        workOrderPosDo.setSecondType(5100);
        workOrderPosDo.setStatus(1);
        workOrderPosDo.setTargetId(100);
        workOrderPosDo.setTargetType("0##0");
        workOrderPosDo.setGmtCreate(new Date());
        workOrderPosDo.setGmtModify(new Date());
        workOrderPosDo.setLocateTime(new Date());
        System.out.println(JSONObject.toJSONString(workOrderPosDo));
    }

//    @Test
//    public void test01(){
//        String latStr = randomLat(30d, 40d);
//        double lat = Double.parseDouble(latStr);
//        String longStr = randomLnt(130d,140d);
//        double lnt = Double.parseDouble(longStr);
//        System.out.println(lnt);
//        System.out.println(lat);
//    }



    @Test
    public void testInsertMongo(){
        MongoCredential mongoCredential = MongoCredential
                .createCredential("root", "admin", "123456".toCharArray());
        String uri = "mongodb://127.0.0.1:27017";
        ConnectionString connectionString = new ConnectionString(uri);
//        MongoClientSettings mongoClientSettings = MongoClientSettings
//                                                .builder()
//                                                .applyConnectionString(connectionString)
//                                                .credential(mongoCredential)
//                                                .build();
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();

        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        ServerAddress serverAddress = new ServerAddress("127.0.0.1",27017);
        MongoCredential credential = MongoCredential.createCredential("zms","workorder_lbs","123456".toCharArray());
        MongoClientOptions options = MongoClientOptions.builder().description("zms").build();
        try (MongoClient mongoClient = new MongoClient(serverAddress,credential,options)) {
            MongoDatabase database = mongoClient.getDatabase("workorder_lbs");
//            MongoDatabase database = mongoClient.getDatabase("workorder_lbs").withCodecRegistry(pojoCodecRegistry);
//            MongoCollection<WorkOrderPosDo> collection = database.getCollection("work_order_pos",WorkOrderPosDo.class);
//            List<WorkOrderPosDo> workOrderPosDos = new ArrayList<>(1000);
//            for(int i=0;i<500 * 10000 ;i++){
//                WorkOrderPosDo mock = WorkOrderMockDataUtil.mock();
//                workOrderPosDos.add(mock);
//                if(workOrderPosDos.size()==500){
//                    collection.insertMany(workOrderPosDos);
//                    workOrderPosDos.clear();
//                }
//            }
//            collection.insertOne(WorkOrderMockDataUtil.mock());
//            Bson projectionFields = Projections.fields(
//                    Projections.include("woId", "centerPos"),
//                    Projections.excludeId());
//            WorkOrderPosDo cityId = collection.find(eq("cityId", 1))
//                    .projection(projectionFields)
//                    .first();
//            System.out.println(cityId);
            MongoCollection<Document> collection = database.getCollection("work_order_pos");
            int[] status = new int[]{1,2,3,7};
            List<List<Double>> polygons = new ArrayList<>();
            List<Double> point1 = Arrays.asList(130.397444479002,36.5135389626648);
            List<Double> point2 = Arrays.asList(136.0670195208191,36.5846784941106);
            List<Double> point3 = Arrays.asList(133.72653749064017,30.75835718845756);
            polygons.add(point1);
            polygons.add(point2);
            polygons.add(point3);

            Document subMatch = new Document();
            subMatch.append("woId",100000401);
            subMatch.append("status",new BasicDBObject("$in",Arrays.asList(1,2,3,4)));
            subMatch.append("centerPos",new BasicDBObject("$geoWithin",new BasicDBObject("$polygon",polygons)));
            Document match = new Document("$match", subMatch);



            Document sub_group = new Document();
            sub_group.append("_id",new Document().append("workOrderType","$type"));
            sub_group.append("count",new Document().append("$sum",1));

            Document group = new Document();
            group.append("$group",sub_group);

            AggregateIterable<Document> aggregate = collection.aggregate(Arrays.asList(match,group));
            MongoCursor<Document> iterator = aggregate.iterator();
            while (iterator.hasNext()){
                Document next = iterator.next();
                System.out.println(JSONObject.toJSONString(next));
                }


        }
    }


    @Test
    public void test02(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<30;i++){
            List<Double> doubles = GeoGraphicalPointGenerateUtil.generatePoint();
            sb.append(Arrays.toString(doubles.toArray())).append(",");
        }
        System.out.println(sb.toString());
    }



    @Test
    public void test03(){
        process(mongoCollection->{
            List<List<Double>> polygons = new ArrayList<>();
            List<Double> point1 = Arrays.asList(130.397444479002,36.5135389626648);
            List<Double> point2 = Arrays.asList(136.0670195208191,36.5846784941106);
            List<Double> point3 = Arrays.asList(133.72653749064017,30.75835718845756);
            polygons.add(point1);
            polygons.add(point2);
            polygons.add(point3);
            Document subMatch = new Document();
            subMatch.append("bizType",1);
            subMatch.append("status",new BasicDBObject("$in",Arrays.asList(1,2,3,4)));
            subMatch.append("woId",100000401);
            subMatch.append("centerPos",new BasicDBObject("$geoWithin",new BasicDBObject("$polygon",polygons)));
            Document match = new Document("$match", subMatch);
            match.append("$match",subMatch);

            Document subGroup = new Document();
            subGroup.append("_id",new BasicDBObject("type","$type").append("priority","$priority"));
            subGroup.append("count",new BasicDBObject("$sum",1));
            Document group = new Document();
            group.append("$group",subGroup);

            AggregateIterable<Document> aggregate = mongoCollection.aggregate(Arrays.asList(match, group));
            MongoCursor<Document> iterator = aggregate.iterator();
            while (iterator.hasNext()){
                Document next = iterator.next();
                Document left = (Document) next.get("_id");
                left.getInteger("type");

                JSONObject jsonObject = JSON.parseObject(next.getString("_id"));
                int type = jsonObject.getIntValue("type");
                int priority = jsonObject.getIntValue("priority");
                System.out.println(type);
                System.out.println(priority);
                System.out.println(JSONObject.toJSONString(next));
            }


        });
    }

    private void process(Consumer<MongoCollection<Document>> consumer) {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        ServerAddress serverAddress = new ServerAddress("127.0.0.1", 27017);
        MongoCredential credential = MongoCredential.createCredential("zms", "workorder_lbs", "123456".toCharArray());
        MongoClientOptions options = MongoClientOptions.builder().description("zms").build();
        try (MongoClient mongoClient = new MongoClient(serverAddress, credential, options)) {
            MongoCollection<Document> mongoCollection = mongoClient
                    .getDatabase("workorder_lbs")
                    .getCollection("work_order_pos_1");
            consumer.accept(mongoCollection);
        }
    }

    private void process2(Consumer<MongoCollection<WorkOrderPosDo>> consumer) {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        ServerAddress serverAddress = new ServerAddress("127.0.0.1", 27017);
        MongoCredential credential = MongoCredential.createCredential("zms", "workorder_lbs", "123456".toCharArray());
        MongoClientOptions options = MongoClientOptions.builder().description("zms").build();
        try (MongoClient mongoClient = new MongoClient(serverAddress, credential, options)) {
            MongoCollection<WorkOrderPosDo> mongoCollection = mongoClient
                    .getDatabase("workorder_lbs")
                    .withCodecRegistry(pojoCodecRegistry)
                    .getCollection("work_order_pos_1",WorkOrderPosDo.class);
            consumer.accept(mongoCollection);
        }
    }

    @Test
    public void test05 (){
        process2(mongoCollection->{
            List<WorkOrderPosDo> workOrderPosDos = new ArrayList<>(500);
            for(int i=0;i< 50 * 10000 ;i++){
                WorkOrderPosDo mock = WorkOrderMockDataUtil.mock();
                workOrderPosDos.add(mock);
                if(workOrderPosDos.size()==500){
                    mongoCollection.insertMany(workOrderPosDos);
                    workOrderPosDos.clear();
                }
            }
        });
    }

}
