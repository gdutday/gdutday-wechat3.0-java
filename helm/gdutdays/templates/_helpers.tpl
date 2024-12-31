{{/*
生成 Chart 的名称
*/}}
{{- define "gdutdays4.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
生成全名
*/}}
{{- define "gdutdays4.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $name := default .Chart.Name .Values.nameOverride -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}

{{/*
通用标签
*/}}
{{- define "gdutdays4.labels" -}}
helm.sh/chart: {{ include "gdutdays4.chart" . }}
app.kubernetes.io/name: {{ include "gdutdays4.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/version: {{ .Chart.AppVersion }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}

{{/*
选择器标签
*/}}
{{- define "gdutdays4.selectorLabels" -}}
app.kubernetes.io/name: {{ include "gdutdays4.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}

{{/*
Chart 名称和版本
*/}}
{{- define "gdutdays4.chart" -}}
{{- if .Chart.Name }}{{ .Chart.Name }}-{{ .Chart.Version }}{{ end }}
{{- end -}}