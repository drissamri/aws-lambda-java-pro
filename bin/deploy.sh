STACK_NAME=${1:-'favorites-service-dev'}
REGION=eu-west-1
S3_DEPLOY_BUCKET=damri-deploys

mvn clean package

echo "Start deploy stack:" $STACK_NAME
sam deploy \
  --s3-bucket $S3_DEPLOY_BUCKET \
  --stack-name $STACK_NAME \
  --capabilities CAPABILITY_IAM \
  --region $REGION



