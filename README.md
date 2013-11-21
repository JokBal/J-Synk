# J-Synk

This framework is real-time messaging platform running on vert.x framework.
This framework is compatible with pusher(http://pusher.com)'s server and client libraries

## Dependencies

This module is using vert.x mongo persistor module(https://github.com/vert-x/mod-mongo-persistor/)
and vert.x redis module (https://github.com/vert-x/mod-redis)

## Configuration

The J-Synk frame work takes the following configuration:

    {
      "api_key" = <any string same with client's configuration>,
    	"secret" = <any string same with server's configuration>,
    	"port":<websocket server>,
    	"eventbus_prefix":<this will add prefix at using eventbus>,
    	"shareddata_prefix":<this will add prefix at saving and loadin shared data>,
    	
    	"permanent_enable":<whether using permanent channel. permanent channel requires gcm and mongodb configuration>
    	"gcm_config":{
    		"api_key":<YOUR GCM SERVER KEY>	
    	},
    	"mongodb_config"={
    	    "address": <address>,
    	    "host": <host>,
    	    "port": <port>,
    	    "db_name": <db_name>,
    	    "pool_size": <pool_size>,
    	    "fake": <fake>
    	},
    	"redis_enabled"=<if it is set, J-Synk use redis instead of shared data. requires redis configuration. when J-Synk running on clustering mod, it should use redis.>,
    	"redis_config"={
    		"address": <address>,
    	    "host": <host>,
    	    "port": <port>,
    	    "encoding": <charset>,
    	    "binary": <boolean>,
    	    "auth": <password>,
    	    "db": <number>
    	},
    	"api_server_instance":<number of api server instance to deploy. max is 5>,
    	"socket_server_instance":<number of socket server instance to deploy max is 5>,
    	"redis_instance":<number of redis module instance to deploy>
    	"mongo_instance":<number of mongo module instance to deploy>
    }
    
  

