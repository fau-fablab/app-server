#!/bin/bash

# (C) 2015 Maximilian Gaukler <development@maxgaukler.de>
# MIT License, see LICENSE file
# feel free to e-mail me if you need another license

# Entrypoint script:
# If a ENVFILE variable is present, load the environment file given in $ENVFILE.
# Then transparently start the given program.

# (this is a workaround for:
# https://github.com/docker/docker/issues/5169
# related: https://github.com/docker-library/mariadb/issues/21
# )

vars_before=$(compgen -v)

list_contains() {
    # usage: list_contains "$list" $search_word
    # return 0 (success) if the space-separated list contains search_word
    for item in $1; do
        [[ $item = $2 ]] && return 0
    done
    return 1
}

# load variables (as local shell variables)
# if ENVFILE is not set, do nothing
source ${ENVFILE:-/dev/null}

# export all variables - this code is not very good, but it works :-P
# variable names may not contain spaces etc.

for var in $(compgen -v); do
    # skip variables that don't come from our ENVFILE, but from bash internals
    list_contains "$vars_before" $var && continue
    export $var
done

exec "$@"
