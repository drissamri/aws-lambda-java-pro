STACK_NAME=${1:-'favorites-service-dev'}

mvn clean package

echo "Start deploy stack:" $STACK_NAME
sam deploy  --stack-name $STACK_NAME \




