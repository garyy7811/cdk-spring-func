package nine8p6;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.FunctionalSpringApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
public class Tmp96App{

    public static void main( String[] args ){
        FunctionalSpringApplication.run( Tmp96App.class, args );
    }


    @Bean
    public Function<String, String> members(){
        return input -> {
            System.out.println(  input );
            return input;
        };
    }
    @Bean
    public Consumer<String> post(){
        return input -> {
            System.out.println(  input );
        };
    }

    @Bean
    public Supplier<String> get(){
        return () -> {
            return "gettt";
        };
    }

}
