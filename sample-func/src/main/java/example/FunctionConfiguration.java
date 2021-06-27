package example;

import java.util.Map;
import java.util.function.Function;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import example.data.Item;
import example.data.ItemMuta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FunctionConfiguration{
    public static void main( String[] args ){
        SpringApplication.run( FunctionConfiguration.class, args );
    }


    @Bean
    public AmazonDynamoDB amazonDynamoDB(){
        AmazonDynamoDB amazonDynamoDB
                = AmazonDynamoDBClient.builder().build();

        return amazonDynamoDB;

    }

    @Autowired
    private DynamoDBMapper dynamoDBMapper;


    @Bean
    public DynamoDBMapper dynamoDBMapper( AmazonDynamoDB amazonDynamoDB ){
        return new DynamoDBMapper( amazonDynamoDB );
    }


    @Bean
    public Function<APIGatewayV2HTTPEvent, String> uppercase(){
        return value -> {
            System.out.println( "input>>>" + value );

            try{
                dynamoDBMapper.save( new Item() );
            }
            catch( Exception e ){
                e.printStackTrace();
            }

            try{
                dynamoDBMapper.load( Item.class, "asdf" );
            }
            catch( Exception e ){
                e.printStackTrace();
            }

            try{
                dynamoDBMapper.save( new ItemMuta() );
            }
            catch( Exception e ){
                e.printStackTrace();
            }

            try{
                dynamoDBMapper.query( Item.class, new DynamoDBQueryExpression<Item>().withRangeKeyConditions( Map.of( "", new Condition() ) ).withHashKeyValues(  new Item() ));
            }
            catch( Exception e ){
                e.printStackTrace();
            }


            return "ok,1234";
        };
    }
}
