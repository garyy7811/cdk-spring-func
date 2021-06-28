package example.data;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.util.List;
import java.util.Map;

@DynamoDBTable( tableName = "gy-tst-items" )
public class Item{

    @DynamoDBHashKey
    private String itemId;

    @DynamoDBRangeKey
    private long createTime;

    @DynamoDBAttribute
    private long size;

    @DynamoDBAttribute
    private String winnerUserId;
    @DynamoDBAttribute
    private String winningPosition;


    @DynamoDBVersionAttribute
    private long version;

    @DynamoDBAttribute
    private long lastUpdateTime;
    @DynamoDBAttribute
    private int lastUpdateUserIndex = -1;

    @DynamoDBAttribute
    private List<String> userIds;

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

    public long getSize(){
        return size;
    }

    public void setSize( long size ){
        this.size = size;
    }

    public String getWinnerUserId(){
        return winnerUserId;
    }

    public void setWinnerUserId( String winnerUserId ){
        this.winnerUserId = winnerUserId;
    }

    public String getWinningPosition(){
        return winningPosition;
    }

    public void setWinningPosition( String winningPosition ){
        this.winningPosition = winningPosition;
    }

    public long getVersion(){
        return version;
    }

    public void setVersion( long version ){
        this.version = version;
    }

    public long getLastUpdateTime(){
        return lastUpdateTime;
    }

    public void setLastUpdateTime( long lastUpdateTime ){
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getLastUpdateUserIndex(){
        return lastUpdateUserIndex;
    }

    public void setLastUpdateUserIndex( int lastUpdateUserIndex ){
        this.lastUpdateUserIndex = lastUpdateUserIndex;
    }

    public List<String> getUserIds(){
        return userIds;
    }

    public void setUserIds( List<String> userIds ){
        this.userIds = userIds;
    }

    public Map<String, String> getPositionToUserId(){
        return positionToUserId;
    }

    public void setPositionToUserId( Map<String, String> positionToUserId ){
        this.positionToUserId = positionToUserId;
    }
}
