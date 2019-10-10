# Filedossier frontend + components

## Setup

.config/context.xml should contain

<Environment name="ru.bystrobank.apps.workflow.certfile" value="/path/to/file.pem" type="java.lang.String" override="false"/>
<Environment name="ru.bystrobank.apps.workflow.cert_PASSWORD" value="<password>" type="java.lang.String" override="false"/>

environment variable NODE_EXTRA_CA_CERTS should be set with valid path to crt bundle
