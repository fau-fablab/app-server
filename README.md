# fablab-server [![Build Status](https://travis-ci.org/fau-fablab/app-server.svg?branch=master)](https://travis-ci.org/fau-fablab/app-server) [![Docker Repository on Quay](https://quay.io/repository/faufablab/app-server/status "Docker Repository on Quay")](https://quay.io/repository/faufablab/app-server)

app-server is a REST server based on [Dropwizard](http://www.dropwizard.io) which is currently used by the following apps:
- [fablab-android](https://github.com/fau-fablab/fablab-android)
- [fablab-iOS](https://github.com/fau-fablab/fablab-ios)
- [app-HTML](https://github.com/fau-fablab/app-html)

## Deployment with Docker

The following files have to be present in order to run the server:

- Apple Push certificate
- Java Keystore with ssl cert inside
- doorstate encryption key

Copy them to `./conf`.

After that you have to copy `conf/{config.yml.example,minimumVersion.yml.example}` to `conf/{config.yml,minimumVersion.yml}` and adapt them to your needs.

Finally, build and run the server with

```bash
sudo ./manage-docker.sh up
```

The container will listen on port 80 for application requests and port 8081 for administrative requests. These ports are bound to random ports on localhost. You can get them by running `sudo ./manage-docker.sh port`.

## Two words about Türstatus

 - FAU FabLab has a serial magnetic sensor on their door
 - it is connected to "ws01" (a linux desktop pc (with some server jobs))
   - there is a user called `tuerstatus`
   - he runs the script `/home/tuerstatus/update-status.sh`
   - this script polls the website (https://fablab.fau.de) and the app-server (if configured at top of the script)
   - the poll is a `HTTP GET` request like: `?data=${time}:${state}&hash=${hmac_hash}`
     - `$time` is the current unix timestamp (`date +%s`)
     - `$state` is `open` or `close`
     - `$hmac_hash` is the `sha256` hmac hash of the message `$time:$state` with a key
     - the key is a bit randomness or anything else
  - the script expects a simple `OK` or a json `{ "success": true, [...] }`

 - If you want to receive such hooks, you have to
   - verify the given time (with some threshold)
   - verify the integrity
     - you need the same key as on ws01
     - e.g. `key=$(cat keyfile) echo -n "${time}:${status}" | openssl dgst -sha256 -hmac "${key}"`
 - Overengineered? No!

## Libraries

app-server uses the following libraries and software:
* [docker-library/mysql/5.6](https://github.com/docker-library/mysql/tree/1f430aeee538aec3b51554ca9fc66955231b3563/5.6)     License: [GNU GPL Version 2](https://github.com/docker-library/mysql/blob/1f430aeee538aec3b51554ca9fc66955231b3563/LICENSE)
* [jsonrpc2-client:1.15](http://software.dzhuvinov.com/json-rpc-2.0-client.html)        License: [Apache 2.0](http://software.dzhuvinov.com/files/jsonrpc2server/LICENSE.txt)
* [ical4j:1.0.5](https://github.com/ical4j/ical4j)    License: (https://github.com/ical4j/ical4j/blob/master/LICENSE)
* [jackson-dataformat-xml:2.5.1](https://github.com/FasterXML/jackson-dataformat-xml)   License: [Apache 2.0](https://github.com/FasterXML/jackson-dataformat-xml/wiki#licensing)
* [jsoup:1.8.2](http://jsoup.org)  License: [MIT License](http://jsoup.org/license)
* [cron4j:2.2.5](http://www.sauronsoftware.it/projects/cron4j/) License: [LGPL](http://www.sauronsoftware.it/projects/cron4j/)
* [pushy:0.4.3](https://github.com/relayrides/pushy) License: [MIT](https://github.com/relayrides/pushy)
