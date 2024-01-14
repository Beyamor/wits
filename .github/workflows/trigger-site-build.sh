#!/bin/bash
set -e

if [[ -z $PUBLISH_GITHUB_WEBSITE_TOKEN ]]; then
  echo "No token specified"
  exit 1;
fi

curl -L \
  -H 'Accept: application/vnd.github+json' \
  -H "Authorization: $PUBLISH_GITHUB_WEBSITE_TOKEN" \
  -H 'X-GitHub-Api-Version: 2022-11-28' \
  'https://api.github.com/repos/Beyamor/beyamor.github.io/dispatches' \
  -d '{"event_type": "test"}'