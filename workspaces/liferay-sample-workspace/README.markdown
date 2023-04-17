# Liferay Sample Workspace

The Liferay Sample workspace contains an example of every client extension and is meant to be the ***primary*** source of truth of how client extensions work by documenting via code.

Feel free to initiate your project by copying the samples found here. But make sure to rename your client extensions according to our established [naming conventions](#naming-conventions).

## Client Extensions

Client extensions are the recommended way of customizing Liferay. Modules and themes are supported for backwards compatibility.

To deploy all client extensions, go to `liferay-sample-workspace` and type `./gradlew deploy`.

To deploy a specific client extension (e.g. liferay-sample-custom-element-1), go to `liferay-sample-workspace` and type `./gradlew :client-extensions:liferay-sample-custom-element-1:deploy`.

### Naming Conventions

The standard directory name of a client extension is broken up into several parts. The first two parts, the owner and project, are separated by `-`.

For `liferay-sample-batch`, the owner is `liferay` and the project is `sample`. The owner and the project must not contain `-` since we use `-` to differentiate the owner from the project.

The third part is usually one of the available client extension types: batch, custom-element, fds-cell-renderer, global-css, global-js, iframe, notification-type, oauth-application-headless-server, oauth-application-user-agent, object-action, site-initializer, static-content, theme-css, theme-favicon, theme-js, theme-spritemap, or workflow-action.

For `liferay-sample-batch`, the third part is the client extension type `batch`.

For `liferay-sample-custom-element-1` and `liferay-sample-custom-element-2`, the third part, `custom-element`, means this is a custom element. The fourth parts, `1` and `2`, are a general description that can be anything.

If the third part it is not a client extension type, then the third part must be the special keyword `etc`.

For `liferay-sample-etc-cron` and `liferay-sample-etc-spring-boot` the third type is `etc`. The fourth parts, `cron` and `spring-boot` are general descriptions that can be anything.

### List of Client Extensions

- liferay-sample-batch

- liferay-sample-custom-element-1

- liferay-sample-custom-element-2

- liferay-sample-custom-element-3

- liferay-sample-custom-element-4

- liferay-sample-custom-element-5

	The sample custom element uses `@clayui/badge`, `react`, and `react-dom` packages that Liferay makes publicly available through import maps.

- liferay-sample-etc-cron

	Use Spring Boot and OAuth (server to server) to read from and write to Liferay in timed intervals.

	To see this in action on your local machine:

	1. Go to `liferay-sample-workspace` and type `./gradlew startDockerContainer logsDockerContainer` to start Liferay.

	1. Go to `liferay-sample-workspace` and type `./gradlew :client-extensions:liferay-sample-etc-cron:deploy`.

	1. Login to Liferay and go to Control Panel > Configuration > OAuth 2 Administration. Select `Liferay Sample Etc Cron`.

	1. Copy the client secret. Open `liferay-sample-workspace/client-extensions/liferay-sample-etc-cron/src/main/resources/application-default.properties` and replace `liferay-sample-etc-cron.oauth2.headless.server.client.secret=myfancypassword` with the real client secret.

	1. Go to `liferay-sample-workspace/client-extensions/liferay-sample-etc-cron` and type `./gradlew bootRun` to start Spring Boot.

- liferay-sample-etc-frontend-1

	The `client-extension.yaml` contains multiple frontend client extensions.

- liferay-sample-etc-frontend-2

	The `client-extension.yaml` contains multiple frontend client extensions that use code from `shared-utils.js` without duplicating or rerequesting it.

- liferay-sample-etc-spring-boot

	Use Spring Boot and OAuth (human to server) to interact with Liferay.

- liferay-sample-fds-cell-renderer

- liferay-sample-global-css

- liferay-sample-global-js

- liferay-sample-iframe-1

- liferay-sample-iframe-2

- liferay-sample-instance-settings

- liferay-sample-static-content

- liferay-sample-theme-css

- liferay-sample-theme-favicon

- liferay-sample-theme-spritemap-1

- liferay-sample-theme-spritemap-2