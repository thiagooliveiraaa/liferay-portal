# Liferay Sample Workspace

## Client Extensions

Client extensions are the recommended way of customizing Liferay. Modules and themes are supported for backwards compatibility.

To deploy all client extensions, go to `liferay-sample-workspace/client-extensions` and type `gw deploy`.

To deploy a specific client textension, go to `liferay-sample-workspace` and type `gw :client-extensions:liferay-sample-etc-cron:deploy`
### liferay-sample-custom-element-5

### liferay-sample-etc-cron

### liferay-sample-etc-frontend-1

This sample was created to showcase that you can combine multiple client extensions in a single `client-extension.yaml`.

Client Extensions Included:
- liferay-sample-etc-frontend-1-custom-element-header (`customElement`)
- liferay-sample-etc-frontend-1-custom-element-sidebar (`customElement`)
- liferay-sample-etc-frontend-1-custom-element-theme-spritemap (`themeSpritemap`)
- liferay-sample-etc-frontend-1-fds-cell-renderer (`fdsCellRenderer`)

### liferay-sample-etc-frontend-2

This sample was created to showcase that you can share code across multiple client extensions in a single `client-extension.yaml`.

Each client extension shares the same code from `shared-utils.js` without duplicating or re-requesting the resource.

Client Extensions Included:
- liferay-sample-etc-frontend-2-custom-element (`customElement`)
- liferay-sample-etc-frontend-2-fds-cell-renderer (`fdsCellRenderer`)