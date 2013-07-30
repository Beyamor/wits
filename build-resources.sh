#!/bin/bash

set -e

sass --update src/wits/style/:resources/public/css
coffee --output resources/public/js src/wits/coffee
