"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.CdkStack = void 0;
const apigatewayv2 = require("@aws-cdk/aws-apigatewayv2");
const dynamodb = require("@aws-cdk/aws-dynamodb");
const lambda = require("@aws-cdk/aws-lambda");
const cdk = require("@aws-cdk/core");
const aws_apigatewayv2_1 = require("@aws-cdk/aws-apigatewayv2");
const aws_apigatewayv2_integrations_1 = require("@aws-cdk/aws-apigatewayv2-integrations");
class CdkStack extends cdk.Stack {
    constructor(scope, id, props) {
        super(scope, id, props);
        const dynamoTable = new dynamodb.Table(this, 'items', {
            partitionKey: {
                name: 'itemId',
                type: dynamodb.AttributeType.STRING
            },
            tableName: 'items',
            removalPolicy: cdk.RemovalPolicy.DESTROY,
        });
        const getOneLambda = new lambda.Function(this, 'getOneItemFunction', {
            code: new lambda.AssetCode('../nineeight/build/libs'),
            handler: 'get-one.handler',
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
            path: '/books',
            methods: [aws_apigatewayv2_1.HttpMethod.ANY],
            integration: new aws_apigatewayv2_integrations_1.LambdaProxyIntegration({ handler: getOneLambda }),
        });
    }
}
exports.CdkStack = CdkStack;
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiY2RrLXN0YWNrLmpzIiwic291cmNlUm9vdCI6IiIsInNvdXJjZXMiOlsiY2RrLXN0YWNrLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7OztBQUFBLDBEQUEwRDtBQUUxRCxrREFBa0Q7QUFDbEQsOENBQThDO0FBQzlDLHFDQUFxQztBQUNyQyxnRUFBcUQ7QUFDckQsMEZBQThFO0FBRTlFLE1BQWEsUUFBUyxTQUFRLEdBQUcsQ0FBQyxLQUFLO0lBQ25DLFlBQVksS0FBYyxFQUFFLEVBQVUsRUFBRSxLQUFzQjtRQUMxRCxLQUFLLENBQUMsS0FBSyxFQUFFLEVBQUUsRUFBRSxLQUFLLENBQUMsQ0FBQztRQUV4QixNQUFNLFdBQVcsR0FBRyxJQUFJLFFBQVEsQ0FBQyxLQUFLLENBQUMsSUFBSSxFQUFFLE9BQU8sRUFBRTtZQUNsRCxZQUFZLEVBQUU7Z0JBQ1YsSUFBSSxFQUFFLFFBQVE7Z0JBQ2QsSUFBSSxFQUFFLFFBQVEsQ0FBQyxhQUFhLENBQUMsTUFBTTthQUN0QztZQUNELFNBQVMsRUFBRSxPQUFPO1lBQ2xCLGFBQWEsRUFBRSxHQUFHLENBQUMsYUFBYSxDQUFDLE9BQU87U0FDM0MsQ0FBQyxDQUFDO1FBRUgsTUFBTSxZQUFZLEdBQUcsSUFBSSxNQUFNLENBQUMsUUFBUSxDQUFDLElBQUksRUFBRSxvQkFBb0IsRUFBRTtZQUNqRSxJQUFJLEVBQUUsSUFBSSxNQUFNLENBQUMsU0FBUyxDQUFDLHlCQUF5QixDQUFDO1lBQ3JELE9BQU8sRUFBRSxpQkFBaUI7WUFDMUIsT0FBTyxFQUFFLE1BQU0sQ0FBQyxPQUFPLENBQUMsT0FBTztZQUMvQixXQUFXLEVBQUU7Z0JBQ1QsVUFBVSxFQUFFLFdBQVcsQ0FBQyxTQUFTO2dCQUNqQyxXQUFXLEVBQUUsUUFBUTthQUN4QjtTQUNKLENBQUMsQ0FBQztRQUVILFdBQVcsQ0FBQyxrQkFBa0IsQ0FBQyxZQUFZLENBQUMsQ0FBQztRQUU3QyxNQUFNLE9BQU8sR0FBRyxJQUFJLFlBQVksQ0FBQyxPQUFPLENBQUMsSUFBSSxFQUFFLFVBQVUsRUFBRTtZQUN2RCxPQUFPLEVBQUUsTUFBTTtZQUNmLGtCQUFrQixFQUFFLElBQUk7U0FDM0IsQ0FBQyxDQUFDO1FBRUgsT0FBTyxDQUFDLFNBQVMsQ0FBQztZQUNkLElBQUksRUFBRSxRQUFRO1lBQ2QsT0FBTyxFQUFFLENBQUMsNkJBQVUsQ0FBQyxHQUFHLENBQUM7WUFDekIsV0FBVyxFQUFFLElBQUksc0RBQXNCLENBQUMsRUFBQyxPQUFPLEVBQUUsWUFBWSxFQUFDLENBQUM7U0FDbkUsQ0FBQyxDQUFDO0lBRVAsQ0FBQztDQUNKO0FBckNELDRCQXFDQyIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCAqIGFzIGFwaWdhdGV3YXl2MiBmcm9tICdAYXdzLWNkay9hd3MtYXBpZ2F0ZXdheXYyJztcbmltcG9ydCAqIGFzIGFwaWdhdGV3YXl2MmludCBmcm9tICdAYXdzLWNkay9hd3MtYXBpZ2F0ZXdheXYyLWludGVncmF0aW9ucyc7XG5pbXBvcnQgKiBhcyBkeW5hbW9kYiBmcm9tICdAYXdzLWNkay9hd3MtZHluYW1vZGInO1xuaW1wb3J0ICogYXMgbGFtYmRhIGZyb20gJ0Bhd3MtY2RrL2F3cy1sYW1iZGEnO1xuaW1wb3J0ICogYXMgY2RrIGZyb20gJ0Bhd3MtY2RrL2NvcmUnO1xuaW1wb3J0IHtIdHRwTWV0aG9kfSBmcm9tIFwiQGF3cy1jZGsvYXdzLWFwaWdhdGV3YXl2MlwiO1xuaW1wb3J0IHtMYW1iZGFQcm94eUludGVncmF0aW9ufSBmcm9tIFwiQGF3cy1jZGsvYXdzLWFwaWdhdGV3YXl2Mi1pbnRlZ3JhdGlvbnNcIjtcblxuZXhwb3J0IGNsYXNzIENka1N0YWNrIGV4dGVuZHMgY2RrLlN0YWNrIHtcbiAgICBjb25zdHJ1Y3RvcihzY29wZTogY2RrLkFwcCwgaWQ6IHN0cmluZywgcHJvcHM/OiBjZGsuU3RhY2tQcm9wcykge1xuICAgICAgICBzdXBlcihzY29wZSwgaWQsIHByb3BzKTtcblxuICAgICAgICBjb25zdCBkeW5hbW9UYWJsZSA9IG5ldyBkeW5hbW9kYi5UYWJsZSh0aGlzLCAnaXRlbXMnLCB7XG4gICAgICAgICAgICBwYXJ0aXRpb25LZXk6IHtcbiAgICAgICAgICAgICAgICBuYW1lOiAnaXRlbUlkJyxcbiAgICAgICAgICAgICAgICB0eXBlOiBkeW5hbW9kYi5BdHRyaWJ1dGVUeXBlLlNUUklOR1xuICAgICAgICAgICAgfSxcbiAgICAgICAgICAgIHRhYmxlTmFtZTogJ2l0ZW1zJyxcbiAgICAgICAgICAgIHJlbW92YWxQb2xpY3k6IGNkay5SZW1vdmFsUG9saWN5LkRFU1RST1ksIC8vIE5PVCByZWNvbW1lbmRlZCBmb3IgcHJvZHVjdGlvbiBjb2RlXG4gICAgICAgIH0pO1xuXG4gICAgICAgIGNvbnN0IGdldE9uZUxhbWJkYSA9IG5ldyBsYW1iZGEuRnVuY3Rpb24odGhpcywgJ2dldE9uZUl0ZW1GdW5jdGlvbicsIHtcbiAgICAgICAgICAgIGNvZGU6IG5ldyBsYW1iZGEuQXNzZXRDb2RlKCcuLi9uaW5lZWlnaHQvYnVpbGQvbGlicycpLFxuICAgICAgICAgICAgaGFuZGxlcjogJ2dldC1vbmUuaGFuZGxlcicsXG4gICAgICAgICAgICBydW50aW1lOiBsYW1iZGEuUnVudGltZS5KQVZBXzExLFxuICAgICAgICAgICAgZW52aXJvbm1lbnQ6IHtcbiAgICAgICAgICAgICAgICBUQUJMRV9OQU1FOiBkeW5hbW9UYWJsZS50YWJsZU5hbWUsXG4gICAgICAgICAgICAgICAgUFJJTUFSWV9LRVk6ICdpdGVtSWQnXG4gICAgICAgICAgICB9XG4gICAgICAgIH0pO1xuXG4gICAgICAgIGR5bmFtb1RhYmxlLmdyYW50UmVhZFdyaXRlRGF0YShnZXRPbmVMYW1iZGEpO1xuXG4gICAgICAgIGNvbnN0IGh0dHBBcGkgPSBuZXcgYXBpZ2F0ZXdheXYyLkh0dHBBcGkodGhpcywgJ2l0ZW1zQXBpJywge1xuICAgICAgICAgICAgYXBpTmFtZTogJ3Jlc3QnLFxuICAgICAgICAgICAgY3JlYXRlRGVmYXVsdFN0YWdlOiB0cnVlXG4gICAgICAgIH0pO1xuXG4gICAgICAgIGh0dHBBcGkuYWRkUm91dGVzKHtcbiAgICAgICAgICAgIHBhdGg6ICcvYm9va3MnLFxuICAgICAgICAgICAgbWV0aG9kczogW0h0dHBNZXRob2QuQU5ZXSxcbiAgICAgICAgICAgIGludGVncmF0aW9uOiBuZXcgTGFtYmRhUHJveHlJbnRlZ3JhdGlvbih7aGFuZGxlcjogZ2V0T25lTGFtYmRhfSksXG4gICAgICAgIH0pO1xuXG4gICAgfVxufVxuIl19