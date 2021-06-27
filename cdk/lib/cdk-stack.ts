import * as apigatewayv2 from '@aws-cdk/aws-apigatewayv2';
import * as apigatewayv2int from '@aws-cdk/aws-apigatewayv2-integrations';
import * as dynamodb from '@aws-cdk/aws-dynamodb';
import * as lambda from '@aws-cdk/aws-lambda';
import * as cdk from '@aws-cdk/core';
import {HttpMethod} from "@aws-cdk/aws-apigatewayv2";
import {LambdaProxyIntegration} from "@aws-cdk/aws-apigatewayv2-integrations";

export class CdkStack extends cdk.Stack {
    constructor(scope: cdk.App, id: string, props?: cdk.StackProps) {
        super(scope, id, props);


        const dynamoItems = new dynamodb.Table(this, 'items', {
            partitionKey: {
                name: 'itemId',
                type: dynamodb.AttributeType.STRING
            },
            sortKey: {
                name: 'createTime',
                type: dynamodb.AttributeType.NUMBER
            },
            tableName: 'gy-tst-items',
            removalPolicy: cdk.RemovalPolicy.DESTROY
        });


        const dynamoMuta = new dynamodb.Table(this, 'itemMuta', {
            partitionKey: {
                name: 'itemId',
                type: dynamodb.AttributeType.STRING
            },
            sortKey: {
                name: 'timestamp',
                type: dynamodb.AttributeType.STRING
            },
            tableName: 'gy-tst-itemMuta',
            removalPolicy: cdk.RemovalPolicy.DESTROY
        });


        const getOneLambda = new lambda.Function(this, 'getOneItemFunction', {
            code: new lambda.AssetCode('../sample-func/build/libs/sample-all-ns.r100-deploy.jar'),
            handler: 'org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest',
            runtime: lambda.Runtime.JAVA_11,
            environment: {
                // TABLE_NAME: "tst-dynamo",
                TABLE_NAME: dynamoItems.tableName,
                PRIMARY_KEY: 'itemId'
            },
            memorySize: 1024
        });

        dynamoItems.grantReadWriteData(getOneLambda);

        const httpApi = new apigatewayv2.HttpApi(this, 'itemsApi', {
            apiName: 'rest',
            createDefaultStage: true
        });

        httpApi.addRoutes({
            path: '/books/{proxy+}',
            methods: [HttpMethod.ANY],
            integration: new LambdaProxyIntegration({handler: getOneLambda}),
        });

    }
}
