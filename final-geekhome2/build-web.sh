#!/bin/bash

cd src/main/vuetify
rm -rf dist
npm run build
mv dist ../resources/