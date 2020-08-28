# XN-L/XP Analyzer Plugin

This is an OpenELIS plugin that processes results from the Sysmex XN-L and Siemens XP analyzers.

## Development setup

### Dependency:
- [OpenELIS Global 2](https://github.com/I-TECH-UW/OpenELIS-Global-2 "OpenELIS Global 2")

The above dependency has a transitive dependency data-export-api with in not available in maven central and so the following steps are required:

1. Clone and compile [dataexports](https://github.com/I-TECH-UW/dataexport/tree/72b8f1640d4b99ab5bf26ae1eb504a5d90a9b08c "dataexports") to have it in local maven repository.

2. Clone **OpenELIS Global 2** and add the following configuration to the maven-war-plugin.

```xml
<configuration>
	<attachClasses>true</attachClasses>
</configuration>
```
After compilation, this creates a JAR package in the local maven repository that will be picked up by this project.

**NOTE**: The additional war plugin configuration is required so that the classes in OpenELIS Global 2 that are required by this project can be pulled in by maven.

### Configuration files

This projects provides two configuration files that contains information about the analyzer instruments and test mappings.

1. **supported**: a list of supported analyzers instrument names each in it's own line. The instrument name could be a prefix e.g. *XN-*

2. **test_mapping**: contains a mapping of a test name from the analyzer to OpenELIS and units for the test.

## Installation
Compile the project and copy the JAR to: `/var/lib/openelis-global/plugins/` on the host running OpenELIS. Restart OpenELIS to load the plugin.
