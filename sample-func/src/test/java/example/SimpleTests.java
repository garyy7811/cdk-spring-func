
package example;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import example.data.Item;
import example.data.ItemMuta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest( classes = FunctionConfiguration.class )
@WebAppConfiguration
@TestInstance( TestInstance.Lifecycle.PER_CLASS )
@ActiveProfiles( "local" )
@TestPropertySource( properties = {
        "amazon.dynamodb.endpoint=http://localhost:8000/",
        "amazon.aws.accesskey=test1",
        "amazon.aws.secretkey=test231" } )
public class SimpleTests{


    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;


    @BeforeAll
    public void setup() throws Exception{

        CreateTableRequest tableRequest = dynamoDBMapper
                .generateCreateTableRequest( Item.class );
        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput( 1L, 1L ) );
        amazonDynamoDB.createTable( tableRequest );


        tableRequest = dynamoDBMapper
                .generateCreateTableRequest( ItemMuta.class );
        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput( 1L, 1L ) );
        amazonDynamoDB.createTable( tableRequest );

        //...

//        dynamoDBMapper.batchDelete(  );
//        dynamoDBMapper.batchDelete( itemMutaDAO.findAll() );
    }

    @Test
    public void givenItemWithExpectedCost_whenRunFindAll_thenItemIsFound(){

        System.out.println( "" );
    }
}
