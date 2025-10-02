# SBOM & Vulnerability Scanning Automation

This repository uses GitHub Actions to automatically generate a Software Bill of Materials (SBOM), scan for vulnerabilities, and produce package inventory reports.

All reports are named with the repository name for easy identification.

## Features

SBOM Generation: Uses Syft to generate an SPDX JSON SBOM.
SBOM Merging: Merges SBOMs for multiple tools if needed.
SBOM to CSV: Converts SBOM JSON to a CSV report.
Vulnerability Scanning: Uses Grype to scan the SBOM for vulnerabilities and outputs a CSV report.
Package Inventory: Extracts a simple package list (name, type, version) as a CSV.
Artifacts: All reports are uploaded as workflow artifacts with the repository name in the filename.

## Workflow Overview

The main workflow is defined in .github/workflows/sbom.yml

## Scripts

scripts/create-sbom.sh
Generates an SBOM for the repo and for specified tools, merging them as needed.
scripts/update-sbom.py
Merges additional SBOMs into the main SBOM.
.github/scripts/sbom_json_to_csv.py
Converts the SBOM JSON to a detailed CSV report.
.github/scripts/grype_json_to_csv.py
Converts Grype’s vulnerability scan JSON output to a CSV report.
Output columns: REPO, NAME, INSTALLED, FIXED-IN, TYPE, VULNERABILITY, SEVERITY
.github/scripts/sbom_packages_to_csv.py
Extracts a simple package inventory from the SBOM.
Output columns: name, type, version

## Example Reports

Vulnerability Report
grype-report-[RepoName].csv
REPO,NAME,INSTALLED,FIXED-IN,TYPE,VULNERABILITY,SEVERITY
my-repo,Flask,2.1.2,,library,CVE-2022-12345,High
...

Package Inventory
sbom-packages-[RepoName].csv
name,type,version
Flask,library,2.1.2
Jinja2,library,3.1.2
...

## Usage

Push to main branch or run the workflow manually.
Download artifacts from the workflow run summary.

## Customization

Add more tools to scripts/create-sbom.sh as needed.
Modify scripts to adjust report formats or add more metadata.
