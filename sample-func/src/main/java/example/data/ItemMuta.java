package example.data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;


@DynamoDBTable( tableName = "gy-tst-itemMuta")
public class ItemMuta{
    @DynamoDBHashKey
    private String itemId;

    @DynamoDBRangeKey
    private String timestamp;

    @DynamoDBAttribute
    private String userId;

    @DynamoDBAttribute
    private String position;

}
