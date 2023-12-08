cd core-api-gateway
./mvnw clean package
docker build -t coauth-core-api-gateway .

cd ..
cd core-modules
./mvnw clean package

cd core-auth-guard
docker build -f src/main/docker/Dockerfile.jvm -t coauth-core-auth-guard .

cd ..
cd core-module-registry
docker build -f src/main/docker/Dockerfile.jvm -t coauth-core-module-registry .

cd ..
cd module-reconfirm
docker build -f src/main/docker/Dockerfile.jvm -t coauth-module-reconfirm .

cd ..
cd module-totp
docker build -f src/main/docker/Dockerfile.jvm -t coauth-module-totp .


