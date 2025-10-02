import json
import csv
import sys
import os

input_file = sys.argv[1] if len(sys.argv) > 1 else "sbom.json"
repo_name = sys.argv[2] if len(sys.argv) > 2 else os.getenv("GITHUB_REPOSITORY", "unknown-repo").split("/")[-1]
output_file = f"sbom-packages-{repo_name}.csv"

with open(input_file, "r", encoding="utf-8") as f:
    sbom = json.load(f)

packages = sbom.get("packages", [])

columns = ["name", "type", "version"]

with open(output_file, "w", newline="", encoding="utf-8") as csvfile:
    writer = csv.DictWriter(csvfile, fieldnames=columns)
    writer.writeheader()
    for pkg in packages:
        row = {
            "name": pkg.get("name", ""),
            "type": pkg.get("type", ""),
            "version": pkg.get("versionInfo", "")
        }
        writer.writerow(row)

print(f"Package list CSV generated: {output_file}")
