import json
import csv
import sys

input_file = sys.argv[1] if len(sys.argv) > 1 else "grype-report.json"
output_file = sys.argv[2] if len(sys.argv) > 2 else "grype-report.csv"

with open(input_file, "r", encoding="utf-8") as f:
    data = json.load(f)

columns = ["NAME", "INSTALLED", "FIXED-IN", "TYPE", "VULNERABILITY", "SEVERITY"]

with open(output_file, "w", newline="", encoding="utf-8") as csvfile:
    writer = csv.DictWriter(csvfile, fieldnames=columns)
    writer.writeheader()
    for match in data.get("matches", []):
        pkg = match.get("artifact", {})
        vuln = match.get("vulnerability", {})
        row = {
            "NAME": pkg.get("name", ""),
            "INSTALLED": pkg.get("version", ""),
            "FIXED-IN": vuln.get("fix", {}).get("versions", [""])[0] if vuln.get("fix", {}).get("versions") else "",
            "TYPE": pkg.get("type", ""),
            "VULNERABILITY": vuln.get("id", ""),
            "SEVERITY": vuln.get("severity", ""),
        }
        writer.writerow(row)
print(f"CSV export complete: {output_file}")
