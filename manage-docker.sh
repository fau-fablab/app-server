#!/bin/bash

set -e

IMAGE="app-server"
CONTAINER="${IMAGE}"
CONF_DIR="${PWD}/conf/"
VOL_LOC="${CONF_DIR}"
VOL_DOC="/home/fablab/app-server/src/dist/"
SHELL="bash"

if [ $(whoami) != "root" ]; then echo "[i] this script has to be executed as root!" && exit 1; fi

function port() {
    docker port "${CONTAINER}"
}
function build() {
    docker build -t "${IMAGE}" .
}
function run() {
    docker run -d -p 80 -p 8081 --volume="${VOL_LOC}:${VOL_DOC}" \
        --name="${CONTAINER}" "${IMAGE}"
}
function up() {
    if [ ! -e "${CONF_DIR}/config.yml" ] && [ ! -e "${CONF_DIR}/minimumVersion.yml" ]; then
        echo "[x] ${CONF_DIR}/config.yml or ${CONF_DIR}/minimumVersion.yml is missing"
        exit 1
    else
        build
        run
    fi
}
function start() {
    docker start "${CONTAINER}"
}
function stop() {
    docker stop "${CONTAINER}"
}
function restart() {
    docker restart "${CONTAINER}"
}
function clean() {
    read -p "Do you really want to delete the container including ALL data? [y/N] "
    if [ "$REPLY" == "y" ]; then
        docker rm -v "${CONTAINER}"
    fi
}
function shell() {
    docker exec -it "${CONTAINER}" "${SHELL}"
}
function logs() {
    docker logs -f "${CONTAINER}"
}

FUNCTIONS=("up" "run" "start" "stop" "restart" "build" "clean" "shell" "port" "logs")

if [ -z $1 ] || [[ ! "${FUNCTIONS[@]}" =~ "$1" ]]; then
    echo "Usage: manage.sh [COMMAND]"
    echo ""
    echo "Commands:"
    echo "    up       build and run"
    echo "    run      run an already built container"
    echo "    start    start an already created container"
    echo "    stop     stop a running container"
    echo "    restart  stop + start"
    echo "    build    build a new container"
    echo "    clean    remove all persistent data"
    echo "    shell    attach to running container and run a shell"
    echo "    port     query the port where the appserver is bound to"
    echo "    logs     tail the logs of dropwizard"
else
    $1  # run the command
fi
