#!/bin/sh
cd $TRAVIS_BUILD_DIR/server
npm install
npm test
