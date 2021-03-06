package Model;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import net.sf.json.JSONObject;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MongoDAO {

    /**
     * 查询所有内容
     * @param colName collection名
     * @return
     */
    public static MongoCursor<DBObject> FindAll(ColName colName){
        //连接mongoDB数据库，获取collection
        MongoDatabase database = MongoDBJDBC.Initial();
        MongoCollection<DBObject> collection = database.getCollection(colName.toString(), DBObject.class);

        return collection.find().iterator();
    }

    /**
     * 简单条件查询一个collection
     * @param colName collection名
     * @param query 条件BSON
     * @return
     */
    public static MongoCursor<DBObject> FindByBosn(ColName colName, String query){
        //连接mongoDB数据库，获取collection
        MongoDatabase database = MongoDBJDBC.Initial();
        MongoCollection<DBObject> collection = database.getCollection(colName.toString(), DBObject.class);

        BasicDBObject queryObject = BasicDBObject.parse(query);
        return collection.find(queryObject).iterator();
    }

    /**
     * 简单条件查询一个collection ,返回指定字段
     * @param colName collection名
     * @param query 条件BSON
     * @param projection 指定字段BSON
     * @return
     */
    public static MongoCursor<DBObject> FindByBosn(ColName colName, String query, Object []projection){
        //连接mongoDB数据库，获取collection
        MongoDatabase database = MongoDBJDBC.Initial();
        MongoCollection<DBObject> collection = database.getCollection(colName.toString(), DBObject.class);

        BasicDBObject queryObject = BasicDBObject.parse(query);
        JSONObject obj = new JSONObject();
        for(Object i : projection) {
            obj.put(i,"1");
        }

        BasicDBObject projectionObject = BasicDBObject.parse(obj.toString());
        return collection.find(queryObject).projection(projectionObject).iterator();
    }

    /**
     *更新数据
     * @param colName
     * @param query
     * @return
     */
    public static UpdateResult Update(ColName colName, String query, String update){
        //连接mongoDB数据库，获取collection
        MongoDatabase database = MongoDBJDBC.Initial();
        MongoCollection<DBObject> collection = database.getCollection(colName.toString(), DBObject.class);

        BasicDBObject queryObject = BasicDBObject.parse(query);
        BasicDBObject updateObject = BasicDBObject.parse(update);


        return collection.updateMany(queryObject,updateObject);
    }


    /**
     * 向集合插入一个文档
     * @param colName
     * @param query
     * @return
     */
    public static  void InsertOne(ColName colName,String query){
        //连接mongoDB数据库，获取collection
        MongoDatabase database = MongoDBJDBC.Initial();
        MongoCollection<DBObject> collection = database.getCollection(colName.toString(), DBObject.class);
        //插入文档
        BasicDBObject queryObject = BasicDBObject.parse(query);
        collection.insertOne(queryObject);
    }


    /**
     * 向集合插入多个文档
     * @param colName
     * @param querys
     * @return
     */
    public static  void InsertMany(ColName colName, ArrayList<String> querys){
        //连接mongoDB数据库，获取collection
        MongoDatabase database = MongoDBJDBC.Initial();
        MongoCollection<DBObject> collection = database.getCollection(colName.toString(), DBObject.class);
        //插入文档

        LinkedList<DBObject> linkedList = new LinkedList<>();
        for(int i =0;i<querys.size();i++){
            BasicDBObject queryObject = BasicDBObject.parse(querys.get(i));
            linkedList.add(queryObject);
        }

        collection.insertMany(linkedList);
    }

    /**
     * 聚合操作
     * @param colName
     * @param list
     * @return
     */
    public static AggregateIterable<DBObject> Aggregate(ColName colName, List<Bson> list){
        //连接mongoDB数据库，获取collection
        MongoDatabase database = MongoDBJDBC.Initial();
        MongoCollection<DBObject> collection = database.getCollection(colName.toString(), DBObject.class);

        return collection.aggregate(list);
    }
}
