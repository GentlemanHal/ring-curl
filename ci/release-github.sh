#!/bin/bash -eu

echo "Creating a GitHub release with tag [${VERSION}]"

responseJson=$(curl \
  -u ${GITHUB_USERNAME}:${GITHUB_TOKEN} \
  -H "Content-Type: application/vnd.github.v3+json" \
  -d "{\"tag_name\": \"v${VERSION}\", \"target_commitish\": \"master\", \"name\": \"${VERSION}\", \"draft\": true, \"prerelease\": false}" \
  https://api.github.com/repos/build-canaries/ring-curl/releases)

echo "Got response [${responseJson}]"

uploadUrl=$(echo ${responseJson} | jq -r '.upload_url' | sed "s|{?name,label}||")

jar="ring-curl-${VERSION}.jar"

echo "Adding the ${jar} as an asset using URL [${uploadUrl}]"

curl \
  -u ${GITHUB_USERNAME}:${GITHUB_TOKEN} \
  -H "Content-Type: application/zip" \
  --data-binary "@./target/${jar}" \
  "${uploadUrl}?name=${jar}"
