# Liferay Sample Workspace

The Liferay Sample workspace contains an example of every client extension and is meant to be the ***primary*** source of truth of how client extensions work by documenting via code.

Feel free to initiate your project by copying the samples found here. But make sure to rename your client extensions according to our established [naming conventions](#naming-conventions).

## Client Extensions

Client extensions are the recommended way of customizing Liferay. Modules and themes are supported for backwards compatibility.

To deploy all client extensions, go to `liferay-sample-workspace` and type `./gradlew deploy`.

To deploy a specific client extension (e.g. liferay-sample-custom-element-1), go to `liferay-sample-workspace` and type `./gradlew :client-extensions:liferay-sample-custom-element-1:deploy`.

### Naming Conventions

The standard directory name of a client extension is broken up into several parts. The first two parts, the owner and project, is separated by `-`.

For `liferay-sample-batch`, the owner is `liferay` and the project is `sample`. The owner and project must not contain `-` since we use `-` to differentiate the owner from the project.

The third part is usually one of the available client extension types: batch, custom-element, fds-cell-renderer, global-css, global-js, iframe, notification-type, oauth-application-headless-server, oauth-application-user-agent, object-action, site-initializer, static-content, theme-css, theme-favicon, theme-js, theme-spritemap, or workflow-action.

For `liferay-sample-batch`, the third part is the client extension type `batch`.

For `liferay-sample-custom-element-1` and `liferay-sample-custom-element-2`, the third part is the client extension type `custom-element`. The fourth parts, `1` and `2`, are a general description that can be anything.

If the third part it is not a client extension type, then the third part must be the special keyword `etc`.

For `liferay-sample-etc-cron` and `liferay-sample-etc-spring-boot` the third type is `etc`. The fourth parts, `cron` and `spring-boot` are general descriptions that can be anything.

### List of Client Extensions

- liferay-sample-batch

- liferay-sample-custom-element-1

- liferay-sample-custom-element-2

- liferay-sample-custom-element-3

- liferay-sample-custom-element-4

- liferay-sample-custom-element-5

	This custom element uses `@clayui/badge`, `react`, and `react-dom` packages that Liferay makes publicly available through import maps.

- liferay-sample-etc-cron

- liferay-sample-etc-frontend-1

	The `client-extension.yaml` in this directory contains multiple frontend client extensions.

- liferay-sample-etc-frontend-2

	The `client-extension.yaml` in this directory contains multiple frontend client extensions that use code from `shared-utils.js` without duplicating or rerequesting it.

- liferay-sample-etc-spring-boot

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