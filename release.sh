mvn release:prepare release:perform
cd target/checkout && mvn deploy -P'!local-repository,release'
echo "Открыть https://oss.sonatype.org/#stagingRepositories"
echo "отметить ruilb-XXXX, Close -> Confirm, подождать нажимая Refresh, пока Activity не станет Last operation completed successfully, нажать Release -> Confirm"