#!/bin/bash
set -e

echo $TEST

if [[ -z "$PUBLISH_GITHUB_WEBSITE_TOKEN" ]]; then
  echo "No token specified"
  exit 1;
fi

curl -L \
  -H 'Accept: application/vnd.github+json' \
  -H 'X-GitHub-Api-Version: 2022-11-28' \
  -H "Authorization: $PUBLISH_GITHUB_WEBSITE_TOKEN" \
  'https://api.github.com/repos/Beyamor/beyamor.github.io/dispatches' \
  -d '{"event_type": "test"}'