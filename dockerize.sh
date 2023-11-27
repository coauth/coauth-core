cd core-api-gateway
./mvnw clean package
docker build -t godwinpinto/coauth-core-api-gateway:0.0.1-SNAPSHOT .
docker push godwinpinto/coauth-core-api-gateway:0.0.1-SNAPSHOT
docker tag godwinpinto/coauth-core-api-gateway:0.0.1-SNAPSHOT quay.io/coauth/coauth-core-api-gateway:0.0.1-SNAPSHOT
docker push quay.io/coauth/coauth-core-api-gateway:0.0.1-SNAPSHOT

cd ..
cd core-auth-guard
./mvnw clean package
docker build -f src/main/docker/Dockerfile.jvm -t godwinpinto/coauth-core-auth-guard:1.0.0-SNAPSHOT .
docker push godwinpinto/coauth-core-auth-guard:1.0.0-SNAPSHOT
docker tag godwinpinto/coauth-core-auth-guard:1.0.0-SNAPSHOT quay.io/coauth/coauth-core-auth-guard:1.0.0-SNAPSHOT
docker push quay.io/coauth/coauth-core-auth-guard:1.0.0-SNAPSHOT

cd ..
cd core-module-registry
./mvnw clean package
docker build -f src/main/docker/Dockerfile.jvm -t godwinpinto/coauth-core-module-registry:1.0.0-SNAPSHOT .
docker push godwinpinto/coauth-core-module-registry:1.0.0-SNAPSHOT
docker tag godwinpinto/coauth-core-module-registry:1.0.0-SNAPSHOT quay.io/coauth/coauth-core-module-registry:1.0.0-SNAPSHOT
docker push quay.io/coauth/coauth-core-module-registry:1.0.0-SNAPSHOT

cd ..
cd module-reconfirm
./mvnw clean package
docker build -f src/main/docker/Dockerfile.jvm -t godwinpinto/coauth-module-reconfirm:1.0.0-SNAPSHOT .
docker push godwinpinto/coauth-module-reconfirm:1.0.0-SNAPSHOT
docker tag godwinpinto/coauth-module-reconfirm:1.0.0-SNAPSHOT quay.io/coauth/coauth-module-reconfirm:1.0.0-SNAPSHOT
docker push quay.io/coauth/coauth-module-reconfirm:1.0.0-SNAPSHOT

cd ..
cd module-totp
./mvnw clean package
docker build -f src/main/docker/Dockerfile.jvm -t godwinpinto/coauth-module-totp:1.0.0-SNAPSHOT .
docker push godwinpinto/coauth-module-totp:1.0.0-SNAPSHOT
docker tag godwinpinto/coauth-module-totp:1.0.0-SNAPSHOT quay.io/coauth/coauth-module-totp:1.0.0-SNAPSHOT
docker push quay.io/coauth/coauth-module-totp:1.0.0-SNAPSHOT

# cd ..
# cd ..
# cd coauth-plugin-web
# docker build -t godwinpinto/coauth-plugin-web:1.0.0-SNAPSHOT .
# docker push godwinpinto/coauth-plugin-web:1.0.0-SNAPSHOT
# docker tag godwinpinto/coauth-plugin-web:1.0.0-SNAPSHOT quay.io/godwin_pinto86/coauth-plugin-web:1.0.0-SNAPSHOT
# docker push quay.io/godwin_pinto86/coauth-plugin-web:1.0.0-SNAPSHOT

# cd ..
# cd example-quarkus-vue
# ./mvnw clean package
# docker build -f src/main/docker/Dockerfile.jvm -t godwinpinto/coauth-example-quarkus-vue:1.0.0-SNAPSHOT .
# docker push godwinpinto/coauth-example-quarkus-vue:1.0.0-SNAPSHOT
# docker tag godwinpinto/coauth-example-quarkus-vue:1.0.0-SNAPSHOT quay.io/godwin_pinto86/coauth-example-quarkus-vue:1.0.0-SNAPSHOT
# docker push quay.io/godwin_pinto86/coauth-example-quarkus-vue:1.0.0-SNAPSHOT

# cd ..
# cd coauth-management
# cd coauth-management-ui
# docker build -t godwinpinto/coauth-management-ui:1.0.0-SNAPSHOT .
# docker push godwinpinto/coauth-management-ui:1.0.0-SNAPSHOT
# docker tag godwinpinto/coauth-management-ui:1.0.0-SNAPSHOT quay.io/godwin_pinto86/coauth-management-ui:1.0.0-SNAPSHOT
# docker push quay.io/godwin_pinto86/coauth-management-ui:1.0.0-SNAPSHOT


