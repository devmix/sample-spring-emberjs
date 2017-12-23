# Warehouse

- Maven v3.x
- H2
- Spring Boot v1.5.9 
- EmberJS v2.17

## Build
```
mvn clean install -Pui
```

## Run
```
java -jar launcher/web-server/target/launcher-web-server-1.0-SNAPSHOT.jar
```

## EmberJS
 
### Debug mode

Run the command below in the folder `ui/webui`.
```
ember serve
```
The page will be available at http://localhost:4200

### Semantic UI 

#### Build

```
npm run-script semantic-ui-build
```
or
```
gulp build --gulpfile vendor/semantic-ui/gulpfile.js
```

#### Watching for changes

```
npm run-script semantic-ui-watch
```
or
```
gulp watch --gulpfile vendor/semantic-ui/gulpfile.js
```

## H2 Console

- url: http://localhost:8080/console
- user: sa
- pass:

## Screenshots

### Browse

![alt Browse](screenshots/storage-books-browse.png)

### Edit

![alt Edit](screenshots/storage-books-edit.png)
