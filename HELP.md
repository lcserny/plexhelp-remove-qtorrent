### Local setup
Build with:  
`./mvnw -Pnative clean package native:compile`

Copy native executable to your start cli path:  
`cp target/remove-qtorrent your/Path`

Create config folder in your start cli path:  
`mkdir your/Path/config`

Create your application.properties file (check `resources` for defaults) and copy in `config` folder:  
`cp application.properties your/Path/config`

Start cli from your path (it will auto read the properties in config):  
`./remove-qtorrent`
