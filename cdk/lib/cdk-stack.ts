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

        const dynamoTable = new dynamodb.Table(this, 'items', {
            partitionKey: {
                name: 'itemId',
                type: dynamodb.AttributeType.STRING
            },
            tableName: 'items',
            removalPolicy: cdk.RemovalPolicy.DESTROY, // NOT recommended for production code
        });

        const getOneLambda = new lambda.Function(this, 'getOneItemFunction', {
            code: new lambda.AssetCode('../sample-func/build/libs/sample-func-2.0.0.RELEASE-aws.jar'),
            handler: 'org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest',
            runtime: lambda.Runtime.JAVA_11,
            environment: {
                TABLE_NAME: dynamoTable.tableName,
                PRIMARY_KEY: 'itemId'
            }
        });

        dynamoTable.grantReadWriteData(getOneLambda);

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
