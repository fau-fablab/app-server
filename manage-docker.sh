#!/bin/bash

FUNCTIONS=("up" "run" "run-shell" "start" "stop" "restart" "build" "clean" "shell" "port" "logs")

if [ "$(basename $0)" == "$(basename ${BASH_SOURCE})" ] ; then
    if [ -z $1 ] || [[ ! "${FUNCTIONS[@]}" =~ "$1" ]]; then
        echo "Usage: manage.sh [COMMAND]"
        echo ""
        echo "Commands:"
        echo "    up        build and run"
        echo "    run       run an already built container"
        echo "    run-shell run a shell in an already built container (overwrites CMD)"
        echo "    start     start an already created container"
        echo "    stop      stop a running container"
        echo "    restart   stop + start"
        echo "    build     build a new container"
        echo "    clean     remove all persistent data"
        echo "    shell     attach to running container and run a shell"
        echo "    port      query the port where the appserver is bound to"
        echo "    logs      tail the logs of dropwizard"
        echo "ProTip: source this file for tab completion"
    else

        set -e # exit on error

        IMAGE="app-server"
        CONTAINER="${IMAGE}"
        CONF_DIR="${PWD}/conf/"
        VOL_LOC="${CONF_DIR}"
        VOL_DOC="/home/fablab/app-server/src/dist/"
        SHELL="bash"

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
        function run-shell() {
            docker run --rm -it -p 80 -p 8081 --volume="${VOL_LOC}/:${VOL_DOC}/" \
                --name="${CONTAINER}-interactive" "${IMAGE}" "${SHELL}"
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
            if [ "$REPLY" == "n" ]; then
                docker rm -v "${CONTAINER}"
            fi
        }
        function shell() {
            docker exec -it "${CONTAINER}" "${SHELL}"
        }
        function logs() {
            docker logs -f "${CONTAINER}"
        }

        if [ $(whoami) != "root" ]; then echo "[!] this script has to be executed as root!" && exit 1; fi

        $1  # run the command

    fi

else
    # someone is sourcing this file -> tab completion
    if $(complete -p "./$(basename ${BASH_SOURCE})" 2>/dev/null) ; then complete -r "./$(basename ${BASH_SOURCE})"; fi
    complete -W "$(echo ${FUNCTIONS[@]})" "$(basename ${BASH_SOURCE})"
    unset FUNCTIONS
    echo "[i] successfully sourced this file - happy tabbing"
fi
