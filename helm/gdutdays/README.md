# gdutdays-helm

![Version: 0.1.0](https://img.shields.io/badge/Version-0.1.0-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: v4](https://img.shields.io/badge/AppVersion-v4-informational?style=flat-square)

A Helm chart for deploying gdutdays v4 application backend

**Homepage:** <https://gdutdays.gdutelc.com>

## Maintainers

| Name         | Email                    | Url |
|--------------|--------------------------|-----|
| gregPerlinLi | <lihaolin13@outlook.com> |     |

## Source Code

* <https://github.com/gdutday/gdutday-wechat3.0-java>
* <https://git.gdutnic.com/gregPerlinLi/gdutdays>

## Values

| Key                                                              | Type   | Default                                    | Description |
|------------------------------------------------------------------|--------|--------------------------------------------|-------------|
| affinity                                                         | object | `{}`                                       |             |
| fullnameOverride                                                 | string | `""`                                       |             |
| gdutdays.secret                                                  | string | `"changemechangeme"`                       |             |
| image.pullPolicy                                                 | string | `"IfNotPresent"`                           |             |
| image.repository                                                 | string | `"registry.gdutnic.com/gdutdays/gdutdays"` |             |
| image.tag                                                        | string | `"latest"`                                 |             |
| ingress.annotations."nginx.ingress.kubernetes.io/rewrite-target" | string | `"/gdutDay3/$1"`                           |             |
| ingress.annotations."nginx.ingress.kubernetes.io/server-tokens"  | string | `"false"`                                  |             |
| ingress.annotations."nginx.ingress.kubernetes.io/use-regex"      | string | `"true"`                                   |             |
| ingress.className                                                | string | `"nginx"`                                  |             |
| ingress.enabled                                                  | bool   | `false`                                    |             |
| ingress.hosts[0].host                                            | string | `"gdutdays.gdutelc.com"`                   |             |
| ingress.hosts[0].paths[0].path                                   | string | `"/v3/(.*)"`                               |             |
| ingress.hosts[0].paths[0].pathType                               | string | `"ImplementationSpecific"`                 |             |
| ingress.tls[0].hosts[0]                                          | string | `"gdutdays.gdutelc.com"`                   |             |
| ingress.tls[0].secretName                                        | string | `"gdutdays-tls"`                           |             |
| nameOverride                                                     | string | `""`                                       |             |
| nodeSelector                                                     | object | `{}`                                       |             |
| replicaCount                                                     | int    | `1`                                        |             |
| resources                                                        | object | `{}`                                       |             |
| service.ports[0].name                                            | string | `"http"`                                   |             |
| service.ports[0].nodePort                                        | int    | `31880`                                    |             |
| service.ports[0].port                                            | int    | `80`                                       |             |
| service.ports[0].targetPort                                      | int    | `8080`                                     |             |
| service.ports[1].name                                            | string | `"metrics"`                                |             |
| service.ports[1].port                                            | int    | `8092`                                     |             |
| service.ports[1].targetPort                                      | int    | `8092`                                     |             |
| service.type                                                     | string | `"NodePort"`                               |             |
| serviceMonitor.annotations                                       | object | `{}`                                       |             |
| serviceMonitor.enabled                                           | bool   | `true`                                     |             |
| serviceMonitor.honorLabels                                       | bool   | `false`                                    |             |
| serviceMonitor.interval                                          | string | `"30s"`                                    |             |
| serviceMonitor.labels                                            | object | `{}`                                       |             |
| serviceMonitor.metricRelabelings                                 | list   | `[]`                                       |             |
| serviceMonitor.path                                              | string | `"/actuator/prometheus"`                   |             |
| serviceMonitor.relabelings                                       | list   | `[]`                                       |             |
| serviceMonitor.scrapeTimeout                                     | string | `"10s"`                                    |             |
| serviceMonitor.selector.matchLabels."app.kubernetes.io/name"     | string | `"gdutday4"`                               |             |
| tolerations                                                      | list   | `[]`                                       |             |

----------------------------------------------
Autogenerated from chart metadata using [helm-docs v1.14.2](https://github.com/norwoodj/helm-docs/releases/v1.14.2)
