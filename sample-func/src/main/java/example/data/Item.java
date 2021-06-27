package example.data;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.util.Map;
import java.util.Set;

@DynamoDBTable( tableName = "gy-tst-items" )
public class Item{

    @DynamoDBHashKey
    private String itemId;
    @DynamoDBRangeKey
    private long createTime;

    @DynamoDBAttribute
    private long size;

    @DynamoDBVersionAttribute
    private long lastUpdateTime;

    @DynamoDBAttribute
    private Set<String> userIds;

    @DynamoDBAttribute
    private Map<String, String> positionToUserId;


    public String getItemId(){
        return itemId;
    }

    public void setItemId( String itemId ){
        this.itemId = itemId;
    }

    public long getCreateTime(){
        return createTime;
    }

    public void setCreateTime( long createTime ){
        this.createTime = createTime;
    }

    public long getLastUpdateTime(){
        return lastUpdateTime;
    }

    public void setLastUpdateTime( long lastUpdateTime ){
        this.lastUpdateTime = lastUpdateTime;
    }

    public Set<String> getUserIds(){
        return userIds;
    }

    public void setUserIds( Set<String> userIds ){
        this.userIds = userIds;
    }

    public Map<String, String> getPositionToUserId(){
        return positionToUserId;
    }

    public void setPositionToUserId( Map<String, String> positionToUserId ){
        this.positionToUserId = positionToUserId;
    }

    public long getSize(){
        return size;
    }

    public void setSize( long size ){
        this.size = size;
    }
}
