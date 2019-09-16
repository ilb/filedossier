#!/bin/sh
set -e
cd target/generated-sources/openapi-javascript
npm install
npm run build
npm publish
