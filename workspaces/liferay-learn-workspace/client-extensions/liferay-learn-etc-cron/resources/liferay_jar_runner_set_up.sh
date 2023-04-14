#!/bin/bash

function send_slack_message() {
	SLACK_MESSAGE=$1

	echo "$SLACK_MESSAGE"

	if [ -z "$LIFERAY_LEARN_ETC_CRON_SLACK_ENDPOINT" ] ; then return 0; fi

	TIMESTAMP=$(date)
	LOG_URL="https://console.${LCP_INFRASTRUCTURE_DOMAIN}/projects/${LCP_PROJECT_ID}/services/${LCP_SERVICE_ID}/logs?instanceId=${HOSTNAME}&logServiceId=${LCP_SERVICE_ID}"

	SLACK_MESSAGE_TEXT="${TIMESTAMP} *${LCP_PROJECT_ID}*->*${LCP_SERVICE_ID}* <${LOG_URL}|${HOSTNAME}> \n>$SLACK_MESSAGE"

	curl -X POST --data-urlencode "payload={'channel': '${LIFERAY_LEARN_ETC_CRON_SLACK_CHANNEL}', 'username': 'devopsbot', 'text': '${SLACK_MESSAGE_TEXT}', 'icon_emoji': ':robot_face:'}" ${LIFERAY_LEARN_ETC_CRON_SLACK_ENDPOINT}
}

function main {
}

main "${@}"