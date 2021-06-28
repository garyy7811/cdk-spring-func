package example;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.data.Item;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication
public class FunctionConfiguration{
    public static void main( String[] args ){
        SpringApplication.run( FunctionConfiguration.class, args );
    }

    private static Log logger = LogFactory.getLog( FunctionConfiguration.class );

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
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> uppercase(){
        return request -> {

            if( HttpMethod.GET.equals( request.getHttpMethod() )
                    && request.getResource().equals( "/items/get/{itemId}" )
                    && request.getPathParameters().containsKey( "itemId" )
            ){
                String itemId = request.getPathParameters().get( "itemId" );
                try{
                    return new APIGatewayProxyResponseEvent().withBody( objectMapper.writeValueAsString( dynamoDBMapper.load( Item.class, itemId ) ) );
                }
                catch( JsonProcessingException e ){
                    logger.error( e );
                    return new APIGatewayProxyResponseEvent().withStatusCode( 404 );
                }
            }
            else if( HttpMethod.POST.equals( request.getHttpMethod() )
                    && request.getResource().equals( "/items/save" ) ){
                try{
                    Item item = objectMapper.readValue( request.getBody(), Item.class );

                    if( item.getItemId() != null ){
                        return new APIGatewayProxyResponseEvent().withStatusCode( 400 ).withBody( "new item can't have id" );
                    }

                    if( item.getSize() < 3 || item.getSize() > 999 ){
                        return new APIGatewayProxyResponseEvent().withStatusCode( 400 ).withBody( "item size should between[3-999]" );
                    }
                    if( item.getUserIds() == null || item.getUserIds().size() < 2 ){
                        return new APIGatewayProxyResponseEvent().withStatusCode( 400 ).withBody( "at least two users" );
                    }
                    if( item.getUserIds().size() > item.getSize() * item.getSize() ){
                        return new APIGatewayProxyResponseEvent().withStatusCode( 400 ).withBody( "too many users" );
                    }
                    item.setCreateTime( System.currentTimeMillis() );
                    item.setItemId( item.getUserIds().stream().collect( Collectors.joining( "," ) ) + item.getCreateTime() );
                    item.setLastUpdateTime( item.getCreateTime() );
                    dynamoDBMapper.save( item );
                    return new APIGatewayProxyResponseEvent().withStatusCode( 200 ).withBody( objectMapper.writeValueAsString( item ) );
                }
                catch( JsonProcessingException e ){
                    logger.error( e );
                    return new APIGatewayProxyResponseEvent().withStatusCode( 400 );
                }

            }
            else if( HttpMethod.POST.equals( request.getHttpMethod() )
                    && request.getResource().equals( "/muta/add" ) ){

                String itemId = null;
                String userId = null;
                int row = - 1;
                int col = - 1;
                try{
                    Map<String, String> adding = objectMapper.readValue( request.getBody(), Map.class );
                    itemId = adding.get( "itemId" );
                    userId = adding.get( "userId" );
                    row = Integer.parseInt( adding.get( "row" ) );
                    col = Integer.parseInt( adding.get( "col" ) );
                }
                catch( Exception e ){
                    e.printStackTrace();
                    return new APIGatewayProxyResponseEvent().withStatusCode( 400 ).withBody( "format error ..." );
                }

                Item item = dynamoDBMapper.load( Item.class, itemId );
                if( item.getWinnerUserId() != null ){
                    String body = null;
                    try{
                        body = objectMapper.writeValueAsString( item );
                    }
                    catch( JsonProcessingException e ){
                        e.printStackTrace();
                    }
                    return new APIGatewayProxyResponseEvent().withStatusCode( 400 ).withBody( body );
                }

                //decide turns
                if( item.getLastUpdateUserIndex() >= 0 ){//someone played
                    //next userId
                    int nextIdx = item.getLastUpdateUserIndex() == item.getSize() - 1 ? 0 : item.getLastUpdateUserIndex() + 1;
                    if( ! item.getUserIds().get( nextIdx ).equals( userId ) ){
                        return new APIGatewayProxyResponseEvent().withStatusCode( 400 ).withBody( "not your turn" );
                    }
                    item.setLastUpdateUserIndex( nextIdx );
                }
                else{
                    item.setLastUpdateUserIndex( item.getUserIds().indexOf( userId ) );
                }

                //someone are in that position
                if( item.getPositionToUserId().containsKey( userId + "," + row ) ){
                    String body = null;
                    try{
                        body = objectMapper.writeValueAsString( item );
                    }
                    catch( JsonProcessingException e ){
                        e.printStackTrace();
                    }
                    return new APIGatewayProxyResponseEvent().withStatusCode( 400 ).withBody( body );
                }

                item.getPositionToUserId().put( row + "," + col, userId );

                int count = 0;
                for( int r = row - 5; r <= row + 5; r++ ){
                    if( r >= 0 && r < item.getSize() ){
                        if( userId.equals( item.getPositionToUserId().get( r + "," + col ) ) ){
                            count++;
                            if( count >= 5 ){
                                item.setWinnerUserId( userId );
                                item.setWinningPosition( r + "," + col );
                            }
                        }
                        else{
                            count = 0;
                        }
                    }
                }

                if( item.getWinnerUserId() == null ){
                    count = 0;
                    for( int c = col - 5; c <= col + 5; c++ ){
                        if( c >= 0 && c < item.getSize() ){
                            if( userId.equals( item.getPositionToUserId().get( row + "," + c ) ) ){
                                count++;
                                if( count >= 5 ){
                                    item.setWinnerUserId( userId );
                                    item.setWinningPosition( row + "," + c );
                                }
                            }
                            else{
                                count = 0;
                            }
                        }
                    }
                }


                if( item.getWinnerUserId() == null ){
                    count = 0;
                    for( int c = col - 5, r = row - 5; c <= col + 5 && r <= row + 5; c++, r++ ){
                        if( c >= 0 && c < item.getSize() && r >= 0 && r < item.getSize() ){
                            if( userId.equals( item.getPositionToUserId().get( row + "," + c ) ) ){
                                count++;
                                if( count >= 5 ){
                                    item.setWinnerUserId( userId );
                                    item.setWinningPosition( r + "," + c );
                                }
                            }
                            else{
                                count = 0;
                            }
                        }
                    }
                }

                if( item.getWinnerUserId() == null ){
                    count = 0;
                    for( int c = col - 5, r = row + 5; c <= col + 5 && r >= row - 5; c++, r-- ){
                        if( c >= 0 && c < item.getSize() && r >= 0 && r < item.getSize() ){
                            if( userId.equals( item.getPositionToUserId().get( row + "," + c ) ) ){
                                count++;
                                if( count >= 5 ){
                                    item.setWinnerUserId( userId );
                                    item.setWinningPosition( r + "," + c );
                                }
                            }
                            else{
                                count = 0;
                            }
                        }
                    }
                }


                try{
                    dynamoDBMapper.save( item );
                    return new APIGatewayProxyResponseEvent().withStatusCode( 200 ).withBody( "please refresh" );
                }
                //out of date
                catch( ConditionalCheckFailedException e ){
                    return new APIGatewayProxyResponseEvent().withStatusCode( 426 ).withBody( "please refresh" );
                }

            }
            else{
                return new APIGatewayProxyResponseEvent().withStatusCode( 400 ).withBody( "unknown request" );
            }
        };
    }
}
