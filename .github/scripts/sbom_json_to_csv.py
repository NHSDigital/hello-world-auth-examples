import json
import csv
import sys
# from pathlib import Path
from tabulate import tabulate

input_file = sys.argv[1] if len(sys.argv) > 1 else "sbom.json"
output_file = sys.argv[2] if len(sys.argv) > 2 else "sbom.csv"

with open(input_file, "r", encoding="utf-8") as f:
    sbom = json.load(f)

packages = sbom.get("packages", [])

columns = [
    "name",
    "versionInfo",
    "type",
    "supplier",
    "downloadLocation",
    "licenseConcluded",
    "licenseDeclared",
    "externalRefs"
]


def get_type(pkg):
    spdxid = pkg.get("SPDXID", "")
    if "-" in spdxid:
        parts = spdxid.split("-")
        if len(parts) > 2:
            return parts[2]
    refs = pkg.get("externalRefs", [])
    for ref in refs:
        if ref.get("referenceType") == "purl":
            return ref.get("referenceLocator", "").split("/")[0]
    return ""


def get_external_refs(pkg):
    refs = pkg.get("externalRefs", [])
    return ";".join([ref.get("referenceLocator", "") for ref in refs])


with open(output_file, "w", newline="", encoding="utf-8") as csvfile:
    writer = csv.DictWriter(csvfile, fieldnames=columns)
    writer.writeheader()
    for pkg in packages:
        row = {
            "name": pkg.get("name", ""),
            "versionInfo": pkg.get("versionInfo", ""),
            "type": get_type(pkg),
            "supplier": pkg.get("supplier", ""),
            "downloadLocation": pkg.get("downloadLocation", ""),
            "licenseConcluded": pkg.get("licenseConcluded", ""),
            "licenseDeclared": pkg.get("licenseDeclared", ""),
            "externalRefs": get_external_refs(pkg)
        }
        writer.writerow(row)

print(f"CSV export complete: {output_file}")


with open("sbom_table.txt", "w", encoding="utf-8") as f:
    table = []
    for pkg in packages:
        row = [
            pkg.get("name", ""),
            pkg.get("versionInfo", ""),
            get_type(pkg),
            pkg.get("supplier", ""),
            pkg.get("downloadLocation", ""),
            pkg.get("licenseConcluded", ""),
            pkg.get("licenseDeclared", ""),
            get_external_refs(pkg)
        ]
        table.append(row)
    f.write(tabulate(table, columns, tablefmt="grid"))
